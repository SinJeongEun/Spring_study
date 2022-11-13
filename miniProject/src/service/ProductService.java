package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import controller.dto.PagingVo;
import controller.dto.ProductList;
import controller.dto.RegisterProduct;
import dao.PfilteringDao;
import dao.ProductAndColorDao;
import dao.ProductAndSizeDao;
import dao.ProductDao;
import domain.Product;
import provider.ConnectionProvider;

public class ProductService {
	//싱글톤 구현
	private static ProductService productService = new ProductService();
	private ProductService() {
	}
	public static ProductService getInstance() {
		return productService;
	}
	// ProductDao 객체 불러옴
	ProductDao productDao = ProductDao.getInstance();
	PfilteringDao pfilteringDao = PfilteringDao.getInstance();
	ProductAndColorDao productAndColorDao = ProductAndColorDao.getInstance();
	ProductAndSizeDao productAndSizeDao = ProductAndSizeDao.getInstance();
	
	// 필터링 x
	// 상품 총수량
	public int countAllProducts() throws SQLException {
		return productDao.selectCountAll();
	}

	// 상품 간단히 보기
	public List<ProductList> showProducts(PagingVo pvo) throws SQLException { // List<ProductList>
		List<ProductList> list = productDao.selectProductList(pvo);
		return list;
	}

	// 필터링 o
	// 1) 카테고리 기준
	// 고른 카테고리 총 수량
	public int countCategory(int category) throws SQLException {
		return pfilteringDao.selectCountCategory(category);
	}

	// 고른 카테고리 상품 간단히 보기
	public List<ProductList> showCategory(PagingVo pvo, int cate) throws SQLException {
		return pfilteringDao.selectShowCategory(pvo, cate);
	}

	// 2) 회사 기준
	// 고른 회사 총 수량
	public int countCompany(int company) throws SQLException {
		return pfilteringDao.selectCountCompany(company);
	}

	// 고른 회사 상품 간단히 보기
	public List<ProductList> showCompany(PagingVo pvo, int company) throws SQLException {
		return pfilteringDao.selectShowCompany(pvo, company);
	}

	// 3) 성별 기준
	public int countSex(String sex) throws SQLException {
		return pfilteringDao.selectCountSex(sex);
	}

	public List<ProductList> showSex(PagingVo pvo, String sex) throws SQLException {
		return pfilteringDao.selectShowSex(pvo, sex);
	}

	// 4) 가격 기준
	public List<ProductList> showprice(PagingVo pvo, String price) throws SQLException {
		return pfilteringDao.selectShowPrice(pvo, price);
	}

	// **********************************************************
	// 상품 상세보기
	public Product showOneProduct(int pid) throws SQLException {
		return productDao.selectProductOne(pid);

	}

	// 관리자 버전
	// 상품 등록하기
	//트랜잭션 처리
	public String registerProduct(RegisterProduct ap) throws  IOException {
		Connection conn = ConnectionProvider.getConnection();

		
		int pid;
		String message ="";
		try {
			//자동 커밋 기능 끔
			conn.setAutoCommit(false);
			// 1) 상품 테이블 먼저 채우고 -> pid 리턴하기
			pid = productDao.insertProduct(conn, ap);
			System.out.println("pid"+pid);
			//2) 객체에 담긴 컬러와 사이즈 리스트
			List<Integer> colorList = ap.getColorList();
			List<Integer> sizeList = ap.getSizeList();
			
			// 3)트랜잭션 검사
			productDao.selectCheckColor(conn, colorList);
			productDao.selectCheckSize(conn, sizeList);

			
			conn.commit();
			
			// 트랜잭션 통과되면 컬러 테이블 채우기 -> 사이즈 테이블 채우기
			int result1 = productAndColorDao.insertProductColors(pid, colorList);

			int result2 = productAndSizeDao.insertProductSizes(pid, sizeList);
			
			if(result1+result2 !=2) {
				message = "상품 등록이 실패하였습니다";
			}
			
//			conn.commit();
			 message = "상품 등록이 완료되었습니다.";
		}catch (RuntimeException | SQLException e) {
			try {
				System.out.println(e.getMessage());
				message = "상품 등록 실패되었습니다.";
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {}
		} 
		
		return message;

	}

	// 상품 삭제(관리자)
	// method: 상품 삭제(Only 관리자)
	public String deleteProduct(int num) throws SQLException {
		String result = "";
		if (productDao.deleteProduct(num)) {
			result = "상품이 삭제되었습니다.";
		} else
			result = "상품 삭제에 실패했습니다.";
		return result;
	}

}
