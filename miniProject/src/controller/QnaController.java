package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import controller.dto.QReply;
import controller.dto.QnaList;
import domain.Qna;
import service.QnAService;
import service.ReplyService;
import socket.server.SocketClient;

public class QnaController implements Controller {
   SocketClient socketClient;
   JSONObject cMessage;
   QnAService qnaService = QnAService.getInstacne();
   ReplyService replyService = ReplyService.getInstance(); 
   @Override
   public void control(SocketClient socketClient, JSONObject cMessage) throws SQLException, IOException {
      this.socketClient= socketClient;
      this.cMessage = new JSONObject(cMessage);
      String function = cMessage.getString("function");
      int productId = cMessage.getInt("productId");
      JSONObject result = new JSONObject();
      
      switch(function) {
         case "list":
            int pageNum = cMessage.getInt("pageNum");
            PagingVo pvo = new PagingVo(qnaService.countAllQnas(productId), pageNum);
            List<QnaList> list = qnaService.showQnaList(productId, pvo); //현재 page도 뷰를 통해 입력받았으므로 showQnaList 메소드에 pager 객체도 추가
            JSONObject jsonPvo = pvo.pageToJson(pvo);
            JSONArray jsonArr = new JSONArray();
            for(QnaList qnaList : list) {
               jsonArr.put(qnaList.qnaListToJSON(qnaList));
            }
            result.put("qnaList", jsonArr);
            result.put("pvo", jsonPvo);
            socketClient.send(result.toString());
            break;
            
         case "showOne" :
            int qnaId = Integer.parseInt(cMessage.getString("qnaId"));
             Qna qna = qnaService.showQnaOne(qnaId, productId);             
             result.put("qna", qna.qnaTOJson(qna));
             socketClient.send(result.toString());
            break;
            
         case "qnaReply": //댓글 쓰기
            qnaId = Integer.parseInt(cMessage.getString("qnaId"));
             String replyContent = cMessage.getString("replyContent");
             String userId = socketClient.getLoginId();
             QReply qreply = new QReply();
             qreply.setQnaId(qnaId);
             qreply.setReplyContent(replyContent);
             qreply.setUserId(userId);
            String replyResult = replyService.writeQnaReply(qreply);
            result.put("result", replyResult);
            socketClient.send(result.toString());
            break;
            
         case "writeQna": //qna게시글 작성하기
            String qnaTitle = cMessage.getString("qnaTitle");
            String qnaContent = cMessage.getString("qnaContent");
             userId = socketClient.getLoginId();
             qna = new Qna();
             qna.setQnaTitle(qnaTitle);
             qna.setQnaContent(qnaContent);
             qna.setUserId(userId);
             qna.setProductId(productId);
             qnaContent = qnaService.writeQna(qna);
            result.put("result", qnaContent);
            socketClient.send(result.toString());             
            break;
            
         case "deleteReply": //댓글 지우기
            qreply = new QReply();
            qnaId = Integer.parseInt(cMessage.getString("qnaId"));
            int qreplyId = Integer.parseInt(cMessage.getString("qreplyId"));
            userId = socketClient.getLoginId();
            qreply.setQnaId(qnaId);
             qreply.setReplyId(qreplyId);
             qreply.setUserId(userId);         
            String deleteResult = replyService.deleteQnaReply(qreply);
            result.put("result", deleteResult);
            socketClient.send(result.toString());
            break;
            
         case "deleteQna":
            qna = new Qna();
            qnaId = Integer.parseInt(cMessage.getString("qnaId"));
            userId = socketClient.getLoginId();
            deleteResult = qnaService.deleteQna(qna);
            result.put("result", deleteResult);
            socketClient.send(result.toString());
            break;
            
         case "adDeleteReply": //댓글 지우기
             qreply = new QReply();
             qnaId = Integer.parseInt(cMessage.getString("qnaId"));
             qreplyId = Integer.parseInt(cMessage.getString("qreplyId"));
             qreply.setQnaId(qnaId);
             qreply.setReplyId(qreplyId);                  
             deleteResult = replyService.adDeleteQnaReply(qreply);
             result.put("result", deleteResult);
             socketClient.send(result.toString());
             break;
             
          case "adDeleteQna":
             System.out.println("adDeleteQna");
             qna = new Qna();
             qnaId = Integer.parseInt(cMessage.getString("qnaId"));
             qna.setQnaId(qnaId);
             deleteResult = qnaService.adDeleteQna(qna);
             result.put("result", deleteResult);
             socketClient.send(result.toString());
             break;
         
      }
      
   }

}