package controller;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.CartDto;
import controller.dto.OrderDto;
import service.CartDetailService;
import service.CartService;
import service.OrdersDetailService;
import socket.server.SocketClient;

public class CartController implements Controller {
	//controller의 역할
	// receive() 로 json을 받는다.
	// json 을 -> DTO 로 변환한다.
	// 서비스를 호출한다. login(dto)
	// 리턴받은 결과를 json으로 받아서 send 한다.
	SocketClient socketClient;
	JSONObject cMessage;
	String result;
	
	OrdersDetailService orderDetailService = OrdersDetailService.getInstance();
	CartService cartService = CartService.getInstance();
	CartDetailService cartDetailService = CartDetailService.getInstance();
	CartDto cartDto = new CartDto();
	
	@Override
	public void control(SocketClient socketClient, JSONObject cMessage) throws SQLException, IOException {
		this.socketClient= socketClient;
		this.cMessage = cMessage;
		
		String function = cMessage.getString("function");
		switch(function) {
			case "createCart":
		
				cartService.createCart(cMessage.getString("userId"));
				break;
		
			case "showCart":
			System.out.println(cartService.showCart(socketClient.getLoginId()));
				JSONObject cart = cartDto.cartDtoToJson(cartService.showCart(socketClient.getLoginId()));
				cMessage.put("cart", cart);
				socketClient.send(cMessage.toString());
				break;
			
			case "removeCartDetailOne" :
				result = cartDetailService.removeCartDetailOne(socketClient.getLoginId(), cMessage.getInt("cdId"));
				cMessage.put("result", result);
				socketClient.send(cMessage.toString());
				break;
								
			case "addCartDetail" :
				JSONArray jarr = cMessage.getJSONArray("order");
				OrderDto od = new OrderDto(socketClient.getLoginId(), jarr.getInt(0), Integer.parseInt(jarr.getString(1)), Integer.parseInt(jarr.getString(2)), Integer.parseInt(jarr.getString(3)));
				
				result = cartDetailService.addCartDetail(od);
				cMessage.put("result", result);
				socketClient.send(cMessage.toString());
				break;
				
			case "updateCartDetailQuantity" :
				int id = cMessage.getInt("id");
				int quantity = cMessage.getInt("quantity");
				
				result = cartDetailService.updateCartDetailQuantity(socketClient.getLoginId(), id, quantity);
				cMessage.put("result", result);
				socketClient.send(cMessage.toString());
		}
	
	}


}
