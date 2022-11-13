package domain;

import java.util.List;

import org.json.JSONObject;

import lombok.Data;
@Data
public class Cart {
	private String userId;
	private int totalPrice;
	private int totalQuantity;
	
	//카트 : 카트디테일 1:N 
	//카트는 리스트로 카트디테일 리턴값을 가지고 있어야한다
	private List<CartDetail> cartDetailList;
	
	//생성자
	public Cart(String userId, List<CartDetail> list) {
		this.userId = userId;
		int price = 0;
		int quantity = 0;
		
		for(CartDetail cd : list) {
			price += cd.getProduct().getProductPrice() * cd.getQuantity();
			quantity += cd.getQuantity();
		}
		
		this.totalPrice = price;
		this.totalQuantity = quantity;
		this.cartDetailList = list;
	}
	
	//json 객체 만들어서 출력보내는 함수 필요
	public JSONObject cartToJson(Cart cart) {
			
		JSONObject json = new JSONObject();
		json.put("userId", cart.userId);
		json.put("totalPrice", cart.totalPrice);
		json.put("totalQuantity", cart.totalQuantity);
		json.put("cartDetailList", cart.cartDetailList);
			
		return json;
	}
	
	
}
