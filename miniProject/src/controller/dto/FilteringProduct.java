package controller.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class FilteringProduct {
	private int companyId;
	private int categotyId;
	private String productSex;
	private String orderByPrice;
}
