package controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class RegisterProduct {
	
	private String productName;
	private int productPrice;
	private String productSex;
	private int company;
	private int category;
	private List<Integer> colorList;
	private List<Integer> sizeList;
	
	
	
}

