package view.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import controller.dto.QReply;
import controller.dto.QnaList;
import domain.Qna;
import socket.client.ShoesClient;

public class QnaView {
   ShoesClient shoesClient;
   JSONObject clientMessage = new JSONObject();
   Scanner sc = new Scanner(System.in);
   PagingVo pvo = new PagingVo();
   JSONObject jsonOb;
   JSONArray page;
   JSONArray jsonArr;

   public void startquestion(ShoesClient shoesClient, int productId) throws IOException, SQLException {
      this.shoesClient = shoesClient;
      clientMessage.put("viewName", "QnaView");
      clientMessage.put("id", productId);
      clientMessage.put("productId", productId);
      System.out.println("==<<  질문 게시판  >>==");
      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");      
      
      clientMessage.put("function", "list");
      clientMessage.put("pageNum", 1);
      shoesClient.send(clientMessage.toString());
      JSONObject jsonOb = shoesClient.receive();
      JSONArray jsonArr = jsonOb.getJSONArray("qnaList");
      pvo = pvo.jsonToPage(jsonOb.getJSONObject("pvo"));
      printQnaList(jsonArr);
      pvo.navigator(pvo);
      
      while (true) {         
         System.out.println("1. 질문 게시글 작성하기 2. 게시글 상세 보기 3.페이지 넘기기 4. 뒤로가기 ");
         System.out.print("선택 >");
         String num = sc.nextLine();
         if (num.equals("1")) {
            writeQna(shoesClient,clientMessage);
            continue;
            
         } else if (num.equals("2")) {
            showQnaOne(shoesClient,clientMessage);
            break;
            
         } else if(num.equals("3")) {
            showQnaList(jsonOb,shoesClient,clientMessage);
            continue;
            
         } else if(num.equals("4")) {
			clientMessage.put("viewName", "back");
			shoesClient.send(clientMessage.toString());
			
			JSONObject pView = shoesClient.receive();
			shoesClient.goBack(pView);
			break;
         }
      }
      
      qnaMenu(shoesClient,clientMessage);
      startquestion(shoesClient, productId);
      
   }
   
   
   public void qnaMenu(ShoesClient shoesClient,JSONObject clientMessage) throws IOException, SQLException {
      System.out.println("원하시는 메뉴를 선택하여 주세요.");
      System.out.println("1. 댓글 달기 2. 댓글 삭제하기 3. 게시물 삭제하기");
      System.out.print("선택 >");
      String num = sc.nextLine();
      System.out.println("-----------------------------------------------------");

      switch (num) {
      case "1":
         writeQnaReply(shoesClient,clientMessage);
         break;

      case "2":
         deleteQnaReply();
         break;

      case "3":
         deleteQna();
         break;
      }
   }

   public void writeQnaReply(ShoesClient shoesClient,JSONObject clientMessage) throws IOException {
      System.out.println("댓글 내용을 입력해주세요.");
      System.out.print("입력 >");
      String replyContent = sc.nextLine();
      clientMessage.put("replyContent", replyContent);
      clientMessage.put("function", "qnaReply");
      shoesClient.send(clientMessage.toString());
      JSONObject result = shoesClient.receive();
      System.out.println(result.getString("result"));
   }

   public void deleteQnaReply() throws IOException {
      clientMessage.put("function", "deleteReply");
      System.out.print("삭제할 댓글 번호를 입력해주세요. > ");
      String qreplyId = sc.nextLine();
      clientMessage.put("qreplyId", qreplyId);
      shoesClient.send(clientMessage.toString());
      JSONObject result = shoesClient.receive();
      System.out.println(result.getString("result"));
   }

   public void deleteQna() throws IOException {
      clientMessage.put("function", "deleteQna");
      shoesClient.send(clientMessage.toString());
      JSONObject result = shoesClient.receive();
      System.out.println(result.getString("result"));
   }

   public void writeQna(ShoesClient shoesClient,JSONObject clientMessage) throws IOException {
      clientMessage.put("function", "writeQna");
      System.out.print("게시글 제목>");
      String qnaTitle = sc.nextLine();
      System.out.println("게시글 내용>");
      String qnaContent = sc.nextLine();
      clientMessage.put("qnaTitle", qnaTitle);
      clientMessage.put("qnaContent", qnaContent);
      shoesClient.send(clientMessage.toString());
      JSONObject result = shoesClient.receive();
      System.out.println(result.getString("result"));
   }

   public void showQnaOne(ShoesClient shoesClient,JSONObject clientMessage) throws IOException {
      System.out.println("보고싶은 질문 게시글 번호를 입력하세요.");
      System.out.print("선택 >");
      String qnaId = sc.nextLine();
      System.out.println("-----------------------------------------------------");
      clientMessage.put("function", "showOne");
      clientMessage.put("qnaId", qnaId);
      shoesClient.send(clientMessage.toString());
      JSONObject qna = shoesClient.receive();
      printQnaOne(qna);
   }

   public void showQnaList(JSONObject jsonOb, ShoesClient shoesClient,JSONObject clientMessage) throws IOException {
      pvo = pvo.jsonToPage(jsonOb.getJSONObject("pvo"));
      System.out.println("원하시는 페이지 번호 혹은 글자를 입력해주세요.");
      System.out.print("입력 >");
      String num = sc.nextLine();
      int pageNum = 0;
      if (num.equals("맨앞")) {
         pageNum = 1;
      } else if (num.equals("이전")) {
         pageNum = pvo.getStartPageNo() - 1;
      } else if (num.equals("다음")) {
         pageNum = pvo.getEndPageNo() + 1;
      } else if (num.equals("맨뒤")) {
         pageNum = pvo.getTotalPageNo();
      } else {
         pageNum = Integer.parseInt(num);
      }
      
      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
      clientMessage.put("pageNum", pageNum);
      shoesClient.send(clientMessage.toString());
      jsonOb = shoesClient.receive();
      jsonArr = jsonOb.getJSONArray("qnaList");
      System.out.println("==<<  질문 게시판  >>==");
      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
      printQnaList(jsonArr);
      pvo.navigator(pvo);
   }
   
   public void printQnaList(JSONArray jsonArr) {
      QnaList qnaList = new QnaList();
      for (int i = 0; i < jsonArr.length(); i++) {
         qnaList = qnaList.jsonToQnaList(jsonArr.getJSONObject(i));
         if (qnaList == null) {
            System.out.println("해당 상품에 대해 작성된 게시물이 없습니다."); //이러고 뒤로가기 하고싶은데 . . .
            
         } else {
            System.out.println("no:" + qnaList.getQnaId() + "\t\t제목:" + qnaList.getQnaTitle()
                  + "\t\t작성자:" + qnaList.getUserId() + "\t\t" + qnaList.getQnaDate());
            System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
         }
      }

   }
   
   public void printQnaOne(JSONObject json) {
      // JSONObject로 파싱하기
      Qna qna = new Qna();
      qna = qna.jsonToQna(json.getJSONObject("qna"));
      System.out.println("<<QnA>>");

      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
      System.out.println("no:" + qna.getQnaId() + "\t\t제목:" + qna.getQnaTitle() + "\t\t작성자:"
            + qna.getUserId() + "\t\t" + qna.getQnaDate());
      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
      System.out.println("<내용>");
      System.out.println();
      System.out.println(qna.getQnaContent());
      if (qna.getReplyList().size() == 0) {
         System.out.println(
               "----------------------------------------------------------------------------------------------------------------------");
         System.out.println("작성된 댓글이 없습니다.");
         System.out.println(
               "----------------------------------------------------------------------------------------------------------------------");
      } else {
         System.out.println(
               "----------------------------------------------------------------------------------------------------------------------");
         System.out.println("<<댓글>>" + "\t\t댓글 수: " + qna.getReplyList().size());
         System.out.println(
               "----------------------------------------------------------------------------------------------------------------------");
         for (QReply qreply : qna.getReplyList()) {
            System.out.println("no:" + qreply.getReplyId() + "\t\t" + qreply.getReplyDate());
            System.out.println("작성자:" + qreply.getUserId());
            System.out.println(qreply.getReplyContent());
            System.out.println(
                  "----------------------------------------------------------------------------------------------------------------------");
         }

      }

   }
   
   

}