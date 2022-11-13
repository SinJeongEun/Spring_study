package domain;


import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class Reply {
	private int replyId;
	private String replyContent;
	private String userId;
	private int reviewId;
	private int qnaId;
	
	
	

}


