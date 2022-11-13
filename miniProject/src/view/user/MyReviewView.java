package view.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import socket.client.ShoesClient;

public class MyReviewView {
	//field
	ShoesClient shoesClient;
	JSONObject clientMessage = new JSONObject();
	Scanner sc = new Scanner(System.in);
	PagingVo pvo = new PagingVo();
	
	//method: [마이페이지] - 나의 리뷰
	public void startquestion(ShoesClient shoesClient) throws IOException, SQLException {
		//variable
		this.shoesClient = shoesClient;
		ReviewView reviewView = new ReviewView();
		
		//Json - put
		clientMessage.put("viewName", "MyReviewView"); 
		clientMessage.put("function", "myList");
		clientMessage.put("pageNum", 1); 
		//Json - send
		shoesClient.send(clientMessage.toString());
		//Json - receive
		JSONObject json = shoesClient.receive();
		JSONArray jsonArr = json.getJSONArray("reviewList");
		pvo = pvo.jsonToPage(json.getJSONObject("pvo"));
		
		System.out.println("===================================================================================================================");
		System.out.println("                                             ⟦ 나의 리뷰 ⟧");
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		reviewView.printReviewList(jsonArr);
		
		if(jsonArr.length()!=0) {
	         pvo.navigator(pvo);
	      } else {
	    	  System.out.println("===================================================================================================================");
	    	 MypageView mypageView = new MypageView();
	         mypageView.startquestion(shoesClient);
	      }
		
		while(true) {
			System.out.println("| 1.페이지 이동 | 2.리뷰 상세보기 | 3. 뒤로가기 |");
			System.out.print("원하는 기능을 선택해주세요> ");
			String choice = sc.nextLine();
			switch(choice) {
				case "1":
					 reviewView.showReviewList(json, shoesClient, clientMessage);
					 break;
				 case "2":
					 reviewView.showReviewOne(shoesClient, clientMessage);
					 reviewView.reviewMenu(shoesClient, clientMessage);
					 break;
				 case "3":
					 	clientMessage.put("viewName", "back");
						shoesClient.send(clientMessage.toString());
						
						JSONObject pView = shoesClient.receive();
						shoesClient.goBack(pView);
						break;
				 default:
					 System.out.println("번호를 다시 입력해주세요!");
					 continue;
			}
		}
	}
}
