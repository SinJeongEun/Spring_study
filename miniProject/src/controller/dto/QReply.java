package controller.dto;

import org.json.JSONObject;

import lombok.AllArgsConstructor; 
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QReply {
	private int replyId;
	private String replyContent;
	private String replyDate;
	private String userId;
	private int qnaId;
	
	//JSON ㅎ형태의 출력함수
public JSONObject qReplyTOJson(QReply qreply) {
		
		JSONObject json = new JSONObject();
		json.put("replyId", qreply.replyId);
		json.put("replyContent", qreply.replyContent);
		json.put("replyDate", qreply.replyDate);
		json.put("userId", qreply.userId);
		json.put("qnaId", qreply.replyId);
		
		return json;
	}
	
public QReply jsonToQReply(JSONObject json) {
		QReply qreply = new QReply();
		qreply.setReplyId(json.getInt("replyId"));
		qreply.setReplyContent(json.getString("replyContent"));
		qreply.setReplyDate(json.getString("replyDate"));
		qreply.setUserId(json.getString("userId"));
		qreply.setQnaId(json.getInt("qnaId"));
		
		return qreply;
	}
	
	
}
