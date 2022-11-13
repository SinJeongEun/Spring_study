package domain;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
	private int ordersDetailId;
	private int quantity;
	private Product product;
//	private int orders_id;
	private int size_id;
	private String color;
	
	public JSONObject orderDetailDtoToJson(OrderDetailDto oddto) {
		JSONObject jo = new JSONObject();
		
		jo.put("id", oddto.getOrdersDetailId());
		jo.put("quantity", oddto.getQuantity());
		jo.put("product", oddto.product.productToJson(oddto.getProduct()));
		jo.put("size", oddto.getSize_id());
		jo.put("color", oddto.getColor());
		return jo;
	}
	
	public OrderDetailDto jsonToOrderDetailDto(JSONObject jo) {
		OrderDetailDto od = new OrderDetailDto();
		Product product = new Product();
		od.setOrdersDetailId(jo.getInt("id"));
		od.setQuantity(jo.getInt("quantity"));
		od.setProduct(product.jsonToProduct(jo.getJSONObject("product")));
		od.setSize_id(jo.getInt("size"));
		od.setColor(jo.getString("color"));
		return od;
		
	}
}
