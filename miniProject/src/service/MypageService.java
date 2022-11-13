package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import domain.User;
import provider.ConnectionProvider;
import socket.server.SocketClient;

public class MypageService {
   // field
   private static PreparedStatement pstmt;
   User user = new User();

   // method: 개인정보 수정
   public static void updateInfo(SocketClient socketClient, JSONObject cMessage) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      JSONObject resultMessage = new JSONObject();
      if(!cMessage.has("num")) {
         //개인정보 목록
         String sql = "select user_name, phone_number, user_address from users where user_id =? and user_password=?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, socketClient.getLoginId());
         pstmt.setString(2, cMessage.getString("pw"));
         ResultSet rs = pstmt.executeQuery();
         
         if(rs.next()) {
               resultMessage.put("result", "\n사용자명: " + rs.getString("user_name") + 
                     "\n핸드폰번호: " + rs.getString("phone_number") +
                     "\n주소: " + rs.getString("user_address")
                     );
         } else {
            resultMessage.put("result", "비밀번호 아니지롱");
         }
         socketClient.send(resultMessage.toString());
      } else {
         resultMessage.remove("result");
         int result1 = 0;
         //개인정보 수정
         switch(cMessage.getInt("num")) {
         case 1:
            String sql = "update users set user_name=? where user_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cMessage.getString("update"));
            pstmt.setString(2, socketClient.getLoginId());
            result1 = pstmt.executeUpdate();
            break;
         case 2:
            sql = "update users set phone_number=? where user_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cMessage.getString("update"));
            pstmt.setString(2, socketClient.getLoginId());
            result1 = pstmt.executeUpdate();
            break;
         case 3:
            sql = "update users set user_address=? where user_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cMessage.getString("update"));
            pstmt.setString(2, socketClient.getLoginId());
            result1 = pstmt.executeUpdate();
            break;
         }
         if(result1==1) {
            resultMessage.put("result", "수정완료");
            socketClient.send(resultMessage.toString());
         } else {
            resultMessage.put("result", "수정실패");
            socketClient.send(resultMessage.toString());
         }
         
      }
      conn.close();
   }

   // method: 회원정보 삭제
   public static void deleteInfo(SocketClient socketClient, JSONObject cMessage) throws SQLException {
      Connection conn = ConnectionProvider.getConnection();
      String sql = "delete from users where user_id = ? and user_password=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, socketClient.getLoginId());
      pstmt.setString(2, cMessage.getString("pw"));
      int rs = pstmt.executeUpdate();

      JSONObject resultMessage = new JSONObject();
      if (rs == 1) {
         resultMessage.put("result", "탈퇴를 성공하였습니다!");
      } else {
         resultMessage.put("result", "탈퇴를 실패하였습니다ㅜ");
      }
      String result = resultMessage.toString();
      socketClient.send(result);
      conn.close();

   }

}