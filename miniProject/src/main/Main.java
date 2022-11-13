package main;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

	public static void main(String[] args) {
		Connection conn = null;
	      try { // JDBC Driver를 메모리로 로딩하고, DriverManager에 등록
	         Class.forName("oracle.jdbc.OracleDriver");

	         // DB와 연결 
	         conn = DriverManager.getConnection("jdbc:oracle:thin:@kosa402.iptime.org:50021/orcl", "java", "oracle");
	         System.out.println("연결 성공"); 
	         
//	         QnAService.startQnAService(conn);
//	         System.out.println();
//	         QnAService.writeQna(2, "제발 되라", "되면 국밥이다", "d", 3);
	        
	         
	         
	      } catch (Exception e) {
	    	  System.out.println(e.getMessage());
	    	  
	      }

	}

}
