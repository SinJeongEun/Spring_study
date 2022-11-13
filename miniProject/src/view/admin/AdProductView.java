package view.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import controller.dto.ProductList;
import controller.dto.ProductSex;
import domain.Color;
import domain.Product;
import socket.client.ShoesClient;

public class AdProductView {
	ShoesClient shoesClient;
	JSONObject clientMessage = new JSONObject();
	Scanner sc = new Scanner(System.in);
	PagingVo vo = new PagingVo();
	
	JSONObject result;
	JSONArray jsonarr;
	JSONObject page;
	JSONObject jp;
	

	public void startquestion(ShoesClient shoesClient) throws SQLException, IOException {
		this.shoesClient = shoesClient;

		clientMessage.put("viewName", "ProductView");
		clientMessage.put("function", "start");
		clientMessage.put("pageNum", 1);
		shoesClient.send(clientMessage.toString());
		result = shoesClient.receive();

		jsonarr = result.getJSONArray("productList");
		printProductList(jsonarr);

		page = result.getJSONObject("pvo");
		vo = vo.jsonToPage(page);
		vo.navigator(vo);
		

		sc = new Scanner(System.in);
		 while (true) {
			System.out.println("**--관리자님--**");
			System.out.println("1. 페이지 이동을 하시겠습니까? 2. 상품을 등록하시겠습니까? 3. 관리를 원하는 상품을 선택하시겠습니까? 4. 뒤로가기");
			System.out.print("선택 >");
			String button = sc.nextLine();
			switch (button) {
			case "1":
				productPaging();
				continue;
			case "2":
				registerProduct();
				startquestion(shoesClient);
				break;
			case "3":
				oneProduct();
				continue;
			case "4":
				AdminView adminView = new AdminView();
				adminView.startquestion(shoesClient);
			}
			break;
		}

		

	}

	public void registerProduct() throws IOException {
		clientMessage.put("function", "registerProduct");
		System.out.print("상품명을 입력하세요> ");
		String name = sc.nextLine();
		clientMessage.put("name", name);

		System.out.print("가격을 입력하세요> ");
		int price = Integer.parseInt(sc.nextLine());
		clientMessage.put("price", price);

		System.out.println("사이즈를 한칸씩 띄어서 입력하세요");
		System.out.println("220 ~ 280 중에 선택하세요. 사이즈는 10단위로 입력 가능합니다.");
		System.out.print("선택 >");
		String[] size = sc.nextLine().split(" ");
		JSONArray jarrSize = new JSONArray();
		jarrSize.putAll(size);
		clientMessage.put("size", jarrSize);

		System.out.println("색상 번호를 한칸씩 띄어서 입력하세요");
		System.out.println("1. red 2. orange 3. yellow 4. green 5. blue 6. navy 7. purple 8. white 9. black");
		System.out.print("선택 >");
		String[] color = sc.nextLine().split(" ");
		JSONArray jarrColor = new JSONArray();
		jarrColor.putAll(color);
		clientMessage.put("color", jarrColor);

		System.out.println("카테고리 번호를 선택하여 주세요");
		System.out.println("1. 운동화 2. 워커");
		System.out.print("선택 >");
		int categoryId = Integer.parseInt(sc.nextLine());
		clientMessage.put("categoryId", categoryId);

		System.out.println("회사 번호를 선택하여 주세요");
		System.out.println("1. 아디다스 2. 나이키");
		System.out.print("선택 >");
		int companyId = Integer.parseInt(sc.nextLine());
		clientMessage.put("companyId", companyId);

		System.out.println("성별을 입력하세요");
		System.out.println("1. male 2. female 3. all");
		System.out.print("선택 >");
		String sex = ProductSex.choosePtype(Integer.parseInt(sc.nextLine()));
		clientMessage.put("productSex", sex);

		shoesClient.send(clientMessage.toString());
		result = shoesClient.receive();

		System.out.println(result.getString("result"));
	}

	public boolean oneProduct() throws IOException, SQLException {
		Product p = new Product();

		System.out.print("선택 >"); 
		String productId = sc.nextLine();

		int pId = Integer.parseInt(productId);
		clientMessage.put("function", "showOne");
		clientMessage.put("productId", pId);
		shoesClient.send(clientMessage.toString());
		result = shoesClient.receive();
		
		if(result.has("result")) {
			System.out.println(result.getString("result"));
			startquestion(shoesClient);
		}
		
		jp= result.getJSONObject("product");
		p = p.jsonToProduct(jp);
		printProduct(p);
		
		
		System.out.println("원하시는 메뉴를 선택하여 주세요.");
		System.out.println("1.상품 삭제 2.리뷰 관리 3. QnA 관리 4. 뒤로가기 ");
		System.out.print("선택 >");
		int menu = Integer.parseInt(sc.nextLine());
		System.out.println("-----------------------------------------------------");

		switch (menu) {
		case 1:// 상품 삭제
			clientMessage.put("function", "deleteProduct");
			shoesClient.send(clientMessage.toString());
			String deleteProduct = shoesClient.receive().getString("result");
			System.out.println(deleteProduct);
			startquestion(shoesClient);
			break;
			
		case 2:// 리뷰 관리
	         AdReviewView adReviewView = new AdReviewView();
	         adReviewView.startquestion(shoesClient,pId);
	         break;
	      case 3:// QnA 관리
	         AdQnaView adqnaView = new AdQnaView();
	         adqnaView.startquestion(shoesClient,pId);
	         break;
			
		case 4:
			startquestion(shoesClient);
			break;	

		}
		
		
		return true;

	}

	// 페이징 기능의 메소드
	public void productPaging() throws IOException {
		System.out.println("원하시는 페이지 번호 혹은 글자를 입력헤주세요.");
		System.out.print("입력 >");
		String num = sc.nextLine();
		// 글자 입력이면 계산해준다.
		int pageNum = 0;
		if (num.equals("맨앞")) {
			pageNum = 1;
		} else if (num.equals("이전")) {
			pageNum = vo.getStartPageNo() - 1;
		} else if (num.equals("다음")) {
			pageNum = vo.getEndPageNo() + 1;
		} else if (num.equals("맨뒤")) {
			pageNum = vo.getTotalPageNo();
		} else {
			pageNum = Integer.parseInt(num);
		}

		clientMessage.put("pageNum", pageNum);
		shoesClient.send(clientMessage.toString());
		result = shoesClient.receive();
		jsonarr = result.getJSONArray("productList");
		printProductList(jsonarr);
		page = result.getJSONObject("pvo");
		vo = vo.jsonToPage(page);
		vo.navigator(vo);

	}

	// jsonArray를 dto로 변환, 출력의 기능을 가진 메소드이다.
	public void printProductList(JSONArray ja) {
		ProductList p = new ProductList();
		System.out.println(
				"=======================================상품 List 입니다.=============================================================");
		System.out.println("");
		for (int i = 0; i < ja.length(); i++) {
			ProductList list = p.JsonTOproductList(ja.getJSONObject(i));
			System.out.print("상품 번호 : " + list.getProductId() + "\t");
			System.out.print("상품 이름: " + list.getProductName() + "\t\t");
			System.out.print("브랜드: " + list.getCompanyName() + "\t");
			System.out.print("카테고리: " + list.getCategoryName() + "\t");
			System.out.print("상품 성별: " + list.getProductSex() + "\t");
			System.out.println("가격:" + list.getProductPrice());
		}
		System.out.println("");
		System.out.println(
				"===================================================================================================================");
	}

	public void printProduct(Product p) {

		System.out.println(
				"=======================================상품 상세보기입니다.=============================================================");
		System.out.println("");
		
		System.out.print("상품 번호: " +p.getProductId()+"\t");
		System.out.print("상품 이름: " +p.getProductName()+"\t");
		System.out.print("상품 가격: " +p.getProductPrice()+"\t");
		System.out.print("상품 성별: " +p.getProductSex()+"\t");
		System.out.print("상품 브랜드: " +p.getCompany()+"\t");
		System.out.println("상품 카테고리: " +p.getCategory());
		
		System.out.println();
		System.out.println("<색상>");
		for(Color c : p.getColorList()) {
			System.out.print("상품 색상 번호: " +c.getColor_id()+"\t");
			System.out.println("상품 색상 이름: " +c.getColor_name());
		}
		
		System.out.println();
		System.out.println("<사이즈>");
		for(Integer s : p.getSizeList()) {
			System.out.println("상품 사이즈: " +s);
		}
		

		System.out.println("");
		System.out.println(
				"===================================================================================================================");
	}

}
