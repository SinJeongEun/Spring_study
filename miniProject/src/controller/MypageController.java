package controller;

import org.json.JSONObject;

import controller.dto.UserInfo;
import domain.User;
import service.UserService;
import socket.server.SocketClient;

public class MypageController implements Controller {
   //field
   SocketClient socketClient;
   JSONObject cMessage;
   JSONObject result;
   UserService userService = UserService.getInstance();//싱글톤 객체 호출
   
   //method: process() 재정의
   @Override
   public void control(SocketClient socketClient, JSONObject cMessage) {
      //variable
      this.socketClient = socketClient;
      this.cMessage = cMessage;
      User user = new User();
      String function = cMessage.getString("function");
      
      //JSON <-> DTO
      switch(function) {
         case "password":
            if(userService.pwCheck(socketClient.getLoginId(), cMessage.getString("pw")) == 1){
               UserInfo userInfo = new UserInfo();
               result = userInfo.userInfoToJson(userService.userInfo(socketClient.getLoginId()));
               cMessage.put("result", result);
               socketClient.send(cMessage.toString());
            } else {
               boolean pwError = true;
               JSONObject jo = new JSONObject().put("pwError", pwError);
               socketClient.send(jo.toString());
            }
            break;
      
         case "updateUser":
            user.setUserId(socketClient.getLoginId());
            switch(cMessage.getString("choice")) {
               case "1":
                  user.setUserName(cMessage.getString("update"));
                  break;
               case "2":
                  user.setPhoneNumber(cMessage.getString("update"));
                  break;
               case "3":
                  user.setUserAddress(cMessage.getString("update"));
                  break;
            }
            if(userService.editUserInfo(user, cMessage.getInt("choice"))) {
               boolean result = true;
               cMessage.put("result", result);
               socketClient.send(cMessage.toString());
            } 
            break;
            
         case "deleteUser":
            if(userService.removeUserInfo(socketClient.getLoginId())) {
               boolean result = true;
               cMessage.put("result", result);
               socketClient.send(cMessage.toString());
            }
            break;
      }
   }
}