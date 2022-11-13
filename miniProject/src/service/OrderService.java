package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import controller.dto.PagingVo;
import dao.OrderDao;
import domain.Orders;
import lombok.NoArgsConstructor;
import provider.ConnectionProvider;

@NoArgsConstructor
public class OrderService {
	private static OrderService orderService = new OrderService();
	OrderDao orderDao = OrderDao.getInstace();

	public static OrderService getInstance() {
		return orderService;
	}
	
	// order 테이블 전체 count
	public int countAll(String userId) {
		return orderDao.selectCount(userId);
	}
	
	// 모든 주문 조회 -  페이징 처
	public List<Orders> showOrders(String userId, PagingVo pvo) {
		int all = countAll(userId);
		return orderDao.selectOrder(userId, pvo);
	}		
	
	// 주문을 등록하고 등록된 id 값을 받아와서 리턴한다.
	public int addOrder(Connection conn, String uId, int totalPrice, int quantity) throws SQLException {
		return orderDao.insertOrder(conn, uId, totalPrice, quantity);
	}

	
	// 주문 취소
    public String cancelOrders(List<Integer> list) {
    	Connection conn = null;
		String result = "";
    	
    	try {
    		conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			for(int oId : list) {
		    	   int row = orderDao.deleteOrders(conn, oId);
		    	   if(row==1) {
		    		   result += oId + "번 주문 취소를 성공하였습니다. \n";
		    	   } 		    	   
		       }
			
    	} catch(SQLException e) {
    		result += "주문 취소를 실패하였습니다.";
    	} finally {
				try {
					conn.close();
				} catch (SQLException e) {e.printStackTrace();}
    	}
    	
              
       return result;
    }
	
}
