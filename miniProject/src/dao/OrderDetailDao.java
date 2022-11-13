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
import domain.OrderDetailDto;
import domain.Product;
import provider.ConnectionProvider;
import service.ProductAndColorService;
import service.ProductAndSizeService;

public class OrderDetailDao {
	private static PreparedStatement pstmt;
	private static OrderDetailDao orderDetailDao = new OrderDetailDao();
	ColorDao colorDao = ColorDao.getInstance();

	public static OrderDetailDao getInstance() {
		return orderDetailDao;
	}
	
//	OrderDao orderDao = OrderDao.getInstace();
	ProductDao productDao = ProductDao.getInstance();
	ProductAndColorService productAndColorService = ProductAndColorService.getInstance();
	ProductAndSizeService productAndSizeService = ProductAndSizeService.getInstance();
	
	// 주문1 에 해당하는 orderDetail 들 조회하기 : 주문내역 상세보기 
	public List<OrderDetailDto> selectOrderDetails (int orderId) {
		Connection conn = ConnectionProvider.getConnection();
		List<OrderDetailDto> list = new ArrayList<>();
		
    	try {
    		conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			String sql = "select orders_detail_id, product_id, quantity, size_id, color_id from orders_detail where orders_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,  orderId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int odId = rs.getInt("orders_detail_id");
				int pid = rs.getInt("product_id");
				Product product = productDao.selectProductOne(pid);
				
				int qt = rs.getInt("quantity");
				int size = rs.getInt("size_id");
				int colorId = rs.getInt("color_id");
				Color color = colorDao.selectColor(colorId);
				
				list.add(new OrderDetailDto(odId, qt, product, size, color.getColor_name()));
			}
			conn.commit();
			
		}catch(SQLException e) {
			try {
				conn.rollback();
				System.out.println("롤백 처리 됨~~~~~~~~~~~~");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;
	}
	
	// 하나의 상품 구매하기
	public int insertOrderDetail(OrderDto od) throws SQLException {
		Connection conn = null;
		int rows = 0;
		
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			String userId = od.getUserId();
			int productId = od.getProductId();
			int color = od.getColor();
			int size = od.getSize();
			int quantity = od.getQuantity();
			
			// 주문한 상품 삭제 여부 확인 필요
			Product product = productDao.selectProductOne(productId);
			boolean colorCheck = productAndColorService.checkColor(productId, color);
			boolean sizeCheck = productAndSizeService.checkSize(productId, size);
			
			// 삭제되지 않은 상품 구매 
			if(!product.isDeleted() && colorCheck && sizeCheck) {
				System.out.println("~~~~~!!!!!!!!!!! 오 지 마 ");
				// 주문한 총 금액 계산하
				int totalPrice = product.getProductPrice() * quantity;
				
				// order 테이블에 정보 삽입  orders_id 값 받아오기
				int oId = OrderDao.getInstace().insertOrder(conn, userId, totalPrice, quantity);
				
				//oder details 테이블에 데이터 삽입하기 
				String sql ="insert into orders_detail (orders_detail_id, product_id, orders_id, quantity, size_id, color_id) values(ordersDetail_seq.nextval, ?, ?, ?, ?, ?)";
				// orderId 받아오
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, productId);
				pstmt.setInt(2, oId);
				pstmt.setInt(3, quantity);
				pstmt.setInt(4, size);
				pstmt.setInt(5, color);
				
				rows = pstmt.executeUpdate();
				conn.commit();
				
			}
			
		} catch(SQLException e) {
			try {
				conn.rollback();
				System.out.println("롤백 처리 됨~~~~~~~~~~~~"); 
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		
		return rows;
	}
	
	// 장바구니에서 상품1개 구매하기
	public int insertCartDetailToOrderDetail(Connection conn, String userId, int oId, CartDetail cd) {
		//oder details 테이블에 데이터 삽입하기 
		String sql ="insert into orders_detail (orders_detail_id, product_id, orders_id, quantity, size_id, color_id) values(ordersDetail_seq.nextval, ?, ?, ?, ?, ?)";
		// orderId 받아오
		int rows = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cd.getProduct().getProductId());
			pstmt.setInt(2, oId);
			pstmt.setInt(3, cd.getQuantity());
			pstmt.setInt(4, cd.getSize_id());
			pstmt.setInt(5, cd.getColor().getColor_id());
			
			rows = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rows;
	}
	
}
