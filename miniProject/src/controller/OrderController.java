package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.OrderDto;
import controller.dto.PagingVo;
import domain.Orders;
import service.OrderService;
import service.OrdersDetailService;
import socket.server.SocketClient;

public class OrderController implements Controller {
	//controller의 역할
	// receive() 로 json을 받는다.
	// json 을 -> DTO 로 변환한다.
	// 서비스를 호출한다. login(dto)
	// 리턴받은 결과를 json으로 받아서 send 한다.
//	String str;
//	JSONArray jarr;
//	
	OrderService orderService = OrderService.getInstance();
	OrdersDetailService ordersDetailService = OrdersDetailService.getInstance();
	
	@Override
	public void control(SocketClient socketClient, JSONObject clientMessage) throws SQLException, IOException {		
		String function = clientMessage.getString("function");
		switch(function) {		
			// 상품 상세보기에서 바로 상품 주문하기 
			case "addProductToOrder":
				JSONArray jarr = clientMessage.getJSONArray("order");
				OrderDto od = new OrderDto(socketClient.getLoginId(), jarr.getInt(0), jarr.getInt(1), jarr.getInt(2), jarr.getInt(3));
				
				String str = ordersDetailService.addProductToOrder(od);
				
				clientMessage.put("result", str);
				socketClient.send(clientMessage.toString());
				break;
				
			// 장바구니에서 상품들 주문하기 
			case "addCartDetailsToOrder" :
				str = ordersDetailService.addCartDetailsToOrder(socketClient.getLoginId(), clientMessage.getJSONArray("cartDetailIdList").toList());
				clientMessage.put("result", str);
				socketClient.send(clientMessage.toString());
				break;
			
			// 나의 모든 주문 조회 
			case "showOrders":
				int pageNum = clientMessage.getInt("pageNum");
				int cnt = orderService.countAll(socketClient.getLoginId());
				PagingVo pvo = new PagingVo(cnt, pageNum, 3);
				
				List<Orders> list = orderService.showOrders(socketClient.getLoginId(), pvo);
				jarr = new JSONArray();
				for(Orders d : list) {
					jarr.put(d.orderToJson(d));
				}
				
				clientMessage.put("pvo", pvo.pageToJson(pvo));
				clientMessage.put("orders", jarr);
				socketClient.send(clientMessage.toString());
				break;
				
			// 주문 취소 
			case "cancelOrders":
				jarr = clientMessage.getJSONArray("cancleList");
				List<Integer> cancleList = new ArrayList<>();
				
				for(int i = 0; i < jarr.length(); i++) {
					cancleList.add(Integer.parseInt(jarr.getString(i)));
				}
				
				String result = orderService.cancelOrders(cancleList);
				clientMessage.put("result", result);
				socketClient.send(clientMessage.toString());

		}
	
	}


}
