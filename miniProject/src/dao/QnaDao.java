package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.dto.PagingVo;
import controller.dto.QReply;
import controller.dto.QnaList;
import domain.Qna;
import provider.ConnectionProvider;

public class QnaDao {
   private static QnaDao qnaDao = new QnaDao();
   private QnaDao() {}
   public static QnaDao getInstacne() {
      return qnaDao;
   }
   ReplyDao replyDao = ReplyDao.getInstance();
   
   

   public int selectCount(int productId) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "select count(*) from qna where product_id = ? ";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, productId);
      ResultSet rs = pstmt.executeQuery();

      int cnt = 0;
      if (rs.next()) {
         cnt = rs.getInt(1);
      }
      pstmt.close();
      conn.close();
      return cnt;
   }

   public Qna selectQna(int qnaId, int productId) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();

      String sql = "select qna_id, qna_title, qna_content, qna_date, user_id from qna where qna_id = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, qnaId);
      ResultSet rs = pstmt.executeQuery();
      List<QReply> list = replyDao.selectQnaReply(qnaId);// 댓글 리스트임
      Qna q1 = null;
      while (rs.next()) {
         q1 = new Qna(rs.getInt("qna_id"), rs.getString("qna_title"), rs.getString("qna_content"),
               rs.getString("qna_date"), rs.getString("user_id"), productId, list);

      }
      pstmt.close();
      conn.close();

      return q1;

   }

   public List<QnaList> selectQnaList(int productId, PagingVo pvo) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      int endRn = pvo.getEndRowNo();
      int startRn = pvo.getStartRowNo();
      String sql = "select rm, qna_id, qna_title, qna_date, user_id, product_id  "
             + "from ( "
            + "select rownum as rm, qna_id, qna_title, qna_date, user_id, product_id "
            + "from ("
               + "select  qna_id, qna_title, qna_date, user_id, product_id  "
               + "from qna "
               + "where product_id = ? "
               + "order by qna_id"
            + ")"
            + " where rownum <= ? )"
      + "where rm >= ?";
      
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, productId);
      pstmt.setInt(2, endRn);
       pstmt.setInt(3, startRn);
      ResultSet rs = pstmt.executeQuery();
      List<QnaList> list = new ArrayList<>();
      while (rs.next()) {
         QnaList ql = new QnaList(rs.getInt("qna_id"), rs.getString("qna_title"), rs.getDate("qna_date").toString(),
               rs.getString("user_id"), rs.getInt("product_id"));
         list.add(ql);
      }
      pstmt.close();
      conn.close();

      return list;
   }

   public int insertQna(Qna qna) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "INSERT INTO qna (qna_id, qna_title ,qna_content, qna_date,product_id ,user_id)"
            + "values(QNA_SEQ.NEXTVAL,?,?,default,?,?)";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, qna.getQnaTitle());
      pstmt.setString(2, qna.getQnaContent());
      pstmt.setInt(3, qna.getProductId());
      pstmt.setString(4, qna.getUserId());
      int r = pstmt.executeUpdate();
      pstmt.close();
      conn.close();
      return r;

   }

   public int deleteQna(Qna qna) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "delete from qna where qna_id=? and user_id=?";
      // 유저아이디도 검사할 것
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, qna.getQnaId());
      pstmt.setString(2, qna.getUserId());

      int r = pstmt.executeUpdate();

      // void로 처리할건지 String으로 처리할건지 곰곰히 생각해보기
      pstmt.close();
      conn.close();
      return r;

   }
   
   public int adDeleteQna(Qna qna) throws SQLException {
         Connection conn = ConnectionProvider.getConnection();
         String sql = "delete from qna where qna_id=?";
         // 유저아이디도 검사할 것
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, qna.getQnaId());

         int r = pstmt.executeUpdate();

         // void로 처리할건지 String으로 처리할건지 곰곰히 생각해보기
         pstmt.close();
         conn.close();
         return r;

      }

}