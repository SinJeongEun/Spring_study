package view.user;

import java.io.IOException;
import java.net.UnknownServiceException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import controller.dto.PagingVo;
import controller.dto.RReply;
import controller.dto.ReviewList;
import domain.Review;
import socket.client.ShoesClient;

public class ReviewView {
   ShoesClient shoesClient;
   JSONObject clientMessage = new JSONObject();
   Scanner sc = new Scanner(System.in);
   PagingVo pvo = new PagingVo();
   JSONObject jsonOb;
   JSONArray jsonArr;
   JSONObject page;
   
   
   public void startquestion(ShoesClient shoesClient, int productId) throws IOException, SQLException {
      this.shoesClient = shoesClient;
      clientMessage.put("viewName", "ReviewView");
      clientMessage.put("id", productId);
      clientMessage.put("productId", productId);
      System.out.println("==<<  리뷰 게시판  >>==");
      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");      
      
      clientMessage.put("function", "list");
      clientMessage.put("pageNum", 1);
      shoesClient.send(clientMessage.toString());
      JSONObject jsonOb = shoesClient.receive();
      JSONArray jsonArr = jsonOb.getJSONArray("reviewList");
      pvo = pvo.jsonToPage(jsonOb.getJSONObject("pvo"));
      printReviewList(jsonArr);
      if(jsonArr.length()!=0) {
            pvo.navigator(pvo);
         } else {
            System.out.println("===================================================================================================================");
            ProductView productView = new ProductView();
			productView.startquestion(shoesClient, 1);
         }
   
      
      
      //목록 보기에서의 메뉴
      while(true) {
         System.out.println("원하시는 메뉴를 선택하여 주세요.");
         System.out.println("1. 페이지 넘기기 2. 게시글 상세보기 3. 뒤로가기 ");
         System.out.print("선택 >");
         String num = sc.nextLine();
         if(num.equals("1")) {
            showReviewList(jsonOb,shoesClient,clientMessage);
            continue;
         } else if(num.equals("2")) {
            showReviewOne(shoesClient,clientMessage);
            break;
         }else if(num.equals("3")) {
        	clientMessage.put("viewName", "back");
			shoesClient.send(clientMessage.toString());
			
			JSONObject pView = shoesClient.receive();
			shoesClient.goBack(pView);
			break;
          }
      }
      
      reviewMenu(shoesClient,clientMessage);
      startquestion(shoesClient, productId);
   }
   
   public void reviewMenu(ShoesClient shoesClient,JSONObject clientMessage) throws IOException, SQLException {
         System.out.println("원하시는 메뉴를 선택하여 주세요.");
         System.out.println("1. 댓글 달기 2. 좋아요 누르기 3. 댓글 삭제하기 4. 게시글 삭제하기 5. 홈으로 돌아가기 " );
         System.out.print("선택 >");
         String num = sc.nextLine();
         System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

         switch (num) {
         case "1":
            writeReviewReply(shoesClient, clientMessage);
            break;

         case "2":
            clickHeart(shoesClient, clientMessage);
            break;

         case "3":
            deleteReviewReply(shoesClient, clientMessage);
            break;

         case "4":
            deleteReview(shoesClient, clientMessage);
            break;
            
         case "5":
				UserView userView = new UserView();
				userView.startquestion(shoesClient);
				break;
         }
         System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
      }
   
   public void writeReivew(ShoesClient shoesClient,JSONObject clientMessage) throws IOException {
	   clientMessage.put("viewName", "ReviewView");
	   clientMessage.put("function", "writeReview");
	   clientMessage.put("id", clientMessage.getInt("productId"));
       
       System.out.print("게시글 제목>");
       String reviewTitle = sc.nextLine();
       
       System.out.println("게시글 내용>");
       String reviewContent = sc.nextLine();
       
       System.out.print("별점을 입력하세요>");
       String starCount = sc.nextLine();
       
       clientMessage.put("reviewTitle", reviewTitle);
       clientMessage.put("reviewContent", reviewContent);
       clientMessage.put("starCount", starCount);
       shoesClient.send(clientMessage.toString());
       
       JSONObject result = shoesClient.receive();
       System.out.println(result.getString("result"));
    }
   
   public void writeReviewReply(ShoesClient shoesClient,JSONObject clientMessage) throws IOException {
      System.out.print("댓글 내용을 입력해주세요.>");
      String replyContent = sc.nextLine();
      clientMessage.put("replyContent", replyContent);
      clientMessage.put("function", "reviewReply");
      shoesClient.send(clientMessage.toString());
      JSONObject result = shoesClient.receive();
      System.out.println(result.getString("result"));

   }

   public void clickHeart(ShoesClient shoesClient,JSONObject clientMessage) throws IOException {
      clientMessage.put("function", "heart");
      shoesClient.send(clientMessage.toString());
      JSONObject result = shoesClient.receive();
      System.out.println(result.getString("result"));
   }

   public void deleteReviewReply(ShoesClient shoesClient,JSONObject clientMessage) throws IOException {
      clientMessage.put("function", "deleteReply");
      System.out.print("삭제할 댓글 번호를 입력해주세요.>");
      String rreplyId = sc.nextLine();
      clientMessage.put("rreplyId", rreplyId);
      shoesClient.send(clientMessage.toString());
      JSONObject result = shoesClient.receive();
      System.out.println(result.getString("result"));
   }

   public void deleteReview(ShoesClient shoesClient,JSONObject clientMessage) throws IOException {
      clientMessage.put("function", "deleteReview");
      shoesClient.send(clientMessage.toString());
      JSONObject result = shoesClient.receive();
      System.out.println(result.getString("result"));
      
   }

   public void showReviewList(JSONObject jsonOb, ShoesClient shoesClient,JSONObject clientMessage) throws IOException, JSONException {
      pvo = pvo.jsonToPage(jsonOb.getJSONObject("pvo"));
      System.out.print("원하시는 페이지 번호 혹은 글자를 입력헤주세요.>");
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
      jsonArr = jsonOb.getJSONArray("reviewList");
      pvo = pvo.jsonToPage(jsonOb.getJSONObject("pvo"));
      System.out.println("==<<  리뷰 게시판  >>==");
      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
      printReviewList(jsonArr);
      pvo.navigator(pvo);
   }

   public void showReviewOne(ShoesClient shoesClient,JSONObject clientMessage) throws IOException {
      System.out.print("보고싶은 리뷰 게시글 번호를 입력하세요.");
      String reviewId = sc.nextLine();
      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
      clientMessage.put("function", "showOne");
      clientMessage.put("reviewId", reviewId);
      shoesClient.send(clientMessage.toString());
      JSONObject json = shoesClient.receive();
      printReviewOne(json);

   }

   public void printReviewList(JSONArray jsonArr) {
         ReviewList reviewList = new ReviewList();
         if(jsonArr.length()!=0) {
            for (int i = 0; i < jsonArr.length(); i++) {
               reviewList = reviewList.jsonToReviewList(jsonArr.getJSONObject(i));
               System.out.println("no:" + reviewList.getReviewId() + "\t\t제목:" + reviewList.getReviewTitle()
               + "\t\t작성자:" + reviewList.getUserId() + "\t\t" + reviewList.getReviewDate());
               System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
            }
         } else {
            System.out.println("해당 상품에 대해 작성된 리뷰가 없습니다.");
         }
      }

   public void printReviewOne(JSONObject json) {
      // JSONObject로 파싱하기
      Review review = new Review();
      review = review.jsonToReview(json.getJSONObject("result"));
      System.out.println("<<리뷰>>");

      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
      System.out.println("no:" + review.getReviewId() + "\t\t제목:" + review.getReviewTitle() + "\t\t작성자:"
            + review.getUserId() + "\t\t" + review.getReviewDate());
      System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
      System.out.println("브랜드:"+ review.getProduct().getCompany()+"\t\t상품 이름:"+ review.getProduct().getProductName()+"\t\t가격:"+review.getProduct().getProductPrice());
      System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ\n");
      String star = "";
      for (int i = 0; i < review.getStarScore(); i++) {
         star += "★";
      }
      System.out.println(review.getReviewContent());
      System.out.println();
      System.out.println("♥: " + review.getHeartCount()+"\t\t별점: " + star);
      
      if (review.getReplyList().size() == 0) {
         System.out.println(
               "----------------------------------------------------------------------------------------------------------------------");
         System.out.println("작성된 댓글이 없습니다.");
         System.out.println(
               "----------------------------------------------------------------------------------------------------------------------");
      } else {
         System.out.println(
               "----------------------------------------------------------------------------------------------------------------------");
         System.out.println("<<댓글>>" + "\t\t댓글 수: " + review.getReplyList().size());
         System.out.println(
               "----------------------------------------------------------------------------------------------------------------------");
         for (RReply rreply : review.getReplyList()) {
            System.out.println("no:" + rreply.getReplyId() + "\t\t" + rreply.getReplyDate());
            System.out.println("작성자:" + rreply.getUserId());
            System.out.println(rreply.getReplyContent());
            System.out.println(
                  "----------------------------------------------------------------------------------------------------------------------");
         }

      }

   }

}