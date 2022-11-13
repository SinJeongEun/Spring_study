package domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private String userId;
	private String userName;
	private String userPassword;
	private String phoneNumber;
	private String userAddress;
	private String userType;
}
