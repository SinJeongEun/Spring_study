package controller.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import domain.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
	private int totalPrice;
	private int totalQuantity;
	
	private List<CartDetailDto> cartDetailDtoList;
	
	public CartDto cartToDto(Cart cart) {
		CartDetailDto cd = new CartDetailDto();
		return new CartDto(cart.getTotalPrice(), cart.getTotalQuantity(), cd.toCartDetailDtoList(cart.getCartDetailList()));
	}

	
	public CartDto jsonToCartDto(JSONObject json) {
		JSONArray ctoArray = json.getJSONArray("cartDetailDtoList");
		List<CartDetailDto> list = new ArrayList<>();
		CartDetailDto cd = new CartDetailDto();
		for(int i = 0;i < ctoArray.length(); i++) {
			//jsonObject -> cartDetailDto
			CartDetailDto cdto = cd.jsonToCartDetailDto(ctoArray.getJSONObject(i));
			list.add(cdto);
		}
		
		return new CartDto(json.getInt("totalPrice"), json.getInt("totalQuantity"), list);
	}
	
	public JSONObject cartDtoToJson(CartDto cdto) {
		JSONObject jo = new JSONObject();
		CartDetailDto cd = new CartDetailDto();
		
		JSONArray jsonArr = new JSONArray();
		for(CartDetailDto c : cdto.getCartDetailDtoList()) {  
			jsonArr.put(cd.cartDetailDtoToJson(c));
		}
		
		jo.put("totalPrice", cdto.getTotalPrice());
		jo.put("totalQuantity", cdto.getTotalQuantity());
		jo.put("cartDetailDtoList", jsonArr);
		
		return jo;
	}
}
