package controller.dto;
 
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RReply {
	private int replyId;
	private String replyContent;
	private String replyDate;
	private String userId;
	private int reviewId;
	
	//JSON ㅎ형태의 출력함수
	public JSONObject rReplyTOJson(RReply rreply) {
		
		JSONObject json = new JSONObject();
		json.put("replyId", rreply.replyId);
		json.put("replyContent", rreply.replyContent);
		json.put("replyDate", rreply.replyDate);
		json.put("userId", rreply.userId);
		json.put("reveiwId", rreply.reviewId);
		
		return json;
	}
	
public RReply jsonToRReply(JSONObject json) {
		RReply rreply = new RReply();
		rreply.setReplyId(json.getInt("replyId"));
		rreply.setReplyContent(json.getString("replyContent"));
		rreply.setReplyDate(json.getString("replyDate"));
		rreply.setUserId(json.getString("userId"));
		rreply.setReviewId(json.getInt("reveiwId"));
		
		return rreply;
	}
	
	
}
