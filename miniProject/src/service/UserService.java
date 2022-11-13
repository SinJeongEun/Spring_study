package service;

import java.sql.SQLException;

import controller.dto.UserInfo;
import dao.UserDao;
import domain.User;

public class UserService {
	String result = "";
	// 싱글톤 호출
	UserDao userDao = UserDao.getInstance(); // 싱글톤 객체 호출
	CartService cartService = CartService.getInstance();
	// 싱글톤 생성
	private static UserService userService; // 싱글톤: field

	private UserService() {
	}; // 싱글톤: constructor

	public static UserService getInstance() { // 싱글톤: method
		if (userService == null) {
			userService = new UserService();
		}
		return userService;
	}

	// method: 아이디 중복여부 확인 (회원 여부 확인)
	public boolean idCheck(String id) {

		return userDao.selectUserId(id);
	}

	// method: 핸드폰 번호 중복여부 확인
	public boolean pnCheck(String pn) {

		return userDao.selectPnCheck(pn);
	}

	// method: 비밀번호 확인
	public int pwCheck(String loginId, String pw) {
		int result = 0;

		if (userDao.selectPwCheck(loginId, pw)) {
			result = 1; // 비밀번호 일치
		} else {
			result = 0; // 비밀번호 불일치
		}
		return result;
	}

	// method: 유저 등록(추가)
	public String registerUser(User user) throws SQLException {

		if (userDao.insertRegisterUser(user)) {
			cartService.createCart(user.getUserId());
			result = "회원가입이 완료되었습니다.";
		} else {
			result = "회원가입에 실패하였습니다.";
		}
		return result;
	}

	// method: 관리자 등록(추가)
	public String registerAdmin(User user) {
		if (userDao.insertRegisterAdmin(user)) {
			result = "회원가입이 완료되었습니다.";
		} else {
			result = "회원가입에 실패하였습니다.";
		}
		return result;
	}

	// method: 로그인
	public String login(User user) {
	
		String type = "";
		if (userDao.selectLogin(user) != null) {
			type = userDao.selectLogin(user); // 로그인 성공
		} else {
			type = null; // 로그인 실패
		}

		return type;
	}

	// method: 비밀번호 맞음 -> 회원정보 목록
	public UserInfo userInfo(String id) {
		UserInfo userInfo = new UserInfo();
		userInfo = userDao.selectUserInfo(id);
		return userInfo;
	}

	// method: 회원정보 수정
//	public String editUserInfo(User user, int num) throws IOException, SQLException {
//		if(userDao.updateEditUser(user, num)) {
//			result = "정보 수정이 완료되었습니다.";
//		} else {
//			result = "정보 수정이 실패하였습니다.";
//		}
//		return result;
//	}
	public boolean editUserInfo(User user, int num) {
		boolean result = true;
		if (userDao.updateEditUser(user, num)) {
			result = true; // 정보수정 완료
		} else {
			result = false; // 정보수정 실패
		}
		return result;
	}

	// method: 회원탈퇴
	public boolean removeUserInfo(String id) {
		boolean result = true;
		if (userDao.deleteRemoveUser(id)) {
			result = true; // 회원탈퇴 성공
		} else {
			result = false; // 회원탈퇴 실패
		}
		return result;
	}

	// method: 회원탈퇴
//	public String removeUserInfo(String id) throws IOException, SQLException {
//		if(userDao.deleteRemoveUser(id)) {
//			result = "회원탈퇴가 완료되었습니다.";
//		} else {
//			result = "회원탈퇴를 실패하였습니다.";
//		}
//		return result;
//	}

}
