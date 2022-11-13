package service;

import java.io.IOException; 
import java.sql.SQLException;
import java.util.List;

import controller.dto.PagingVo;
import controller.dto.QnaList;
import dao.QnaDao;
import domain.Qna;

public class QnAService {
   private static QnAService qnaService = new QnAService();
   private QnAService() {}
   public static QnAService getInstacne() {
      return qnaService;
   }
   
   QnaDao qnaDao = QnaDao.getInstacne();

    
    //큐엔에이 전체 글 수 구하기
   public int countAllQnas(int productId) throws SQLException {
      return qnaDao.selectCount(productId);
   }

    
    //1. 큐엔에이 간단히 보기
    public List<QnaList> showQnaList(int productId, PagingVo pvo) throws SQLException {//qna 간단히 보기
       List<QnaList> list = qnaDao.selectQnaList(productId, pvo);
            return list;
        
   }
    //2. 큐엔에이 자세히 보기 showQnaOne(원하는 큐엔에이 번호)
    public Qna showQnaOne(int qnaId, int productId) throws SQLException { // qna 하나씩 보기
       Qna qna = qnaDao.selectQna(qnaId, productId);    
        return qna;
           
    }
    //3. 큐엔에이 작성하기 writeQna(큐엔에이 번호, 큐엔에이 제목, 큐엔에이 내용, 글쓴이, 상품 번호)
    public String writeQna(Qna qna) throws SQLException {
       int r = qnaDao.insertQna(qna);
        String result = "게시물이 등록되지 않았습니다";
       if(r==1) {
          result = "게시물이 등록되었습니다";
       }
        return result;
        
    }
    //4. 큐엔에이 삭제하기 deleteQna(큐엔에이 번호, 글쓴이)
    public String deleteQna(Qna qna)throws IOException, SQLException {
        int r = qnaDao.deleteQna(qna);
       String result = "해당 ID가 작성한 게시글이 아닙니다";
        if(r==1) {
           result = "게시글이 삭제되었습니다";
        }        
        return result;
         
     }
    
    public String adDeleteQna(Qna qna)throws IOException, SQLException {
        int r = qnaDao.adDeleteQna(qna);
       String result = "해당 번호의 게시글이 없습니다";
        if(r==1) {
           result = "게시글이 삭제되었습니다";
        }        
        return result;
         
     }
    

   
   
   
    
}