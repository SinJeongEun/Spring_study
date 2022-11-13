package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import controller.dto.OrderDto;
import dao.CartDetailDao;
import domain.CartDetail;
import domain.Product;
import provider.ConnectionProvider;

public class CartDetailService {
	static PreparedStatement pstmt;
	private static CartDetailService cartDetailService = new CartDetailService();
	
	CartService cartService = CartService.getInstance();
	CartDetailDao cartDetailDao = CartDetailDao.getInstance();
	ProductService productService = ProductService.getInstance();
	ProductAndColorService productAndColorService = ProductAndColorService.getInstance();
	ProductAndSizeService productAndSizeService = ProductAndSizeService.getInstance();
	
	private CartDetailService() {}
	
	public static CartDetailService getInstance() {
		return cartDetailService;
	}
	
	// 해당 상품을 장바구니에 넣는다.
	// 장바구니속 총 수량,총 금액을 수정한다.
    public String addCartDetail(OrderDto od) { 
    	Connection conn = null;
		String result = "";
    	
    	try {
    		conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
    		Product product = productService.showOneProduct(od.getProductId());

    		
    		// 주문한 상품 색상, 사이즈  확인 필요
			boolean colorCheck = productAndColorService.checkColor(product.getProductId(), od.getColor());
			boolean sizeCheck = productAndSizeService.checkSize(product.getProductId(), od.getSize());
			
			// 삭제되지 않은 상품 구매 , 색상, 컬러 확인 후 장바구니에 담을 수 있다.
			if(product.isDeleted() || !colorCheck || !sizeCheck) {

				return od.getProductId() + "번 상품을 장바구니에 담는데 실패했습니다."; 

			}

			int r = cartDetailDao.insertCartDetail(conn, od);
			
			// 장바구니에 상품을 담았으므로, 장바구니의 총 수량과 총 금액을 업데이트 해야된다.
			cartService.refreshCart(conn, od.getUserId(), product.getProductPrice() * od.getQuantity(), od.getQuantity());
			result = od.getProductId() + "번 상품이 장바구니에 담겼습니다.";
        	 		
        	conn.commit();
    	} catch(SQLException |RuntimeException e) {
    		try {
				conn.rollback();
				result = od.getProductId() + "번 상품을 장바구니에 담는데 실패했습니다."; 
				e.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
		
    	return result;
	}
  
    
    // 장바구니에 담긴 내역들 list 로 Cart 에 전달하기
    public List<CartDetail> findByUserId(Connection conn, String userId) throws SQLException {
    	List<CartDetail> list = cartDetailDao.selectCartDetails(conn, userId);
		return list;
    }
    
    
    //장바구니 항목 1삭제
    public String removeCartDetailOne(String userId, int cdId){  
    	Connection conn = null;
    	String result = "";
    	
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			CartDetail cd = getCartDetailOne(conn, cdId);
			int mp = cd.getProduct().getProductPrice() * cd.getQuantity();
			int mq = cd.getQuantity();
			
			int r = cartDetailDao.removeCartDetailOne(conn, cdId);
			
			if(r == 1) {
				cartService.refreshCart(conn, userId, -mp, -mq);
				result = cdId + "번 장바구니 상품이 삭제되었습니다."; 
			}
			conn.commit();
			
		} catch(Exception e) {

			e.printStackTrace();
			
			try {
				conn.rollback();
				result = cdId + "번 장바구니 상품이 삭제에 실패했습니다.!!!"; 
				throw new RuntimeException(e);


			} catch (SQLException e1) {
				e1.printStackTrace();
				e.printStackTrace();
			}
		} finally {
			if(conn!=null) try { conn.close();}catch(Exception e) { }
		}
		return result;
    }
    
    
    
    //cartDetail 객체 1개 반환
    public CartDetail getCartDetailOne(Connection conn, int cartDetailId) {
    	CartDetail cd;
		try {
			cd = cartDetailDao.selectCartDetail(conn, cartDetailId);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    	return cd;
    }

    //—————————— 카트 디케일 서비스의 updateCartDetailQuantity 메소드 ㅡㅡㅡㅡ 

 // 장바구니 상세내역의 수량 변경
     public String updateCartDetailQuantity(String userId, int cartDetailId, int quantity) {
     	Connection conn = null;
     	String str = "";
     	   	
     	try {
     		conn = ConnectionProvider.getConnection();
     		conn.setAutoCommit(false);
     		
     		// 변경 전 기존의 수량, 수량*상품금액 가져오기
     		CartDetail cd = getCartDetailOne(conn, cartDetailId);
     		
     		// 삭제된 상품인 경우 수량 변경 막기
     		if(cd.getProduct().isDeleted()) return "수량 변경에 실패했습니다. ";
     		
     		int xquantity = cd.getQuantity();
     		int xtotalPrice = cd.getProduct().getProductPrice() * xquantity;
     		
     		// 새로 변경할 수량, 그에따른 가격
     		int q = quantity;
     		int p = cd.getProduct().getProductPrice() * q;
     		// 새로운 변경사항 반영하기 
     		cartDetailDao.updateQuantity(conn, cartDetailId, quantity);
     		
     		// cart 의 총 수량, 금액 변경하기 
     		int rq = -xquantity + quantity;
     		int rp = -xtotalPrice + p;
     		cartService.refreshCart(conn, userId, rp, rq);
    
     		str = "수량 변경이 완료되었습니다. ";
     		
     		conn.commit();
 			
 		} catch(Exception e) {
 			e.printStackTrace();
 			
 			try {
 				conn.rollback();
 	    		str = "수량 변경에 실패했습니다. ";
// 				throw new RuntimeException(e);

 			} catch (SQLException e1) {}
 		} finally {
 			if(conn!=null) try { conn.close();} catch(Exception e) { }
 		}
     	
 		return str;
     }
    

    
}
