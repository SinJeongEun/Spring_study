
package view.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import socket.client.ShoesClient;
import view.user.QnaView;

public class AdQnaView {
   ShoesClient shoesClient;
   Scanner sc = new Scanner(System.in);   
   PagingVo pvo = new PagingVo();
   JSONObject clientMessage = new JSONObject();
   JSONObject jsonOb;
   JSONArray page;
   JSONArray jsonArr;
   QnaView qnaView = new QnaView();
   
   public void startquestion(ShoesClient shoesClient, int productId) throws IOException, SQLException {
      this.shoesClient = shoesClient;
      clientMessage.put("viewName","AdQnaView");
      clientMessage.put("productId", productId);
      System.out.println("==<<  질문 게시판  >>==");
      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");      
      
      clientMessage.put("function", "list");
      clientMessage.put("pageNum",1);
      shoesClient.send(clientMessage.toString());
      jsonOb = shoesClient.receive();
      jsonArr = jsonOb.getJSONArray("qnaList");
      pvo = pvo.jsonToPage(jsonOb.getJSONObject("pvo"));
      qnaView.printQnaList(jsonArr);
      pvo.navigator(pvo);
      
      while(true) {
         System.out.println("원하시는 메뉴를 선택하여 주세요.");
         System.out.println("1. 페이지 넘기기 2. 게시글 상세보기 3. 뒤로가기 ");
         System.out.print("선택 >");
         String num = sc.nextLine();
         if(num.equals("1")) {   
            qnaView.showQnaList(jsonOb,shoesClient,clientMessage);
            continue;
         } else if(num.equals("2")) {
            //showReviewOne 메소드는 해당 게시글의 글번호만 입력받으므로 동일한 메소드를 사용함
            qnaView.showQnaOne(shoesClient,clientMessage);
            break;
         } else if(num.equals("3")) {
        	 AdProductView adProductView = new AdProductView();
				adProductView.startquestion(shoesClient);
			break;
         }
      }
      this.qnaMenu(shoesClient, clientMessage);      
      startquestion(shoesClient, productId);   
   }
   
   public void qnaMenu(ShoesClient shoesClient,JSONObject clientMessage) throws IOException, SQLException {
      System.out.println("원하시는 메뉴를 선택하여 주세요.");
      System.out.println("1. 댓글 달기 2. 댓글 삭제하기 3. 게시물 삭제하기 4. 뒤로가기 ");
      System.out.print("선택 >");
      String num = sc.nextLine();
      System.out.println("-----------------------------------------------------");

      switch (num) {
      case "1":
         qnaView.writeQnaReply(shoesClient,clientMessage);
         break;

      case "2":
         adDeleteQnaReply();
         break;

      case "3":
         System.out.println("관리자가 게시글 삭제하기");
         adDeleteQna();
         break;
         
      case "4":
    	  AdProductView adProductView = new AdProductView();
			adProductView.startquestion(shoesClient);
			break;
      }
   }
   
   //관리자 메뉴에서는 모든 게시글과 댓글 삭제가 가능하다.
   //따라서 delete의 기능일 경우에 관리자는 모든 댓글과 게시글을 삭제할 수 있으므로 기존 메소드를 활용하여 변경하였다.
   public void adDeleteQnaReply() throws IOException {
      clientMessage.put("function", "adDeleteReply");
      System.out.println("삭제할 댓글 번호를 입력해주세요.");
      System.out.print("선택 >");
      String rreplyId = sc.nextLine();
      clientMessage.put("rreplyId", rreplyId);
      shoesClient.send(clientMessage.toString());
      JSONObject result = shoesClient.receive();
      System.out.println(result.getString("result"));
   }
   
   public void adDeleteQna() throws IOException {
      System.out.println("adDeleteQna 메소드 실행");
      clientMessage.put("function", "adDeleteQna");
      shoesClient.send(clientMessage.toString());
      JSONObject result = shoesClient.receive();
      System.out.println(result.getString("result"));
      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
   }

}