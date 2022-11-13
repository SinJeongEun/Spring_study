package controller.dto;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
	//field
	private String userName;
	private String phoneNumber;
	private String userAddress;
	
	//method : DTO -> JSON 변환
	public JSONObject userInfoToJson(UserInfo userInfo) {
		JSONObject json = new JSONObject();
		json.put("name", userInfo.getUserName());
		json.put("phoneNumber", userInfo.getPhoneNumber());
		json.put("address", userInfo.getUserAddress());
		return json;
	}
	
	//method: JSON -> DTO 변환
	public UserInfo jsonToUserInfo(JSONObject json) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(json.getString("name"));
		userInfo.setPhoneNumber(json.getString("phoneNumber"));
		userInfo.setUserAddress(json.getString("address"));
		return userInfo;
	}
}
