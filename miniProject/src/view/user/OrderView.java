package view.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import domain.OrderDetailDto;
import domain.Orders;
import socket.client.ShoesClient;

public class OrderView {
	ShoesClient shoesClient;
	Scanner sc = new Scanner(System.in);
	String s[];
	JSONArray arr;
	
	Orders orders = new Orders();
	PagingVo vo = new PagingVo();
	
	// 상품에서 바로 주문하기 
	public void orderProduct(ShoesClient shoesClient, int productId) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("==== 주문서 작성하기 ♥ ==== "); 
		
		System.out.println("원하시는 색상번호, 사이즈, 수량을 입력해주세요.");
		System.out.print("입력 >");
		s = sc.nextLine().split(" ");
		arr = new JSONArray();
		arr.put(productId);
		arr.putAll(s);
		
		System.out.println("======================= ");
		
		JSONObject clientMessage = new JSONObject();
		clientMessage.put("viewName", "OrderView"); 
		clientMessage.put("function", "addProductToOrder");
		clientMessage.put("pageNum", 0);
		clientMessage.put("order", arr);
		shoesClient.send(clientMessage.toString());

		System.out.println(shoesClient.receive().get("result") + " !");
		System.out.println("-----------------------------------------------------");


	}
	
	// 장바구니 내역들 주문하기
	public void orderCartDetails(ShoesClient shoesClient) throws IOException {
		System.out.println("주문할 항목의 id를 선택하여 주세요. 여러개인 경우 한칸씩 띄어서 입력해주세요. ");
		System.out.print("입력 >");
		String[] s = sc.nextLine().split(" ");

		// JSONArray 로 배열을 보내야 된다.
		JSONArray jarr = new JSONArray();
		jarr.putAll(s);
		
		JSONObject clientMessage = new JSONObject();
		clientMessage.put("viewName", "OrderView");
		clientMessage.put("function", "addCartDetailsToOrder");
		clientMessage.put("pageNum", 0);
		clientMessage.put("cartDetailIdList", jarr);
		shoesClient.send(clientMessage.toString());
		
	}
	
	// 주문 내역 보기 , 주문취소 및 리뷰글 작성으로 이
	public void showOrders(ShoesClient shoesClient, int page) throws IOException, SQLException {
		JSONObject clientMessage = new JSONObject();
		clientMessage.put("viewName", "OrderView"); 
		clientMessage.put("function", "showOrders");
		clientMessage.put("pageNum", page);
		shoesClient.send(clientMessage.toString());
		
		JSONObject jo = shoesClient.receive();
		arr = jo.getJSONArray("orders");
		vo = vo.jsonToPage(jo.getJSONObject("pvo"));
		
		// 받은 주문 리스트 출력하기 , 네비게이터 
		pagingOrders(shoesClient, page + "");
		
		// 페이지 이동, 주문을 취소하거나 , 리뷰를 작성하거나
		while (true) {
			System.out.println("원하시는 번호를 선택해 주세요");
			System.out.println("1. 페이지 이동  2. 주문 취소  3. 리뷰 작성하기 4. 뒤로가기 ");
			System.out.print("선택 >");
			int num = Integer.parseInt(sc.nextLine());

			switch(num) {
			case 1:
				// 페이지 이동하기 
				System.out.println("원하시는 페이지 번호 혹은 글자를 입력헤주세요. ");
				System.out.print("입력 >");
				String n = sc.nextLine();
				pagingOrders(shoesClient, n);									
				break;
				
			case 2:
				cancleOrders(shoesClient, page);
				break;
				
			case 3:
				// 리뷰 작성 뷰 -> 컨트롤러로 이동하기 
				ReviewView reviewView = new ReviewView();
				System.out.println("리뷰를 작성할 상품번호를 입력해주세요. ");
				System.out.print("선택 >");
				int pid = Integer.parseInt(sc.nextLine());
				clientMessage.put("productId", pid);
				
				reviewView.writeReivew(shoesClient, clientMessage);
				break;
				
			case 4:
//				clientMessage.put("viewName", "back");
//				shoesClient.send(clientMessage.toString());
//				
//				JSONObject pView = shoesClient.receive();
//				shoesClient.goBack(pView);
				ProductView p = new ProductView();
				p.startquestion(shoesClient, 1);
				break;
			}
			
		}
		
		
		
		
		
		
		
		
	}
	
	public void printOrders(JSONArray arr) {
		if(arr.length() == 0) System.out.println("주문내역이 없습니다. ");
		else {
			List<Orders> list = new ArrayList<>();
			Orders order;
			for(int i=0; i< arr.length(); i++) {
				order = orders.jsonToOrder(arr.getJSONObject(i));
				list.add(order);						
			}
			
			System.out.println();
			System.out.println("=================== 나의 주문 내역 ♥ ======================= "); 
			for(Orders od : list) {
				System.out.println("-----------------------------------------------------");
				
				
				if(od.isOrdersIsDeleted()) System.out.print("[ 취소된 주문 ]  ");
				System.out.println("주문번호[" +od.getOrdersId() + "]     주문날짜 : " +od.getOrdersDate() + "     총 금액 : " + od.getTotalPrice() + "   총 수량 : " + od.getQuantity());
				System.out.println();
				
				// 해당 주문에 대한 상세 주문들
				for(OrderDetailDto oddto : od.getOrdersDetailList()) {
					System.out.println("상품 번호(" +oddto.getProduct().getProductId() + ")" + "상품 이름 : " + oddto.getProduct().getProductName() + " 상품 가격 : " + oddto.getProduct().getProductPrice() 
							+ " 주문 수량 : " + oddto.getQuantity() + " 사이즈 : " + oddto.getSize_id() + " 색상 : " + oddto.getColor());
				}
				
			}	
			System.out.println("========================================================= "); 
			
			// 네비게이터
			
			
		}
	}
	
	public void cancleOrders(ShoesClient shoesClient, int pageNum) throws IOException {
		System.out.println("취소할 주문번호를 입력해주세요. 여러개인 경우 한칸씩 띄어서 입력해주세요. ");
		System.out.print("선택 >");
   	 	String[] s = sc.nextLine().split(" ");
   	 	
   	 	arr = new JSONArray();
   	 	for(String a : s) {
   	 		arr.put(a);
   	 	}
   	 
   	 	JSONObject clientMessage = new JSONObject();
		clientMessage.put("viewName", "OrderView"); 
		clientMessage.put("function", "cancelOrders");
		clientMessage.put("cancleList", arr);
		clientMessage.put("pageNum", pageNum);
		shoesClient.send(clientMessage.toString());
		System.out.println(shoesClient.receive().getString("result"));
		
	}
	
	public  void pagingOrders(ShoesClient shoesClient, String num) throws IOException {
		// 페이징 기능의 메소드
//       처음에는 이 부분이 뜨지 않는다.
//       1) 사용자로 부터 원하는 페이지를 입력받는다
//      if (!num.equals("")) {
//         System.out.println("원하시는 페이지 번호 혹은 글자를 입력헤주세요.");
//         num = sc.nextLine();
//      }
      // 2) 사용자가 입력한 값이 문자이던지 숫자이던지 상관없이 원하는 페이지를 계산하여 주는 부분
		
		JSONObject clientMessage = new JSONObject();
      int pageNum = 0;
      
      if (num.equals("맨앞") | num.equals("처음")) {
         pageNum = 1;
      } 
      else if (num.equals("이전")) {
         pageNum = vo.getStartPageNo() - 1;
      } 
      else if (num.equals("다음")) {
         pageNum = vo.getEndPageNo() + 1;
      } 
      else if (num.equals("맨뒤")) {
         pageNum = vo.getTotalPageNo();
      } 
      else {
         
         pageNum = Integer.parseInt(num);
      }
      
      clientMessage.put("viewName", "OrderView"); 
	  clientMessage.put("function", "showOrders");
      clientMessage.put("pageNum", pageNum);
      shoesClient.send(clientMessage.toString());

      // 3) 서버로부터 받은 결과를 처리한다
      JSONObject result = shoesClient.receive();
      
      // 4) 먼저 상품리스트를 출력해준다.
      JSONArray orders = result.getJSONArray("orders");
      printOrders(orders);
      
      // 5) JSONObject 타입의 페이지를 dto로 변환
      // 네비게이터 호출
      JSONObject page = result.getJSONObject("pvo");
      vo = vo.jsonToPage2(page);
      vo.navigator(vo);    
		   
	}	

}
