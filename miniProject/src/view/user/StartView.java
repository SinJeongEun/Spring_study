package view.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONObject;

import socket.client.ShoesClient;
import view.admin.AdminView;

public class StartView {
	// 필드
	JSONObject clientMessage = new JSONObject();
	ShoesClient shoesClient;
	Scanner sc = new Scanner(System.in);
	JSONObject result;

	public void startquestion(ShoesClient shoesClient) throws IOException, SQLException {
		this.shoesClient = shoesClient;
		clientMessage.put("viewName", "StartView");
		System.out.println("==<<  환영합니다  >>==");

		out: while (true) {
			System.out.println("원하시는 메뉴를 선택하여 주세요.");
			System.out.println("1. 회원가입 2. 로그인");
			System.out.print("선택 >");
			String num = sc.nextLine();
			System.out.println("-----------------------------------------------------");

			switch (num) {
			case "1":
				if (idcheck()) {
					continue;
				}
				if (pncheck()) {
					continue;
				}
				join();

			case "2":
				if (login()) {
					continue;
				}
				break out;
			}
		}
	}

	// 로그인 메소드
	public void join() throws IOException {
		clientMessage.put("function", "join");

		System.out.println("<<이름을 입력하세요.>>");
		String name = sc.nextLine();
		clientMessage.put("name", name);

		System.out.println("<<비밀번호를 입력하세요.>>");
		String password = sc.nextLine();
		clientMessage.put("password", password);

		System.out.println("<<주소를 입력해주세요.>>");
		String address = sc.nextLine();
		clientMessage.put("address", address);
		System.out.println("----------------------------------------------------");

		String cm = clientMessage.toString();
		try {
			shoesClient.send(cm);
			result = shoesClient.receive();
			System.out.println(result.getString("result"));

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("유저 뷰 에러");
		}

	}

	// 중복여부 체크 함수
	public boolean idcheck() throws IOException {
		clientMessage.put("function", "check");
		System.out.println("id 중복 여부를 검사합니다.");
		System.out.println("사용을 원하시는 id를 입력해 주시기 바랍니다.");
		System.out.print("입력 >");
		String id = sc.nextLine();
		clientMessage.put("id", id);
		shoesClient.send(clientMessage.toString());
		System.out.println(clientMessage.getString("id"));
		result = shoesClient.receive();
		
		//아이디가 중복이면 true 반납
		boolean have = result.getBoolean("result"); 
		if(have) {
			System.out.println("아이디 중복으로 사용 불가입니다. ");
			clientMessage.remove("id");
		}
		else {
			System.out.println("아이디 사용 가능합니다.");
		}
		return have;
	}

	public boolean pncheck() throws IOException {
		clientMessage.put("function", "check");
		System.out.println("핸드폰 중복 여부를 검사합니다.");
		System.out.println("사용을 원하시는 핸드폰 번호를 입력해 주시기 바랍니다.");
		System.out.print("phoneNum >");

		String pn = sc.nextLine();
		clientMessage.put("phoneNumber", pn);
		shoesClient.send(clientMessage.toString());
		result = shoesClient.receive();
		boolean have = result.getBoolean("result"); 
		if(have) {
			System.out.println("핸드폰 번호 중복으로 사용 불가입니다. ");
			clientMessage.remove("phoneNumber");
		}
		else {
			System.out.println("핸드폰 번호 사용가능합니다.");
		}
		return have;
	}

	public boolean login() throws IOException, SQLException {
		clientMessage.put("function", "login");

		System.out.println("로그인을 위한 id와 비밀번호를 입력하여 주세요.");
		System.out.println("<<id를 입력하세요.>>");
		String id = sc.nextLine();
		clientMessage.put("id", id);

		System.out.println("<<비밀번호를 입력하세요.>>");
		String password = sc.nextLine();
		clientMessage.put("password", password);

		String cm = clientMessage.toString();

		shoesClient.send(cm);
		
		result = shoesClient.receive(); //로그인이 성공되면 
		String type = result.getString("result");
		
		if (!type.equals("not")) {
			System.out.println("로그인 성공");
			
			
			if(result.getString("result").equals("ADMIN")) {
				AdminView adminView = new AdminView();
				adminView.startquestion(shoesClient);
			}
			
			else {
				UserView userView = new UserView();
				userView.startquestion(shoesClient);
			}
			
			return false;
		}
		else {
			System.out.println("로그인 실패");
			return true;
		}
		
	}
}
