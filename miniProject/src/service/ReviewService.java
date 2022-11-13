package service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import controller.dto.PagingVo;
import controller.dto.ReviewList;
import dao.ReviewDao;
import domain.Review;

public class ReviewService {
   private static ReviewService reviewService = new ReviewService();
   private ReviewService() {}
   public static ReviewService getInstance() {
      return reviewService;
   }
   
   ReviewDao reviewDao = ReviewDao.getInstance();
   

   public List<ReviewList> showReviewList(int productId,PagingVo pvo) throws SQLException {
      List<ReviewList> list = reviewDao.selectReviewList(productId, pvo);
      return list;
   }
   
   //method: [마이페이지] - 나의 리뷰 목록 (간단히 보기)
   public List<ReviewList> showMyReviews(String userId, PagingVo pvo) throws SQLException {
      List<ReviewList> list = reviewDao.selectMyReviewList(userId, pvo);
      return list;
   }

   // 2. 리뷰 자세히 보기 showReviewOne(원하는 리뷰 번호)
   public Review showReviewOne(int reviewId) throws SQLException {
      Review review = reviewDao.selectReview(reviewId);
      
      return review;
   }
   
   public int countAllReviews(int productId) throws SQLException {
      return reviewDao.selectCount(productId);
   }
   
 //method: [마이페이지] - 나의 리뷰 총 개수 (페이징 처리)
   public int countAllMyReviews(String userId) throws SQLException {
      return reviewDao.selectMyCount(userId);
   }

   public String writeReview(Review review) {
	      int r = 0;
	      String result = "";
	      try {
	         r = reviewDao.insertReview(review);
	         if (r == 1) {
	            result = "게시물이 등록되었습니다";
	         }
	      } catch (RuntimeException e) {
	         throw new RuntimeException();
	      }

	      return result;
	   }

   // 4. 리뷰 삭제하기 deleteReview(리뷰 번호, 글쓴이)
   public  String deleteReview(Review review) throws IOException, SQLException {
      int r = reviewDao.deleteReview(review);
      String result = "해당 ID가 작성한 리뷰가 아닙니다";
      if (r == 1) {
         result = "게시물이 삭제되었습니다";
      }
      return result;
   }
   
   //관리자용 리뷰 게시글 삭제
   public  String adDeleteReview(Review review) throws IOException, SQLException {
         int r = reviewDao.adDeleteReview(review);
         String result = "해당 게시글이 존재하지 않습니다";
         if (r == 1) {
            result = "게시물이 삭제되었습니다";
         }
         return result;
      }

   // 5. 리뷰에 좋아요 누르기 heartCountUp (원하는 리뷰 번호)
   public String heartCountUp(int reivewId) throws SQLException {
      int r = reviewDao.updateReviewHeartCount(reivewId);
      String result = "좋아요를 누를 게시글이 없습니다";
      if (r == 1) {
         result = "좋아요를 눌렀습니다";
      }
      return result;
   }

}