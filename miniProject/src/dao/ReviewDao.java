package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.dto.PagingVo;
import controller.dto.RReply;
import controller.dto.ReviewList;
import domain.Product;
import domain.Review;
import provider.ConnectionProvider;
import service.ProductService;

public class ReviewDao {

   // 싱글톤 생성
   private static ReviewDao reviewDao = new ReviewDao();

   private ReviewDao() {
   }

   public static ReviewDao getInstance() {
      return reviewDao;
   }

   ReplyDao replyDao = ReplyDao.getInstance();

   public int selectCount(int productId) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "select count(*) from review where product_id = ? ";
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

   // method: select문 - [마이페이지] 내가 작성한 리뷰 총개수
   public int selectMyCount(String userId) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "select count(*) from review where user_id = ? ";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      int cnt = 0;

      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, userId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
         cnt = rs.getInt(1);
      }
      pstmt.close();
      conn.close();
      return cnt;
   }

   public Review selectReview(int reviewId) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();

      String sql = "select review_id, review_title, review_content, review_date, user_id,star_score,heart_count, product_id from review where review_id = ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, reviewId);
      ResultSet rs = pstmt.executeQuery();
      List<RReply> list = replyDao.selectReviewReply(reviewId);// 댓글 리스트임
      Review r1 = null;
      while (rs.next()) {
         int pid = rs.getInt("product_id");
         Product product = ProductService.getInstance().showOneProduct(pid);
         r1 = new Review(rs.getInt("review_id"), rs.getString("review_title"), rs.getString("review_content"),
               rs.getString("review_date"), rs.getString("user_id"), rs.getInt("star_score"),
               rs.getInt("heart_count"), product, list);

      }
      pstmt.close();
      conn.close();

      return r1;

   }

   // method: select문 - [마이페이지] 내가 쓴 리뷰 목록
   public List<ReviewList> selectMyReviewList(String userId, PagingVo pvo) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      int endRn = pvo.getEndRowNo();
      int startRn = pvo.getStartRowNo();
      List<ReviewList> list = new ArrayList<>();
      String sql = "select rm, review_id, review_title, review_date, user_id, product_id  " + "from ( "
            + "select rownum as rm, review_id, review_title, review_date, user_id, product_id " + "from ("
            + "select  review_id, review_title, review_content, review_date, user_id, product_id  " + "from review "
            + "where user_id = ? " + "order by review_id" + ")" + " where rownum <= ? )" + "where rm >= ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);

      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, userId);
      pstmt.setInt(2, endRn);
      pstmt.setInt(3, startRn);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
         ReviewList rl = new ReviewList(rs.getInt("review_id"), rs.getString("review_title"),
               rs.getDate("review_date").toString(), rs.getString("user_id"), rs.getInt("product_id"));
         list.add(rl);
      }
      pstmt.close();
      conn.close();

      return list;
   }

   public int insertReview(Review review) {
	      Connection conn = ConnectionProvider.getConnection();
	      String sql = "INSERT INTO review (review_id, review_title, review_content, review_date, user_id, star_score, product_id)"
	            + "values(Review_seq.NEXTVAL,?,?,default,?,?,?)";
	      int r = 0;
	      try {
	      PreparedStatement pstmt = conn.prepareStatement(sql);
	      pstmt.setString(1, review.getReviewTitle());
	      pstmt.setString(2, review.getReviewContent());
	      pstmt.setInt(3, review.getProduct().getProductId());
	      pstmt.setString(4, review.getUserId());
	      r = pstmt.executeUpdate();
	      } catch (SQLException e) {
	         throw new RuntimeException();
	      }
	      try {
	      conn.close();
	   } catch (SQLException e) {}
	      return r;
	   }

   public int deleteReview(Review review) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "delete from review where review_id=? and user_id=?";
      // 유저아이디도 검사할 것
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, review.getReviewId());
      pstmt.setString(2, review.getUserId());

      int r = pstmt.executeUpdate();

      // void로 처리할건지 String으로 처리할건지 곰곰히 생각해보기
      pstmt.close();
      conn.close();
      return r;

   }

   public int adDeleteReview(Review review) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "delete from review where review_id=? ";
      // 유저아이디도 검사할 것
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, review.getReviewId());

      int r = pstmt.executeUpdate();

      // void로 처리할건지 String으로 처리할건지 곰곰히 생각해보기
      pstmt.close();
      conn.close();
      return r;

   }

   public List<ReviewList> selectReviewList(int productId, PagingVo pvo) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      int endRn = pvo.getEndRowNo();
      int startRn = pvo.getStartRowNo();
      String sql = "select rm, review_id, review_title, review_date, user_id, product_id  " + "from ( "
            + "select rownum as rm, review_id, review_title, review_date, user_id, product_id " + "from ("
            + "select  review_id, review_title, review_content, review_date, user_id, product_id  " + "from review "
            + "where product_id = ? " + "order by review_id" + ")" + " where rownum <= ? )" + "where rm >= ?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, productId);
      pstmt.setInt(2, endRn);
      pstmt.setInt(3, startRn);
      ResultSet rs = pstmt.executeQuery();

      List<ReviewList> list = new ArrayList<>();

      while (rs.next()) {
         ReviewList rl = new ReviewList(rs.getInt("review_id"), rs.getString("review_title"),
               rs.getString("review_date"), rs.getString("user_id"), rs.getInt("product_id"));
         list.add(rl);
      }
      pstmt.close();
      conn.close();

      return list;
   }

   public int updateReviewHeartCount(int reviewId) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "select heart_count from review where review_id=?";

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, reviewId);
      ResultSet rs = pstmt.executeQuery();
      int r = 0;
      if (rs.next()) {
         int heartCount = rs.getInt("heart_count");
         System.out.println(heartCount);
         sql = "update review set heart_count=? where review_id=?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, heartCount + 1);
         pstmt.setInt(2, reviewId);
         r = pstmt.executeUpdate();
      }
      pstmt.close();
      conn.close();
      return r;

   }

}