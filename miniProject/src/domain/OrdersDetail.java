package domain;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class OrdersDetail {
	private int quantity;
	private Product product;
}
