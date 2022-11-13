package domain;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class CartDetail {
	private int cart_detail_id;
	private int quantity;
	private Product product;
	private String user_id;
	private int size_id;
	private Color color;	
	
}
