 package domain;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.RReply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
   private int reviewId;
   private String reviewTitle;
   private String reviewContent;
   private String reviewDate;
   private String userId;
   private int starScore;
   private int heartCount;
   private Product product;
   private List<RReply> replyList;

   
   public JSONObject reviewTOJson(Review review) {      
      JSONObject json = new JSONObject();
      JSONArray jsonarr = new JSONArray();
      
      json.put("reviewId", review.getReviewId());
      json.put("reviewTitle", review.getReviewTitle());
      json.put("reviewContent", review.getReviewContent());
      json.put("reviewDate", review.getReviewDate());
      json.put("userId", review.getUserId());
      json.put("starScore", review.getStarScore());
      json.put("heartCount", review.getHeartCount());
      Product product = review.getProduct();
      json.put("product", product.productToJson(product));
         
      for(RReply r : review.getReplyList()) {
         jsonarr.put(r.rReplyTOJson(r));
      }
      
      json.put("replyList", jsonarr);
      
      return json;
   }
   
   
   public Review jsonToReview(JSONObject json) {      
      Review review = new Review();
      Product product = new Product();
         review.setReviewId(json.getInt("reviewId"));
         review.setReviewTitle(json.getString("reviewTitle"));
      review.setReviewContent(json.getString("reviewContent"));
      review.setReviewDate(json.getString("reviewDate"));
      review.setUserId(json.getString("userId"));
      review.setStarScore(json.getInt("starScore"));
      review.setHeartCount(json.getInt("heartCount"));
      JSONObject jsonOb = json.getJSONObject("product");
      review.setProduct(product.jsonToProduct(jsonOb));      
      JSONArray jsonArr = json.getJSONArray("replyList");
      
      RReply rreply = new RReply();
      List<RReply> replyList = new ArrayList<>();
      for(int i =0; i<jsonArr.length();i++) {
         replyList.add(rreply.jsonToRReply(jsonArr.getJSONObject(i)));
      }
      review.setReplyList(replyList);
      
      return review;
   }
  
   
   

   //JSON 미리보기(미리볼 항목은 qna와 동일)
   
   
   //JSON 자세히보기

}