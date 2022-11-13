package controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDto {
	private String userId;
	private int productId;
	private int color;
	private int size;
	private int quantity;
	
}
