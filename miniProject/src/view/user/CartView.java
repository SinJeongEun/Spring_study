package view.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.CartDetailDto;
import controller.dto.CartDto;
import socket.client.ShoesClient;

public class CartView {
	ShoesClient shoesClient;
	JSONObject result;
	JSONObject clientMessage = new JSONObject();
	Scanner sc = new Scanner(System.in);
	String s[];
	JSONArray arr;
	CartDto cartDto = new CartDto();

	public void startquestion(ShoesClient shoesClient) throws IOException, SQLException {
		this.shoesClient = shoesClient;
		clientMessage.put("viewName", "CartView");
		clientMessage.put("function", "showCart");
		shoesClient.send(clientMessage.toString());
		System.out.println("=================== 장바구니 ♥ ======================= ");

		CartDto cart = cartDto.jsonToCartDto(shoesClient.receive().getJSONObject("cart"));
		System.out.println("총 가격 : " + cart.getTotalPrice() + "     총 수량  : " + cart.getTotalQuantity());
		System.out.println();

		if (cart.getCartDetailDtoList().size() == 0)
			System.out.println("장바구니가 비었습니다.");
		else {
			for (CartDetailDto cd : cart.getCartDetailDtoList()) {
				System.out.println("-----------------------------------------------------");
				if (cd.isProduct_isDeleted())
					System.out.print("[ 삭제된 상품입니다 ]  ");
				System.out.println("번호 : " + cd.getCart_detail_id() + "   상품 이름 : " + cd.getProduct_name() + " 사이즈 : "
						+ cd.getSize_id() + " 색상 : " + cd.getColor_name() + "   총 금액 : " + cd.getTotalPrice()
						+ "   총 수량 : " + cd.getQuantity());
			}

		}

		System.out.println("========================================================= ");
		
		boolean flag = true;
		while (flag) {
			System.out.println("**--환영합니다--**");
			System.out.println("원하시는 메뉴를 선택하여 주세요.");
			System.out.println("1. 장바구니 내역 삭제하기 2. 장바구니 내역 주문하기  3. 수량 변경하기 4. 뒤로가기 ");
			System.out.print("선택 >");
			int menu = Integer.parseInt(sc.nextLine());
			System.out.println("-----------------------------------------------------");

			switch (menu) {
			case 1:
				System.out.println("삭제할 항목 id를 선택하여 주세요.");
				System.out.print("선택 >");
				int n = Integer.parseInt(sc.nextLine());
				clientMessage.put("viewName", "CartView");
				clientMessage.put("function", "removeCartDetailOne");
				clientMessage.put("cdId", n);
				shoesClient.send(clientMessage.toString());
				System.out.println(shoesClient.receive().get("result"));
				break;

			case 2:
				OrderView orderView = new OrderView();
				orderView.orderCartDetails(shoesClient);
				
				System.out.println(shoesClient.receive().get("result"));			
				break;

			case 3:
				System.out.println("수량을 변경할 항목의 id를 선택하여 주세요.");
				System.out.print("선택 >");
				int id = Integer.parseInt(sc.nextLine());

				System.out.println("수량을 입력해주세요. ");
				System.out.print("수량 >");
				int quantity = Integer.parseInt(sc.nextLine());

				clientMessage.put("viewName", "CartView");
				clientMessage.put("function", "updateCartDetailQuantity");
				clientMessage.put("id", id);
				clientMessage.put("quantity", quantity);
				shoesClient.send(clientMessage.toString());

				System.out.println(shoesClient.receive().get("result"));
				break;
				
			case 4:
				clientMessage.put("viewName", "back");
				shoesClient.send(clientMessage.toString());
				
				JSONObject pView = shoesClient.receive();
				shoesClient.goBack(pView);
				flag = false;
				break;
			}
		}

	}

	// 상품1개 장바구니에 담기
	public void addToCart(ShoesClient shoesClient, int productId) throws IOException {
		System.out.println("==== 장바구니에 담기 ♥ ==== ");
		System.out.println("원하시는 색상번호, 사이즈, 수량을 입력해주세요.");
		System.out.print("입력 >");
		s = sc.nextLine().split(" ");
		arr = new JSONArray();
		arr.put(productId);
		arr.putAll(s);
		System.out.println("======================= ");

		clientMessage.put("viewName", "CartView");
		clientMessage.put("function", "addCartDetail");
		clientMessage.put("order", arr);
		shoesClient.send(clientMessage.toString());
		System.out.println(shoesClient.receive().get("result"));
	}

	public void createCart(String userId) throws IOException {
		clientMessage.put("viewName", "CartView");
		clientMessage.put("function", "addCartDetail");
		clientMessage.put("userId", userId);
		shoesClient.send(clientMessage.toString());
	}

}
