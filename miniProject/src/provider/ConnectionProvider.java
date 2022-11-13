package provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionProvider {
	
//방법1
	public static Connection getConnection() {
		
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@kosa402.iptime.org:50021/orcl", "java", "oracle");
	         
		} catch (Exception e) {
			System.out.println("연결실패");
			e.printStackTrace();
		} 
		return conn;
	}
	
//	private static BasicDataSource dataSource;
//	static {
//		dataSource = new BasicDataSource(); //싱글톤으로 만들겠다는 이야기 
//		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
//		dataSource.setUrl("jdbc:oracle:thin:@kosa402.iptime.org:50021/orcl");
//		dataSource.setUsername("java");
//		dataSource.setPassword("oracle");
//		dataSource.setMaxTotal(5); //최대 몇개까지 만들도록할 것인가
//		dataSource.setInitialSize(1); //몇개의 커넥션을 미리 생성해놓겠는가  (초기사이즈)
//		dataSource.setMaxIdle(1); //최대 몇개까지 남겨놓고 죽일것인가
//	}
//	
//	public static Connection getConnection() {
//		Connection conn = null;
//		try {
//			conn = dataSource.getConnection();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return conn;
//	}
//	
//	public static void main(String[] args) {
//		Connection conn = null;
//		try {
//			//Connection 대여
//			conn = ConnectionProvider.getConnection();
//			System.out.println("대여 성공");
//		} catch(Exception e) {
//			
//		} finally {
//			try {
//				//Connection 반납
//				conn.close();
//				System.out.println("반납 성공");
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
