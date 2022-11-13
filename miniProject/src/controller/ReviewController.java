package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import controller.dto.RReply;
import controller.dto.ReviewList;
import domain.Review;
import service.ProductService;
import service.ReplyService;
import service.ReviewService;
import socket.server.SocketClient;

public class ReviewController implements Controller {
   SocketClient socketClient;
   JSONObject cMessage;
   ReviewService reviewService = ReviewService.getInstance();
   ReplyService replyService = ReplyService.getInstance();

   @Override
   public void control(SocketClient socketClient, JSONObject cMessage) throws SQLException, IOException {
      this.socketClient = socketClient;
      this.cMessage = new JSONObject(cMessage);      
      String function = cMessage.getString("function");
      JSONObject result = new JSONObject();

      switch (function) {
      case "list":
         int productId = cMessage.getInt("productId");
         int pageNum = cMessage.getInt("pageNum");
         PagingVo pvo = new PagingVo(reviewService.countAllReviews(productId), pageNum);
         List<ReviewList> list = reviewService.showReviewList(productId, pvo);
         JSONObject jsonPvo = pvo.pageToJson(pvo);
         JSONArray jsonArr = new JSONArray();
         for (ReviewList reviewList : list) {
            jsonArr.put(reviewList.reviewListToJSON(reviewList));
         }
         result.put("reviewList", jsonArr);
         result.put("pvo", jsonPvo);
         socketClient.send(result.toString());
         break;
         
//      case "myList":
//            pageNum = cMessage.getInt("pageNum"); //페이지 번호
//            String userId = socketClient.getLoginId(); //로그인 아이디
//            pvo = new PagingVo(reviewService.countAllMyReviews(userId), pageNum);
//            
//            list = reviewService.showMyReviews(userId, pvo);
//            jsonPvo = pvo.pageToJson(pvo);
//            jsonArr = new JSONArray();
//            for(ReviewList reviewList : list) {   
//               jsonArr.put(reviewList.reviewListToJSON(reviewList));
//            }   
//            result.put("reviewList", jsonArr);
//            result.put("pvo",jsonPvo);
//            socketClient.send(result.toString());
//            break;
        
      case "writeReview": 
          String reviewTitle = cMessage.getString("reviewTitle");
          String reviewContent = cMessage.getString("reviewContent");
          int starCount = Integer.parseInt(cMessage.getString("starCount"));
          productId = cMessage.getInt("productId");
          String userId = socketClient.getLoginId();
          
          Review review = new Review();
          review.setReviewTitle(reviewTitle);
          review.setReviewContent(reviewContent);
          review.setUserId(userId);
          review.setStarScore(starCount);
          
          ProductService productService = ProductService.getInstance();         
          review.setProduct(productService.showOneProduct(productId));
         
          String message = "";
             try {
               message = reviewService.writeReview(review);
          } catch(RuntimeException e) {
              message = "게시물이 등록되지 않았습니다";
          }
          result.put("result", message);
          socketClient.send(result.toString());             
          break;

      case "showOne":
         int reviewId = Integer.parseInt(cMessage.getString("reviewId"));
         Review review2 = reviewService.showReviewOne(reviewId);
         result.put("result", review2.reviewTOJson(review2));
         socketClient.send(result.toString());
         break;

      case "heart":
         reviewId = Integer.parseInt(cMessage.getString("reviewId"));
         String heartOk = reviewService.heartCountUp(reviewId);
         result = new JSONObject();
         result.put("result", heartOk);
         socketClient.send(result.toString());
         break;

      case "reviewReply": // 댓글 쓰기
         String replyContent = cMessage.getString("replyContent");
         userId = socketClient.getLoginId();
         RReply rreply = new RReply();
         reviewId = Integer.parseInt(cMessage.getString("reviewId"));
         rreply.setReviewId(reviewId);
         rreply.setReplyContent(replyContent);
         rreply.setUserId(userId);
         String replyResult = replyService.writeReviewReply(rreply);
         result.put("result", replyResult);
         socketClient.send(result.toString());
         break;

      case "deleteReply":
         rreply = new RReply();
         reviewId = Integer.parseInt(cMessage.getString("reviewId"));
         int rreplyId = Integer.parseInt(cMessage.getString("rreplyId"));
         userId = socketClient.getLoginId();
         rreply.setReviewId(reviewId);
         rreply.setReplyId(rreplyId);
         rreply.setUserId(userId);
         replyResult = replyService.deleteReviewReply(rreply);

         result.put("result", replyResult);
         socketClient.send(result.toString());
         break;

      case "deleteReview":
         review = new Review();
         reviewId = Integer.parseInt(cMessage.getString("reviewId"));
         userId = socketClient.getLoginId();
         review.setReviewId(reviewId);
         review.setUserId(userId);
         String reviewResult = reviewService.deleteReview(review);

         result.put("result", reviewResult);
         socketClient.send(result.toString());
         break;

      case "adDeleteReply":
         rreply = new RReply();
         reviewId = Integer.parseInt(cMessage.getString("reviewId"));
         rreplyId = Integer.parseInt(cMessage.getString("rreplyId"));
         userId = socketClient.getLoginId();
         rreply.setReviewId(reviewId);
         rreply.setReplyId(rreplyId);
         replyResult = replyService.adDeleteReviewReply(rreply);

         result.put("result", replyResult);
         socketClient.send(result.toString());
         break;

      case "adDeleteReview":
         review = new Review();
         reviewId = Integer.parseInt(cMessage.getString("reviewId"));
         review.setReviewId(reviewId);
         reviewResult = reviewService.adDeleteReview(review);

         result.put("result", reviewResult);
         socketClient.send(result.toString());
         break;
         
         

      }

   }

}