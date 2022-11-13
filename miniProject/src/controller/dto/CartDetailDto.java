package controller.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import domain.CartDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailDto {
	// 장바구니 상세보기 보여주기 위한 dto
	private int cart_detail_id;
	private String product_name;
	private boolean product_isDeleted;
	private int size_id;
	private String color_name;
	private int quantity;
	private int totalPrice;
	
	public CartDetailDto cartDetailToCartDetailDto(CartDetail cd) {
		int price = cd.getQuantity() * cd.getProduct().getProductPrice();
		
		return new CartDetailDto(cd.getCart_detail_id(), cd.getProduct().getProductName(), cd.getProduct().isDeleted(), cd.getSize_id(),
				cd.getColor().getColor_name(), cd.getQuantity(), price);
	}
	
	
	public List<CartDetailDto> toCartDetailDtoList(List<CartDetail> cdList) {
		List<CartDetailDto> list = new ArrayList<>();
		
		for(CartDetail cd : cdList) {
			list.add(cartDetailToCartDetailDto(cd));
		}
		
		return list;
	}
	
	public CartDetailDto jsonToCartDetailDto(JSONObject json) {		
		 int cdId = json.getInt("cdId");
		 String pname = json.getString("pname");
		 boolean isDeleted = json.getBoolean("isDeleted");
		 int sid = json.getInt("sid");
		 String cname = json.getString("cname");
		 int quantity = json.getInt("quantity");
		 int totalPrice = json.getInt("totalPrice");
		 
		 return new CartDetailDto(cdId, pname, isDeleted, sid, cname, quantity, totalPrice);
	}
	
	public JSONObject cartDetailDtoToJson(CartDetailDto cdto) {		
		 JSONObject jo = new JSONObject();
		 jo.put("cdId", cdto.getCart_detail_id());
		 jo.put("pname", cdto.getProduct_name());
		 jo.put("isDeleted", cdto.product_isDeleted);
		 jo.put("sid", cdto.getSize_id());
		 jo.put("cname", cdto.getColor_name());
		 jo.put("quantity", cdto.getQuantity());
		 jo.put("totalPrice", cdto.getTotalPrice());
		 
		 return jo;
	}

}
