package controller.dto;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewList {
   private int reviewId;
   private String reviewTitle;
   private String reviewDate;
   private String userId;
   private int productId;
   
   public JSONObject reviewListToJSON(ReviewList reviewList) {
	   JSONObject json = new JSONObject();
	   	json.put("reviewId", reviewList.getReviewId());
		json.put("reviewTitle", reviewList.getReviewTitle());		
		json.put("reviewDate", reviewList.getReviewDate());
		json.put("userId", reviewList.getUserId());	
		json.put("productId", reviewList.getProductId());
		
		return json;
	   
   }
   
   public ReviewList jsonToReviewList(JSONObject json) {
	   ReviewList reviewList = new ReviewList();
	 	reviewList.setReviewId(json.getInt("reviewId"));
	   	reviewList.setReviewTitle(json.getString("reviewTitle"));
		reviewList.setReviewDate(json.getString("reviewDate"));
		reviewList.setUserId(json.getString("userId"));
		reviewList.setProductId(json.getInt("productId"));		
		
		return reviewList;
	   
   }
   

}