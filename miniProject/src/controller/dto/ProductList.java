package controller.dto;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductList {
	private int productId;
	private String productName;
	private String companyName;
	private String categoryName;
	private String productSex;
	private int productPrice;

	public JSONObject productListTOJson(ProductList productList) {
		JSONObject json = new JSONObject();
		json.put("productId", productList.getProductId());
		json.put("productName", productList.getProductName());
		json.put("companyName", productList.getCompanyName());
		json.put("categoryName", productList.getCategoryName());
		json.put("productSex", productList.getProductSex());
		json.put("productPrice", productList.getProductPrice());
		return json;
	}

	public ProductList JsonTOproductList (JSONObject json) {
		ProductList p = new ProductList();
		p.setProductId(json.getInt("productId"));
		p.setProductName(json.getString("productName"));
		p.setCompanyName(json.getString("companyName"));
		p.setCategoryName(json.getString("categoryName"));
		p.setProductSex(json.getString("productSex"));
		p.setProductPrice(json.getInt("productPrice"));
		return p;

	}
}
