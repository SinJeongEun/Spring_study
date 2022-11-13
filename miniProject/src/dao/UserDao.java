package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.dto.UserInfo;
import domain.User;
import provider.ConnectionProvider;

public class UserDao {
   //field
   private static UserDao userDao; //싱글톤: 필드
   
   //constructor : 싱글톤
   private UserDao() {};
   
   //method: 싱글톤
   public static UserDao getInstance() { 
      if(userDao == null) {
         userDao = new UserDao();
      }
      return userDao; 
   }
   
   //method: select문 - 아이디 중복 여부 판별
   public boolean selectUserId(String id) { 
      Connection conn = ConnectionProvider.getConnection();
      String sql = "select user_id from users where user_id=?";
      boolean result = false;
      
      try {
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id);
         ResultSet rs = pstmt.executeQuery();
         result = rs.next();
         pstmt.close();
         conn.close();
      } catch (SQLException e) { }
      System.out.println(false);
      return result;
   }
   
   //method: select문 - 핸드폰 번호 중복 여부 확인
   public boolean selectPnCheck(String pn) {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "select phone_number from users where phone_number=?";
      boolean result = true;
      try {
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, pn);
         ResultSet rs = pstmt.executeQuery();
         result = rs.next();
         pstmt.close();
         conn.close();
      } catch (SQLException e) { }
      //핸드폰 번호 중복이면 true;
      return result;
   }
   
   //method: select문 - 비밀번호 확인
   public boolean selectPwCheck(String loginId, String pw) {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "select user_id, user_password from users where user_id = ? and user_password=?";
      boolean result = true;
      try {
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, loginId);
         pstmt.setString(2, pw);
         ResultSet rs = pstmt.executeQuery();
         result = rs.next();
         pstmt.close();
         conn.close();
      } catch (SQLException e) { }
      //비밀번호가 맞으면 true 리턴
      return result; 
   }
   
   //method: select문 - 로그인
   public String selectLogin(User user) {

      Connection conn = ConnectionProvider.getConnection();
      String sql = "select user_id, user_type from users where user_id=? and user_password=?";
      String type = "" ;
      try {
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, user.getUserId());
         pstmt.setString(2, user.getUserPassword());
         ResultSet rs = pstmt.executeQuery();
         //로그인 성공이면 user_type 리턴
         if (rs.next()) {
            type = rs.getString("user_type"); 
         } else { 
            //로그인 실패면 null 리턴
            type = null; //로그인 실패
         }
         pstmt.close();
         conn.close();
      } catch(SQLException e) { }
      return type;
   }
   
   //method: select문 - 사용자 정보 목록
   public UserInfo selectUserInfo(String id) {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "select user_name, phone_number, user_address from users where user_id =?";
      UserInfo userInfo = new UserInfo();
      try {
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id);
         ResultSet rs = pstmt.executeQuery();
         if(rs.next()) {
            userInfo = new UserInfo(rs.getString("user_name"),
                  rs.getString("phone_number"),
                  rs.getString("user_address")
                  );
         }
         pstmt.close();
         conn.close();
      } catch(SQLException e) { }
        return userInfo;
   }
   
   //method: insert문 - 사용자 정보를 DB에 등록
   public boolean insertRegisterUser(User user) {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "insert into users(user_id, user_name, user_password, phone_number, user_address) values (?,?,?,?,?)";
      boolean result = true;
      try {
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, user.getUserId());
         pstmt.setString(2, user.getUserName());
         pstmt.setString(3, user.getUserPassword());
         pstmt.setString(4, user.getPhoneNumber());
         pstmt.setString(5, user.getUserAddress());
         int rows = pstmt.executeUpdate();
         if(rows == 1) {
            result = true; //정보 등록 성공
         }else {
            result = false; //정보 등록 실패
         }
         pstmt.close();
         conn.close();
      } catch(SQLException e) { }
      return result;
   }
   
   //method: update문 - 사용자 정보 DB에서 수정
   public boolean updateEditUser(User user, int num) {
      Connection conn = ConnectionProvider.getConnection();
      boolean result = true;
      try {
         switch(num) {
              case 1:
                 String sql = "update users set user_name=? where user_id=?";
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 pstmt.setString(1, user.getUserName());
                 pstmt.setString(2, user.getUserId());
                 int rows = pstmt.executeUpdate();
                  if(rows == 1) {
                     result = true; //정보 수정 성공
                  }else {
                     result = false; //정보 수정 실패
                  }
                  pstmt.close();
                  conn.close();
                 break;
              case 2:
                 sql = "update users set phone_number=? where user_id=?";
                 pstmt = conn.prepareStatement(sql);
                 pstmt.setString(1, user.getPhoneNumber());
                 pstmt.setString(2, user.getUserId());
                 rows = pstmt.executeUpdate();
                  if(rows == 1) {
                     result = true; //정보 수정 성공
                  }else {
                     result = false; //정보 수정 실패
                  }
                  pstmt.close();
                  conn.close();
                 break;
              case 3:
                 sql = "update users set user_address=? where user_id=?";
                 pstmt = conn.prepareStatement(sql);
                 pstmt.setString(1, user.getUserAddress());
                 pstmt.setString(2, user.getUserId());
                 rows = pstmt.executeUpdate();
                 if(rows == 1) {
                    result = true; //정보 수정 성공
                 }else {
                    result = false; //정보 수정 실패
                 }
                 pstmt.close();
                 conn.close();
                 break;
           }
      } catch(SQLException e) { }
      return result;
   }
   
   //method: delete문 - 사용자정보 DB에서 삭제
   public boolean deleteRemoveUser(String id) {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "delete from users where user_id = ?";
      boolean result = true;
      try {
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id);
         int rows = pstmt.executeUpdate();
         if(rows == 1) {
            result = true; //정보 삭제 성공
         }else {
            result = false; //정보 삭제 실패
         }
         pstmt.close();
         conn.close();
      } catch(SQLException e) { }
      return result;
   }
   
   public boolean insertRegisterAdmin(User user) {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "insert into users(user_id, user_name, user_password, phone_number, user_address, user_type) values (?,?,?,?,?,?)";
		PreparedStatement pstmt;
		boolean result =false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, user.getUserPassword());
			pstmt.setString(4, user.getPhoneNumber());
			pstmt.setString(5, user.getUserAddress());
			pstmt.setString(6, user.getUserType());
			int rows = pstmt.executeUpdate();
			
			if(rows == 1) {
				result = true; //정보 등록 성공
			}else {
				result = false; //정보 등록 실패
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
   
}



