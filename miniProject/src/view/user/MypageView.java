package view.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.UserInfo;
import socket.client.ShoesClient;

public class MypageView {
   // field
   ShoesClient shoesClient;
   JSONObject clientMessage = new JSONObject();
   JSONObject json = new JSONObject();
   JSONArray jsonArr = new JSONArray();
   UserInfo info = new UserInfo();
   Scanner sc = new Scanner(System.in);
   String choice;

   // method: 마이페이지 첫 화면
   public void startquestion(ShoesClient shoesClient) throws IOException, SQLException {
      this.shoesClient = shoesClient;
      clientMessage.put("viewName", "MypageView");

      while (true) {
         System.out.println("**--환영합니다--**");
         System.out.println("원하시는 메뉴를 선택하여 주세요.");
         System.out.println("| 1.개인정보 수정 | 2.주문내역/리뷰작성 | 3.나의 리뷰 | 4.회원탈퇴 | 5.뒤로가기 | 6. 찐 뒤로가");
         System.out.print("선택 >");
         choice = sc.nextLine();
         switch (choice) {
         case "1":
            updateUser(); // 회원 정보수정창
            break;
            
         case "2":
            // 정은이 추가 예정
        	 OrderView orderView = new OrderView();
        	 orderView.showOrders(shoesClient, 1);
            break;
            
         case "3":
            MyReviewView myReviewView = new MyReviewView();
            myReviewView.startquestion(shoesClient);
            break;
            
         case "4":
            deleteUser(); // 회원 탈퇴창
            break;
            
         case "5":
            UserView memberView = new UserView();
            memberView.startquestion(shoesClient);
            break;
            
         case "6":
				clientMessage.put("viewName", "back");
				shoesClient.send(clientMessage.toString());
				
				JSONObject pView = shoesClient.receive();
				shoesClient.goBack(pView);
				break;
         default:
            System.out.println("번호를 다시 입력해주세요!");
            break;
         }
         continue;
      }
   }

   // method: 개인정보 수정 - <메인창>
   public void updateUser() throws IOException {
      // 메인창
      System.out.println("==================================");
      System.out.println("         ⟦ 개인정보 수정 ⟧");
      System.out.println("==================================");

      while (true) {
         clientMessage.put("function", "password");
         System.out.print("비밀번호를 입력해주세요>");
         String pw = sc.nextLine();
         clientMessage.put("pw", pw);
         shoesClient.send(clientMessage.toString());

         json = shoesClient.receive();

         if (json.has("pwError")) {
            System.out.println("비밀번호가 맞지 않습니다.");
            continue;
         } else {
            UserInfo userInfo = info.jsonToUserInfo(json.getJSONObject("result")); // UserInfo(DTO) 형식으로 변환
            printUserInfo(userInfo); // 서브창
            break;
         }
      }
   }

   // method: 개인정보 수정 - <서브창>
   public void printUserInfo(UserInfo userInfo) throws IOException {
      clientMessage.put("function", "updateUser");

      System.out.println();
      System.out.println("---------<기존 회원 정보>---------");
      System.out.println("사용자명: \t\t" + userInfo.getUserName());
      System.out.println("핸드폰번호: \t" + userInfo.getPhoneNumber());
      System.out.println("주소: \t\t" + userInfo.getUserAddress());
      System.out.println("-----------[변경할 부분]---------");
      System.out.println("| 1.사용자명 | 2.핸드폰번호 | 3.주소 |");
      System.out.println("------------------------------");

      while (true) {
         System.out.print("번호를 입력해주세요>");
         choice = sc.nextLine();
         if (choice.equals("1") || choice.equals("2") || choice.equals("3")) {
            clientMessage.put("choice", choice);
            break;
         } else {
            System.out.println("번호를 다시 입력해주세요!");
            continue;
         }
      }

      System.out.print("변경할 내용을 입력해주세요>>");
      String update = sc.nextLine();
      clientMessage.put("update", update);
      shoesClient.send(clientMessage.toString());

      json = shoesClient.receive();

      if (json.has("result")) {
         System.out.println("변경을 성공하였습니다.");
      } else {
         System.out.println("변경을 실패하였습니다.");
      }
      System.out.println();
   }

   // method: 회원탈퇴 - <메인창>
   public void deleteUser() throws IOException, SQLException {
      clientMessage.put("function", "password");

      System.out.println("==================================");
      System.out.println("            ⟦ 회원탈퇴 ⟧");
      System.out.println("==================================");

      while (true) {
         System.out.print("비밀번호를 입력해주세요>");
         String pw = sc.nextLine();
         clientMessage.put("pw", pw);
         shoesClient.send(clientMessage.toString());

         json = shoesClient.receive();

         if (json.has("pwError")) {
            System.out.println("비밀번호가 맞지 않습니다.");
            continue;
         } else {
            deleteUserSub();
            break;
         }
      }

      System.out.println();
      startquestion(shoesClient);
   }

   // method: 회원탈퇴 - <서브창>
   public void deleteUserSub() throws IOException, SQLException {
      clientMessage.put("function", "deleteUser");

      while (true) {
         System.out.println("-----------------");
         System.out.println("|1. 탈퇴 | 2. 취소 |");
         System.out.println("-----------------");
         System.out.print("선택 >");
         choice = sc.nextLine();

         switch (choice) {
         case "1":
            shoesClient.send(clientMessage.toString());
            break;
         case "2":
            startquestion(shoesClient);
            break;
         default:
            System.out.println("번호를 다시 입력해주세요!");
            break;
         }

         json = shoesClient.receive();

         if (json.has("result")) {
            System.out.println("탈퇴를 성공하였습니다");
            StartView startUserView = new StartView();
            startUserView.startquestion(shoesClient);
         } else {
            System.out.println("탈퇴를 실패하였습니다.");
            continue;
         }
      }
   }
}