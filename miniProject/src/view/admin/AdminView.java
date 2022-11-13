package view.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONObject;

import socket.client.ShoesClient;
import view.user.StartView;

public class AdminView {

	public void startquestion(ShoesClient shoesClient) throws SQLException, IOException {
		Scanner sc = new Scanner(System.in);
		
		boolean flag = true;
		while (flag) {
			System.out.println("====반갑습니다★ 관리자님====");

			System.out.println("원하시는 서비스를를 선택하여 주세요.");
			System.out.println("1. 상품서비스 2. 새 관리자 등록하기 3. 로그아웃 ");
			System.out.print("선택 >");
			String num = sc.nextLine();
			System.out.println("-----------------------------------------------------");

			switch (num) {
			case "1":
				AdProductView adProductView = new AdProductView();
				adProductView.startquestion(shoesClient);
				break;
			case "2":
				AdUserView adUserView = new AdUserView();
				adUserView.startquestion(shoesClient);
				break;
			case "3":
				JSONObject clientMessage = new JSONObject();
				clientMessage.put("viewName", "logout");
				shoesClient.send(clientMessage.toString());
				System.out.println(shoesClient.receive().getString("result"));
				StartView startView = new StartView();
				startView.startquestion(shoesClient);
				break;
			}
		}

	}

}
