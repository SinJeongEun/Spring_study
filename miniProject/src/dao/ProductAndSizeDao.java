package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import provider.ConnectionProvider;

public class ProductAndSizeDao {
	
	private static ProductAndSizeDao productAndSizeDao = new ProductAndSizeDao();
	private ProductAndSizeDao() {
	}
	public static ProductAndSizeDao getInstance() {
		return productAndSizeDao;
	}

	public int insertProductSizes(int pid, List<Integer> sizeList) throws SQLException {
		// 상품 id ,그 상품의 사이즈들을 리스트로 받아와서 각각 Product_size 테이블에 삽입한다.
		Connection conn = ConnectionProvider.getConnection();
		PreparedStatement pstmt = null;
		int sum = 0;
		int result = 0;
		
			for(int psize : sizeList) {
				
				String sql = "insert into product_size(product_id, size_id) values (?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, pid);
				pstmt.setInt(2, (psize));
				sum += pstmt.executeUpdate();
			}
			if(sizeList.size() == sum) {
				result = 1;
			}
			conn.close();
			pstmt.close();
			
			return result;
			
		
	}
	
	// 해당상품의 전체 사이즈  리스트 리턴 
	public List<Integer> selectProductSizes(int productId) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();

		String sql = "select size_id from product_size where product_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,  productId);
		
		ResultSet rs = pstmt.executeQuery();
		List<Integer> sizeList = new ArrayList<>();
		while(rs.next()) {
			sizeList.add(rs.getInt("size_id"));	
			
        }
		pstmt.close();
		conn.close();
		return sizeList;
	}
}
