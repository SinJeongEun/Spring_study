package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.dto.QReply;
import controller.dto.RReply;
import provider.ConnectionProvider;

public class ReplyDao {
   //싱글톤
   private static ReplyDao replyDao = new ReplyDao();
   private ReplyDao() {}
   public static ReplyDao getInstance() {
      return replyDao;
   }

   public int insertReviewReply(RReply rreply) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "INSERT INTO reply (reply_id, reply_content, user_id, reply_date, review_id)"
            + "values(REPLY_SEQ.NEXTVAL,?,?,default,?)";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, rreply.getReplyContent());
      pstmt.setString(2, rreply.getUserId());
      pstmt.setInt(3, rreply.getReviewId());
      int r = pstmt.executeUpdate();
      pstmt.close();
      conn.close();
      return r;

   }

   public int insertQnaReply(QReply qreply) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "INSERT INTO reply (reply_id, reply_content, user_id, reply_date, qna_id)"
            + "values(REPLY_SEQ.NEXTVAL,?,?,default,?)";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, qreply.getReplyContent());
      pstmt.setString(2, qreply.getUserId());
      pstmt.setInt(3, qreply.getQnaId());
      int r = pstmt.executeUpdate();
      pstmt.close();
      conn.close();
      return r;

   }

   public List<RReply> selectReviewReply(int reviewId) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "select reply_id, reply_content,reply_date, user_id, review_id from Reply where review_id = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, reviewId);
      // 데이터를 가져와 읽을 것이므로 executeQuery
      ResultSet rs = pstmt.executeQuery();
      List<RReply> list = new ArrayList<>();
      // list 에 reply 객체를 담아야됨
      while (rs.next()) {
         RReply qr1 = new RReply(rs.getInt("reply_id"), rs.getString("reply_content"), rs.getString("reply_date"),
               rs.getString("user_id"), rs.getInt("review_id"));
         list.add(qr1);// 댓글 n행의 데이터를 1행씩 리스트에 담은거임
      }
      pstmt.close();
      conn.close();

      return list;
   }

   public List<QReply> selectQnaReply(int qnaId) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "select reply_id, reply_content, reply_date, user_id, qna_id from Reply where qna_id = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, qnaId);
      // 데이터를 가져와 읽을 것이므로 executeQuery
      ResultSet rs = pstmt.executeQuery();
      List<QReply> list = new ArrayList<>();
      // list 에 reply 객체를 담아야됨
      while (rs.next()) {
         QReply qr1 = new QReply(rs.getInt("reply_id"), rs.getString("reply_content"), rs.getString("reply_date"),
               rs.getString("user_id"), rs.getInt("qna_id"));
         list.add(qr1);// 댓글 n행의 데이터를 1행씩 리스트에 담은거임
      }
      pstmt.close();
      conn.close();

      return list;
   }

   public int deleteReviewReply(RReply rreply) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "delete from reply where review_id=? and reply_id=? and user_id=?";
      // 유저아이디도 검사할 것
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, rreply.getReviewId());
      pstmt.setInt(2, rreply.getReplyId());
      pstmt.setString(3, rreply.getUserId());
      int r = pstmt.executeUpdate();
      pstmt.close();
      conn.close();
      return r;

   }

   public int deleteQnaReply(QReply qreply) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "delete from reply where qna_id=? and reply_id=? and user_id=?";
      // 유저아이디도 검사할 것
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, qreply.getQnaId());
      pstmt.setInt(2, qreply.getReplyId());
      pstmt.setString(3, qreply.getUserId());
      int r = pstmt.executeUpdate();

      pstmt.close();
      conn.close();
      return r;

   }
   
   //관리자가 모든 댓글을 지울 수 있는 메소드
   public int adDeleteReviewReply(RReply rreply) throws SQLException {
         Connection conn = ConnectionProvider.getConnection();
         String sql = "delete from reply where review_id=? and reply_id=? ";
         // 유저아이디도 검사할 것
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, rreply.getReviewId());
         pstmt.setInt(2, rreply.getReplyId());
         
         int r = pstmt.executeUpdate();
         pstmt.close();
         conn.close();
         return r;

      }
   
   public int adDeleteQnaReply(QReply qreply) throws SQLException {
         Connection conn = ConnectionProvider.getConnection();
         String sql = "delete from reply where review_id=? and reply_id=? ";
         // 유저아이디도 검사할 것
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, qreply.getQnaId());
         pstmt.setInt(2, qreply.getReplyId());
         
         int r = pstmt.executeUpdate();
         pstmt.close();
         conn.close();
         return r;

      }


}