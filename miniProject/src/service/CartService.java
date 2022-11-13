package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import controller.dto.CartDetailDto;
import controller.dto.CartDto;
import dao.CartDao;
import domain.CartDetail;
import provider.ConnectionProvider;

public class CartService {
    private static CartService cartService = new CartService();
    
    CartDao cartDao = CartDao.getInstance();
    
    private CartService() {}
    
    public static CartService getInstance() {
    	return cartService;
    }
    
    // 회원가입시 유저 당 카트 생성 
    public void createCart(String userId) throws SQLException {
    	cartDao.insertCart(userId);
    }
    
	//장바구니에 담긴 상품들 ,총 수량, 총 금액 보여준다.
    public CartDto showCart(String userId) throws SQLException {
    	CartDetailService cartDetailService = CartDetailService.getInstance();
    	Connection conn = ConnectionProvider.getConnection();
    	CartDto cartDto = null;
    	
    	try {			
			cartDto = cartDao.selectCart(conn, userId);
			CartDetailDto cdto = new CartDetailDto();
			//cart detail 정보 가져오기
			List<CartDetail> cdList = cartDetailService.findByUserId(conn,userId);
			
			List<CartDetailDto> list = cdto.toCartDetailDtoList(cdList);
			cartDto.setCartDetailDtoList(list);
			
    	} catch(SQLException e) {   		
			e.printStackTrace();
    	}
    		
	   return cartDto;
    	
    }
		
	//장바구니의 총 수량, 금액  수정
   public void refreshCart(Connection conn, String userId, int totalPrice, int quantity){
	   // 기존의 수량, 가격 가져오기
	   CartDto cd;
	try {
		cd = showCart(userId);
		int p = cd.getTotalPrice();
		int q = cd.getTotalQuantity();
		
		// 수량, 가격 변경싴키기
		int np = p + totalPrice;
		int nq = q + quantity;

		cartDao.updateCart(conn, userId, np, nq);
	} catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException(e);
	}
   }

}
