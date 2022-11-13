package domain;



import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	private int productId;
	private String productName;
	private boolean isDeleted;
	private int productPrice;
	private String productSex;
	private String company;
	private String category;
	private List<Color> colorList;
	private List<Integer> sizeList;
	 
	
	
	public JSONObject productToJson(Product p) {
		
		JSONObject json = new JSONObject();
		json.put("productId", p.getProductId());
		json.put("productName", p.getProductName());
		json.put("isDeleted", p.isDeleted);
		json.put("productPrice", p.getProductPrice());
		json.put("productSex", p.getProductSex());
		json.put("company", p.getCompany());
		json.put("category", p.getCategory());
		json.put("sizeList", p.getSizeList());
		
		JSONArray jarr = new JSONArray();
		for(Color c : p.getColorList()) {
			jarr.put(c.colorToJson(c));
		}
		json.put("colorList", jarr);
		
		return json;
	}
	
	public Product jsonToProduct(JSONObject jo) {
		Color c = new Color();
		Product p = new Product();
		p.setProductId(jo.getInt("productId"));
		p.setProductName(jo.getString("productName"));
		p.setDeleted(jo.getBoolean("isDeleted"));
		p.setProductPrice(jo.getInt("productPrice"));
		p.setProductSex(jo.getString("productSex"));
		p.setCompany(jo.getString("company"));
		p.setCategory(jo.getString("category"));
		
		List<Integer> sizeList = new ArrayList<>();
		for(Object ob : jo.getJSONArray("sizeList")) {
			sizeList.add(Integer.parseInt(ob.toString()));
		}
		p.setSizeList(sizeList);
		
		List<Color> colorList = new ArrayList<>();
		JSONArray arr = jo.getJSONArray("colorList");
		for(int i=0; i<arr.length(); i++) {
			colorList.add(c.jsonToColor(arr.getJSONObject(i)));
		}
		
		p.setColorList(colorList);
		return p;	
	}
}
