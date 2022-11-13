package view.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import socket.client.ShoesClient;
import view.user.ReviewView;

public class AdReviewView {
	ShoesClient shoesClient;
	Scanner sc = new Scanner(System.in);
	PagingVo pvo = new PagingVo();
	JSONObject clientMessage = new JSONObject();
	JSONObject jsonOb;
	JSONArray page;
	JSONArray jsonArr;
	ReviewView reviewView = new ReviewView();

	public void startquestion(ShoesClient shoesClient, int productId) throws IOException, SQLException {
		this.shoesClient = shoesClient;
		clientMessage.put("viewName", "AdReviewView");
		clientMessage.put("productId", productId);
		System.out.println("==<<  리뷰 게시판  >>==");
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

		clientMessage.put("function", "list");
		clientMessage.put("pageNum", 1);
		shoesClient.send(clientMessage.toString());
		jsonOb = shoesClient.receive();
		jsonArr = jsonOb.getJSONArray("reviewList");
		pvo = pvo.jsonToPage(jsonOb.getJSONObject("pvo"));
		reviewView.printReviewList(jsonArr);
		pvo.navigator(pvo);

		while (true) {
			System.out.println("원하시는 메뉴를 선택하여 주세요.");
			System.out.println("1. 페이지 넘기기 2. 게시글 상세보기 3. 뒤로가기");
			System.out.print("선택 >");
			String num = sc.nextLine();
			if (num.equals("1")) {
				reviewView.showReviewList(jsonOb, shoesClient, clientMessage);
				continue;
			} else if (num.equals("2")) {
				// showReviewOne 메소드는 해당 게시글의 글번호만 입력받으므로 동일한 메소드를 사용함
				reviewView.showReviewOne(shoesClient, clientMessage);

				break;
			} else {
				AdProductView adProductView = new AdProductView();
				adProductView.startquestion(shoesClient);
			}
		}
		System.out.println("원하시는 메뉴를 선택하여 주세요.");
		System.out.println("1. 댓글 달기 2. 댓글 삭제하기 3. 게시글 삭제하기 4. 뒤로가기 ");
		System.out.print("선택 >");
		String num = sc.nextLine();
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

		switch (num) {
		case "1":
			reviewView.writeReviewReply(shoesClient, clientMessage);
			break;

		case "2":
			deleteReviewReply();
			break;

		case "3":
			deleteReview();
			break;

		case "4":
			AdProductView adProductView = new AdProductView();
			adProductView.startquestion(shoesClient);
			break;

		}
		startquestion(shoesClient, productId);
	}

	// 관리자 메뉴에서는 모든 게시글과 댓글 삭제가 가능하다.
	// 따라서 delete의 기능일 경우에 관리자는 모든 댓글과 게시글을 삭제할 수 있으므로 기존 메소드를 활용하여 변경하였다.
	public void deleteReviewReply() throws IOException {
		clientMessage.put("function", "adDeleteReply");
		System.out.println("삭제할 댓글 번호를 입력해주세요.");
		System.out.print("선택 >");
		String rreplyId = sc.nextLine();
		clientMessage.put("rreplyId", rreplyId);
		shoesClient.send(clientMessage.toString());
		JSONObject result = shoesClient.receive();
		System.out.println(result.getString("result"));
	}

	public void deleteReview() throws IOException {
		clientMessage.put("function", "adDeleteReview");
		shoesClient.send(clientMessage.toString());
		JSONObject result = shoesClient.receive();
		System.out.println(result.getString("result"));
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
	}

}