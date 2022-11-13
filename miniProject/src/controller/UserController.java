package controller;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONObject;

import domain.User;
import service.UserService;
import socket.server.SocketClient;

//구현 클래스
public class UserController implements Controller {
	// field
	SocketClient socketClient;
	JSONObject cMessage;
	Object result;
	UserService userService = UserService.getInstance();

	// method: process() 재정의
	@Override
	public void control(SocketClient socketClient, JSONObject cMessage) throws SQLException, IOException {
		// variable
		this.socketClient = socketClient;
		this.cMessage = new JSONObject(cMessage);
		User user = new User();
		String function = cMessage.getString("function");

		// JSON <-> DTO
		switch (function) {
		case "join":
			// 사용자 정보를 DTO에 담음
			user.setUserId(cMessage.getString("id"));
			user.setUserName(cMessage.getString("name"));
			user.setUserPassword(cMessage.getString("password"));
			user.setPhoneNumber(cMessage.getString("phoneNumber"));
			user.setUserAddress(cMessage.getString("address"));
			// 사용자 DTO <-> 서비스 메소드 -> JSON
			result = userService.registerUser(user);
			cMessage.put("result", result);
			socketClient.send(cMessage.toString());
			break;

		case "check":
			// 하나의 값만 주고받기 때문에 굳이 DTO로 담지 않음
			if (cMessage.has("phoneNumber")) {
				result = userService.pnCheck(cMessage.getString("phoneNumber"));
				cMessage.put("result", result);
				socketClient.send(cMessage.toString());
				break;
			} else if (cMessage.has("id")) {
				result = userService.idCheck(cMessage.getString("id"));
				cMessage.put("result", result);
				socketClient.send(cMessage.toString());
				break;
			}

		case "login":
	
			// 사용자 정보를 DTO에 담음
			user.setUserId(cMessage.getString("id"));
			user.setUserPassword(cMessage.getString("password"));
			// 사용자 DTO <-> 서비스 메소드 -> JSON
			String type = userService.login(user);
		

			if (type != null) {
				socketClient.setLoginId(cMessage.getString("id"));
			} else {
				type = "not";
			}
			cMessage.put("result", type);
			socketClient.send(cMessage.toString());
			break;

		case "adJoin":
			// 사용자 정보를 DTO에 담음
			user.setUserId(cMessage.getString("id"));
			user.setUserName(cMessage.getString("name"));
			user.setUserPassword(cMessage.getString("password"));
			user.setPhoneNumber(cMessage.getString("phoneNumber"));
			user.setUserAddress(cMessage.getString("address"));
			user.setUserType("ADMIN");
			// 사용자 DTO <-> 서비스 메소드 -> JSON
			result = userService.registerAdmin(user);
			cMessage.put("result", result);
			socketClient.send(cMessage.toString());
			break;

		}

	}

}
