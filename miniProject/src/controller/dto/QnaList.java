package controller.dto;


import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaList {
	 private int qnaId;
	 private String qnaTitle;
	 private String qnaDate;
	 private String userId;
	 private int productId;
	 
	 public JSONObject qnaListToJSON(QnaList qnaList) {
		   JSONObject json = new JSONObject();
		   	json.put("qnaId", qnaList.getQnaId());
			json.put("qnaTitle", qnaList.getQnaTitle());		
			json.put("qnaDate", qnaList.getQnaDate());
			json.put("userId", qnaList.getUserId());	
			json.put("productId", qnaList.getProductId());
			
			return json;
		   
	   }
	 
	 public QnaList jsonToQnaList(JSONObject json) {
		   QnaList qnaList = new QnaList();
		 	qnaList.setQnaId(json.getInt("qnaId"));
		   	qnaList.setQnaTitle(json.getString("qnaTitle"));
			qnaList.setQnaDate(json.getString("qnaDate"));
			qnaList.setUserId(json.getString("userId"));
			qnaList.setProductId(json.getInt("productId"));		
			
			return qnaList;
		   
	   }
	 
	 
}
