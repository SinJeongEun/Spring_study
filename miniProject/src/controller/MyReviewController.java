package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import controller.dto.ReviewList;
import domain.Review;
import service.ReplyService;
import service.ReviewService;
import socket.server.SocketClient;

public class MyReviewController implements Controller {
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
      case "myList":
    	    int pageNum = cMessage.getInt("pageNum"); //페이지 번호
    	    String userId = socketClient.getLoginId(); //로그인 아이디
    	    
    	    PagingVo pvo = new PagingVo(reviewService.countAllMyReviews(userId), pageNum);
    	    
    	    List<ReviewList> list = reviewService.showMyReviews(userId, pvo);
    	    JSONObject jsonPvo = pvo.pageToJson(pvo);
    	    JSONArray jsonArr = new JSONArray();
    	    
    	    for(ReviewList reviewList : list) {   
    	       jsonArr.put(reviewList.reviewListToJSON(reviewList));
    	    }   
    	    
    	    result.put("reviewList", jsonArr);
    	    result.put("pvo",jsonPvo);
    	    
    	    socketClient.send(result.toString());
    	    break;
    	    
      case "showOne":
    	  int reviewId = Integer.parseInt(cMessage.getString("reviewId"));
          Review review = reviewService.showReviewOne(reviewId);
          result.put("result", review.reviewTOJson(review));
          socketClient.send(result.toString());
          break;
	    }
   }
}
