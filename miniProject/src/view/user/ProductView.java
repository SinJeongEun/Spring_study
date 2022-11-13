package view.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import controller.dto.ProductList;
import domain.Color;
import domain.Product;
import socket.client.ShoesClient;

public class ProductView {
	ShoesClient shoesClient;
	JSONObject clientMessage = new JSONObject();
	Scanner sc = new Scanner(System.in);
	PagingVo vo = new PagingVo();

	// productView를 시작할 때, 실행되는 메소드
	public void startquestion(ShoesClient shoesClient, int pnumber) throws SQLException, IOException {
		this.shoesClient = shoesClient;
		// 데이터 보내기
		clientMessage.put("viewName", "ProductView");
		clientMessage.put("function", "start");
		clientMessage.put("pageNum", pnumber);
		shoesClient.send(clientMessage.toString());

		// 데이터 받기
		JSONObject result = shoesClient.receive();

		// 객체리스트 출력
		JSONArray jsonarr = result.getJSONArray("productList");
		printProductList(jsonarr);

		// 페이지 네비게이터 출력
		JSONObject page = result.getJSONObject("pvo");
	
		vo = vo.jsonToPage(page);
		vo.navigator(vo);

		while (true) {
			System.out.println("원하시는 번호를 선택해 주세요");
			System.out
					.println("1. 페이지 이동을 하시겠습니까? 2. 필터링 기능을 이용하시겠습니까? 3. 원하는 상품번호를 선택하시겠습니까? 4. 초기화면으로 돌아가기 5. 뒤로가기");
			System.out.print("선택 >");
			String button = sc.nextLine();

			switch (button) {
			case "1":
				// 페이지 이동
				productPaging();
				continue;

			case "2":
				// 필터링 기능
				filter();
				continue;

			case "3":
				// 원하는 상품번호
				oneProduct(0);
				startquestion(shoesClient, 1);
				break;

			case "4":
				UserView UserView = new UserView();
				UserView.startquestion(shoesClient);
				break;

			case "5":
				clientMessage.put("viewName", "back");
				shoesClient.send(clientMessage.toString());
				JSONObject pView = shoesClient.receive();
				shoesClient.goBack(pView);
				break;
			}
			break;

		}

	}

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

		JSONObject result = shoesClient.receive();

		JSONArray jsonarr = result.getJSONArray("productList");
		printProductList(jsonarr);
		JSONObject page = result.getJSONObject("pvo");
		vo = vo.jsonToPage(page);
		vo.navigator(vo);

	}

	public void filter() throws IOException, SQLException {
		System.out.println("필터링");
		clientMessage.put("function", "filter");
		clientMessage.put("pageNum", 1);

		// 필터링 기능에서는 ClientMessage에 "standard", "data" 키가 추가적으로 담긴다.
		String standard ="";
		int data = 0;

		// 1) "standard" 키의 값 선택
		System.out.println("필터링 기준을 선택하세요.");
		System.out.println("1. 카테고리 2. 브랜드 3. 성별 4. 가격");
		System.out.print("선택> ");
		standard = sc.nextLine();
		
		// 2) "data" 키의 값 선택
		if (standard.equals("1")) { // 카테고리 기준
			System.out.println("원하시는 번호를 선택하세요.");
			System.out.println("1. 운동화 2. 워커");
			System.out.print("선택> ");
			data = Integer.parseInt(sc.nextLine());

		} else if (standard.equals("2")) { // 브랜드 기준
			System.out.println("원하시는 번호를 선택하세요.");
			System.out.println("1. 아디다스 2. 나이키");
			System.out.print("선택> ");
			data = Integer.parseInt(sc.nextLine());

		} else if (standard.equals("3")) { // 성별 기준
			System.out.println("원하시는 번호를 선택하세요.");
			System.out.println("1. male 2. female 3. all");
			System.out.print("선택> ");
			data = Integer.parseInt(sc.nextLine());

		} else if (standard.equals("4")) { // 가격 기준
			System.out.println("원하시는 번호를 선택하세요.");
			System.out.println("1. 낮은 가격 순 2. 높은 가격 순");
			System.out.print("선택> ");
			data = Integer.parseInt(sc.nextLine());
		}
		// 데이터 담아서 보내기
		clientMessage.put("standard", standard);
		clientMessage.put("data", data);
		shoesClient.send(clientMessage.toString());

		// 결과 처리
		JSONObject result = shoesClient.receive();
		JSONArray jsonarr = result.getJSONArray("productList");
		printProductList(jsonarr);

		JSONObject page = result.getJSONObject("pvo");
		
		vo = vo.jsonToPage(page);
		vo.navigator(vo);

		while (true) {
			System.out.println("원하시는 번호를 선택해 주세요");
			System.out.println("1. 페이지 이동을 하시겠습니까? 2. 다른 기준의 필터링 기능을 이용하시겠습니까? 3. 원하는 상품번호를 선택하시겠습니까? 4. 필터링 끄기 ");
			System.out.print("선택 >");
			String button = sc.nextLine();
			switch (button) {
			case "1":
				productPaging();
				continue;

			case "2":
				filter();
				continue;

			case "3":
				oneProduct(0);
				
			case "4":
				
				startquestion(shoesClient, 1);
				
				break;
				
			}
			
			break;
		}

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 여기서 부터 다른 뷰가 열린다. 내 영역이 아님
	public void oneProduct(int n) throws IOException, SQLException {
		Product p = new Product();
		int pId = 0;

		// 매개변수가 0이 들어오면 사용자에게 상품을 물어보고
		if (n == 0) {
			System.out.print("선택 >");
			String productId = sc.nextLine();
			pId = Integer.parseInt(productId);
		}
		
		// 값이 들어오면 해당 상품을 바로 보내준다.
		else pId = n;
		
		//데이터 보내기
		clientMessage.put("function", "showOne");
		clientMessage.put("productId", pId);
		shoesClient.send(clientMessage.toString());
		
		//보내고 난 후 필요없는 키는 삭제한다.
		clientMessage.remove("productId");
		
		//결과처리
		//결과 Json에 "result" 키가 있다면 상품이 존재하지 않음
		//결과 Json에 "product" 키가 있다면 가져올 상품이 존재
		JSONObject result = shoesClient.receive();
		if (result.has("result")) {
			System.out.println(result.getString("result"));
			startquestion(shoesClient, 1);
		}

		JSONObject jp = result.getJSONObject("product");
		p = p.jsonToProduct(jp);
		printProduct(p);
		
		//1개의 상품을 고르고 난 후 다른 서비스를 이용할 수 있다.
		System.out.println("-----------------------------------------------------");
		System.out.println("원하시는 메뉴를 선택하여 주세요.");
		System.out.println("1. 상품 주문하기 2. 상품 장바구니에 담기 3. 상품 리뷰 게시판  4. 상품 QnA 게시판 5. 뒤로가기");
		System.out.print("선택 >");
		int menu = Integer.parseInt(sc.nextLine());
		System.out.println("-----------------------------------------------------");

		switch (menu) {
		case 1:
			// 상품 바로 주문하기
			OrderView orderView = new OrderView();
			orderView.orderProduct(shoesClient, pId);
			break;

		case 2:
			// 장바구니에 담기 : 상품에서 생상, 사이즈, 수량을 정한 후 장바구니에 담는다.
			CartView cartView = new CartView();
			cartView.addToCart(shoesClient, pId);
			
			break;

		case 3:
			ReviewView reviewView = new ReviewView();
			reviewView.startquestion(shoesClient, pId);
			break;

		case 4:
			QnaView qnaView = new QnaView();
			qnaView.startquestion(shoesClient, pId);
			break;

		case 5:
			clientMessage.put("viewName", "back");
			shoesClient.send(clientMessage.toString());
			JSONObject pView = shoesClient.receive();
			shoesClient.goBack(pView);
			break;

		}
		

	}
	
	//결과에 상품 리스트가 온다면 상품 리스트를 출력하는 메소드
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
	
	//결과에 1개의 상품이 온다면 상품을 출력하는 메소드
	public void printProduct(Product p) {
		System.out.println(
				"=======================================상품 상세보기입니다.=============================================================");
		System.out.println("");

		System.out.print("상품 번호: " + p.getProductId() + "\t");
		System.out.print("상품 이름: " + p.getProductName() + "\t");
		System.out.print("상품 가격: " + p.getProductPrice() + "\t");
		System.out.print("상품 성별: " + p.getProductSex() + "\t");
		System.out.print("상품 브랜드: " + p.getCompany() + "\t");
		System.out.println("상품 카테고리: " + p.getCategory());
		System.out.println();
		System.out.println("<색상>");
		for (Color c : p.getColorList()) {
			System.out.print("상품 색상 번호: " + c.getColor_id() + "\t");
			System.out.println("상품 색상 이름: " + c.getColor_name());
		}
		System.out.println();
		System.out.println("<사이즈>");
		for (Integer s : p.getSizeList()) {
			System.out.println("상품 사이즈: " + s);
		}
		System.out.println("");
		System.out.println(
				"===================================================================================================================");
	}
}
