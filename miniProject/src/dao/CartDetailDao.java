package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.dto.OrderDto;
import domain.CartDetail;
import domain.Color;
import domain.Product;

public class CartDetailDao {
	PreparedStatement pstmt;
	private static CartDetailDao cartDetailDao = new CartDetailDao();
	ProductDao productDao = ProductDao.getInstance();
	ColorDao colorDao = ColorDao.getInstance();

	public static CartDetailDao getInstance() {
		return cartDetailDao;
	}
	
	// 장바구니에 담긴 내역 1개 조회 
	public CartDetail selectCartDetail(Connection conn, int cdId) throws SQLException {
		String sql = "select cart_detail_id, product_id, quantity, size_id, color_id, user_id from cart_detail where cart_detail_id=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,  cdId);
		
		ResultSet rs = pstmt.executeQuery();
		CartDetail cartDetail = null;
		
		while(rs.next()) {
			int cid = rs.getInt("cart_detail_id");
			int pid = rs.getInt("product_id");
			Product product = productDao.selectProductOne(pid);
			
			int qt = rs.getInt("quantity");
			int size = rs.getInt("size_id");
			int colorId = rs.getInt("color_id");
			Color color = colorDao.selectColor(colorId);

			String uid = rs.getString("user_id");
			
			cartDetail = new CartDetail(cid, qt, product, uid, size, color);
        }
		return cartDetail;
	}

	// 장바구니에 담긴 내역들 조회 
	public List<CartDetail> selectCartDetails(Connection conn, String userId) throws SQLException { 
		String sql = "select cart_detail_id, product_id, quantity, size_id, color_id, user_id from cart_detail where user_id=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,  userId);
		
		ResultSet rs = pstmt.executeQuery();
		List<CartDetail> list = new ArrayList<>();
		
		while(rs.next()) {
			int cid = rs.getInt("cart_detail_id");
			int pid = rs.getInt("product_id");
			Product product = productDao.selectProductOne(pid);
			
			int qt = rs.getInt("quantity");
			int size = rs.getInt("size_id");
			int colorId = rs.getInt("color_id");
			Color color = colorDao.selectColor(colorId);

			String uid = rs.getString("user_id");
			
			list.add(new CartDetail(cid, qt, product, uid, size, color));
        }
		return list;
	}
	
	// 장바구니에 상품 담기
    public int insertCartDetail(Connection conn, OrderDto od) throws SQLException{ 
		String userId = od.getUserId();
		int productId = od.getProductId();
		int color = od.getColor();
		int size = od.getSize();
		int quantity = od.getQuantity();
		
		String sql = "insert into cart_detail(cart_detail_id, product_id, user_id, size_id, color_id, quantity) values (cartDetail_seq.nextval,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, productId);
		pstmt.setString(2, userId);
		pstmt.setInt(3, size);
		pstmt.setInt(4, color);
		pstmt.setInt(5, quantity);


		int rows = pstmt.executeUpdate();
		return rows;
		
	}	
    
    //장바구니에 담긴 항목 1개 삭제
    public int removeCartDetailOne(Connection conn, int id) {    	
		String sql = "delete from cart_detail where cart_detail_id=?";
		int row = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			row = pstmt.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row;
		
    }
    
    // 장바구니 내역의 수량 변경
    public int updateQuantity(Connection conn, int id, int quantity) {    	
    	 String sql = "update cart_detail set quantity=? where cart_detail_id=?";
    	 int rows = 0;
    	 
         try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, id);
            
            rows = pstmt.executeUpdate();
            
         } catch (SQLException e) {
            e.printStackTrace();
         }
         
         return rows;
         
    }
    
    
    
    
    
    
    
}
