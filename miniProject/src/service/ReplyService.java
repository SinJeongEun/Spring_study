package service;

import java.sql.SQLException;
import java.util.List;

import controller.dto.QReply;
import controller.dto.RReply;
import dao.ReplyDao;

public class ReplyService {
   ReplyDao replyDao = ReplyDao.getInstance();
   
   private static ReplyService replyService = new ReplyService();
   private ReplyService() {}
   
   public static ReplyService getInstance() {
      return replyService;
   }
   
   
   public String writeReviewReply(RReply rreply) throws SQLException {
      int r = replyDao.insertReviewReply(rreply);
      String result;
        if(r==1) {
           result = "댓글이 등록되었습니다";           
        } else {
           result = "댓글이 등록되지 않았습니다";
        }
        return result;
   }
   
   public String writeQnaReply(QReply qreply) throws SQLException {
      int r = replyDao.insertQnaReply(qreply);
      String result;
        if(r==1) {
           result = "댓글이 등록되었습니다";           
        } else {
           result = "댓글이 등록되지 않았습니다";
        }
        return result;
   }

   public List<QReply> showQnaReplys(int boardId) throws SQLException { //qna글에 해당하는 댓글 출력하기
      List<QReply> list = replyDao.selectQnaReply(boardId);
        return list;
      
   }
   
   public String deleteQnaReply(QReply qreply) throws SQLException {
      int r = replyDao.deleteQnaReply(qreply);
      String result;
        if(r==1) {
           result = "댓글이 삭제되었습니다";           
        } else {
           result = "해당 댓글 번호의 자신의 댓글이 존재하지 않습니다";
        }
        return result;
   }
   
   public String deleteReviewReply(RReply rreply) throws SQLException {
      int r = replyDao.deleteReviewReply(rreply);
      String result;
        if(r==1) {
           result = "댓글이 삭제되었습니다";           
        } else {
           result = "해당 댓글 번호의 자신의 댓글이 존재하지 않습니다";
        }
        return result;
   }
   
   
   public List<RReply> showReviewReplys(int boardId) throws SQLException { //qna글에 해당하는 댓글 출력하기
      List<RReply> list = replyDao.selectReviewReply(boardId);
        return list; //댓글 담은거 list로 출력      
   }
   
   public String adDeleteReviewReply(RReply rreply) throws SQLException {
      int r = replyDao.adDeleteReviewReply(rreply);
      String result = "해당하는 번호의 댓글이 없습니다";
      if(r==1) {
         result = "댓글이 삭제되었습니다";
      }
      return result;
   }
   
   public String adDeleteQnaReply(QReply qreply) throws SQLException {
      int r = replyDao.adDeleteQnaReply(qreply);
      String result = "해당하는 번호의 댓글이 없습니다";
      if(r==1) {
         result = "댓글이 삭제되었습니다";
      }
      return result;
   }
   

   
   

}