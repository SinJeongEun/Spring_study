package domain;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.QReply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Qna {
   private int qnaId;
   private String qnaTitle;
   private String qnaContent;
   private String qnaDate;
   private String userId;
   private int productId;
   private List<QReply> replyList;
   
   
   
   //JSON으로 게시글과 댓글 한번에 보이기
   public JSONObject qnaTOJson(Qna qna) {      
      JSONObject json = new JSONObject();
      JSONArray jsonarr = new JSONArray();
      
      json.put("reviewId", qna.getQnaId());
      json.put("reviewTitle", qna.getQnaTitle());
      json.put("reviewContent", qna.getQnaContent());
      json.put("reviewDate", qna.getQnaDate());
      json.put("userId", qna.getUserId());
      json.put("productId", qna.getProductId());
      
      for(QReply q : qna.getReplyList()) {
         jsonarr.put(q.qReplyTOJson(q));
      }
      
      json.put("replyList", jsonarr);
      
      return json;
      
   
   }
   
   
   public Qna jsonToQna(JSONObject json) {      
      Qna qna = new Qna();
         qna.setQnaId(json.getInt("reviewId"));
         qna.setQnaTitle(json.getString("reviewTitle"));
      qna.setQnaContent(json.getString("reviewContent"));
      qna.setQnaDate(json.getString("reviewDate"));
      qna.setUserId(json.getString("userId"));
      
      qna.setProductId(json.getInt("productId"));      
      JSONArray jsonArr = json.getJSONArray("replyList");
      
      QReply qreply = new QReply();
      List<QReply> replyList = new ArrayList<>();
      for(int i =0; i<jsonArr.length();i++) {
         replyList.add(qreply.jsonToQReply(jsonArr.getJSONObject(i)));
      }
      qna.setReplyList(replyList);
      
      return qna;
   }
  
   
   

   //글 목록 미리보기 (글Id, 제목, 글쓴이, 날짜)
   
}