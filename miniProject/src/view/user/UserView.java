package view.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONObject;

import socket.client.ShoesClient;

public class UserView {
   public void startquestion(ShoesClient shoesClient) throws SQLException, IOException {
      JSONObject clientMessage = new JSONObject();
      shoesClient.send(clientMessage.put("viewName", "UserView").toString());
      
      Scanner sc = new Scanner(System.in);
      System.out.println();
      System.out.println("=================반갑습니다♥ 고객님===========================");
      System.err.println("                     \r\n"
            + "                   $$#:.      \r\n"
            + "  Welcome !      ,;####*.,-,.    <원하는 서비스를 선택해주세요!> \r\n"
            + "                 !$###=#$$=$,     \r\n"
            + "  This is        ~####*!*###     -------------- \r\n"
            + "  Shoes Shop !   *#####=$###     | 1.상품보기 |\r\n"
            + "                .####!##=###     ------------------\r\n"
            + "  Have a         ==, ,:####*     | 2.장바구니보기 |\r\n"
            + "  Nice day ~    ,,.===~####*      ------------------\r\n"
            + "               ..$-.#@@*###*     | 3.마이페이지 |\r\n"
            + "               =*-...,#$###=     --------------\r\n"
            + "              : =-;=@~=####=     | 4.로그아웃 |\r\n"
            + "              #=-, ~###@###=.    -------------\r\n"
            + "           .~-. -*@*#*@@###!-    | 5.뒤로가기 |\r\n"
            + "          . ~$;$=.*@#@@####*=    -------------\r\n"
            + "        .:- -;,,#$$##@@####*# \r\n"
            + "       =###*-~#:!#$#@@@@####: \r\n"
            + "   .    .####=$#$#@@@@@@#=~.. \r\n"
            + "  ...:   .:!*$$#@@@@@@#~...., \r\n"
            + " .. .. .!.-:#$###@#=;....,~!    \r\n"
            + " ..,..........,........,*    \r\n"
            + " ,,,.................-!       \r\n"
            + " ,,,,,,.....,...,.:!:        \r\n"
            + "   ,,,,,,,,.,,.,,");
      System.out.println();
      System.out.println("===============================================================");
      System.out.println();
      
      //System.out.println("원하시는 서비스를를 선택하여 주세요.");
      //System.out.println("1. 상품보기 2. 장바구니보기 3. 마이페이지 4. 로그아웃 5. 뒤로가기 ");
      System.out.print("선택 >");
      int num = Integer.parseInt(sc.nextLine());

      switch(num) {
      case 1:
         ProductView productView = new ProductView();
         productView.startquestion(shoesClient, 1);
         break;
      case 2:
         CartView cartView = new CartView();
         cartView.startquestion(shoesClient);
         break;
      case 3:
         MypageView mypageView = new MypageView();
         mypageView.startquestion(shoesClient);
         break;
      case 4:
         clientMessage.put("viewName", "logout");
         shoesClient.send(clientMessage.toString());
         System.out.println(shoesClient.receive().getString("result"));
         StartView startView = new StartView();
         startView.startquestion(shoesClient);
         break;   
      case 5:
         clientMessage.put("viewName", "back");
         shoesClient.send(clientMessage.toString());
         
         JSONObject pView = shoesClient.receive();
         shoesClient.goBack(pView);
         break;
   }
      
      

   }
}