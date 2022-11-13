package domain;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
	private int ordersId;
	private String ordersDate;
//	private String userId;
	private boolean ordersIsDeleted;
	private int totalPrice;
	private int quantity;
	private List<OrderDetailDto> ordersDetailList;
	

	public Orders(int oId, List<OrderDetailDto> list) {
		this.ordersId = oId;
		this.ordersDate = new Date(System.currentTimeMillis()).toString();
		this.ordersIsDeleted = false;
		int price = 0;
		int quantity = 0;
		for(OrderDetailDto od : list) {
			price += od.getProduct().getProductPrice() * od.getQuantity();
			quantity += od.getQuantity();
		}
		
		this.totalPrice = price;
		this.quantity = quantity;
		this.ordersDetailList = list;
	}
	
	//json 객체 만들어서 출력보내는 함수 필요
	public JSONObject orderToJson(Orders order) {
		JSONObject jo = new JSONObject();
		jo.put("oid", order.getOrdersId());
		jo.put("date", order.getOrdersDate());
		jo.put("isDeleted", order.isOrdersIsDeleted());
		jo.put("totalPrice", order.getTotalPrice());
		jo.put("quantity", order.getQuantity());
		
		JSONArray jarr = new JSONArray();
		for(OrderDetailDto od : order.getOrdersDetailList()) {
			jarr.put(od.orderDetailDtoToJson(od));
		}
;		jo.put("ordersDetailList", jarr);
		
		return jo;
		
	}
	
	public Orders jsonToOrder(JSONObject jo) {
		Orders or = new Orders();
		or.setOrdersId(jo.getInt("oid"));
		or.setOrdersDate(jo.getString("date"));
		or.setOrdersIsDeleted(jo.getBoolean("isDeleted"));
		or.setTotalPrice(jo.getInt("totalPrice"));
		or.setQuantity(jo.getInt("quantity"));
		
		OrderDetailDto oddto = new OrderDetailDto();
		List<OrderDetailDto> ordersDetailList = new ArrayList<>();
		JSONArray arr = jo.getJSONArray("ordersDetailList");
		for(int i = 0; i < arr.length(); i++) {
			ordersDetailList.add(oddto.jsonToOrderDetailDto(arr.getJSONObject(i)));
		}
		or.setOrdersDetailList(ordersDetailList);
		
		return or;
	}
	
}
