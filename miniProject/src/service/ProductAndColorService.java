package service;

import java.sql.SQLException;
import java.util.List;

import dao.ProductAndColorDao;
import domain.Color;

public class ProductAndColorService {
	private static ProductAndColorService productAndColorService = new ProductAndColorService();
	private ProductAndColorService() {}
	public static ProductAndColorService getInstance() {
		return productAndColorService;
	}
	
	ProductAndColorDao productAndColorDao = ProductAndColorDao.getInstance();
	public void addProductColors(int pid, List<Integer> colorList) throws SQLException {
		productAndColorDao.insertProductColors(pid, colorList);

	}

	// 해당상품의 전체 색상 리스트 리턴
	public List<Color> findProductColors(int productId) throws SQLException {
		List<Color> colorList = productAndColorDao.selectProductColors(productId);
		return colorList;

	}
	
	// 유저가 선택한 색상이 상품에 있는지 확인
		public boolean checkColor(int productId, int colorId) throws SQLException {
			List<Color> list = findProductColors(productId);
			
			boolean result = false;
			for(Color c : list) {
				if(c.getColor_id() == colorId) {
					result = true;
					break;
				}
			}
			return result;
		
		}

}
