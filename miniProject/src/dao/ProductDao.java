package dao;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.dto.PagingVo;
import controller.dto.ProductList;
import controller.dto.RegisterProduct;
import domain.Product;
import provider.ConnectionProvider;
import service.ProductAndColorService;
import service.ProductAndSizeService;

public class ProductDao {
	private static ProductDao productDao = new ProductDao();

	private ProductDao() {
	}

	public static ProductDao getInstance() {
		return productDao;
	}

	ProductAndSizeService productAndSizeService = ProductAndSizeService.getInstance();
	ProductAndColorService productAndColorService = ProductAndColorService.getInstance();
	CategoryDao categoryDao = CategoryDao.getInstance();

	// 상품의 총수량
	public int selectCountAll() throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "select count(product_id) from product where is_deleted = '0'";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();

		int cnt = 0;
		if (rs.next()) {
			cnt = rs.getInt(1);
		}
		pstmt.close();
		conn.close();
		return cnt;
	}

	// 모든 상품 목록(간단히 보기)
	public List<ProductList> selectProductList(PagingVo pvo) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		ProductList pdl = null;

		int endRn = pvo.getEndRowNo(); // 페이지의 끝행 번호
		int startRn = pvo.getStartRowNo();// 페이지의 시작 행 번호
		System.out.println(endRn);
		System.out.println(startRn);

		String sql = "select rm, product_id, product_name, company_id, category_id, product_sex, product_price  "
				+ "from( "
				+ "select rownum as rm, product_id, product_name, company_id, category_id, product_sex, product_price "
				+ "from (" + "select  product_id, product_name, company_id, category_id, product_sex, product_price "
				+ "from product where is_deleted = '0' " + "order by product_id" + ")" + " where rownum <= ? ) " + "where rm >= ?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, endRn);
		pstmt.setInt(2, startRn);

		ResultSet rs = pstmt.executeQuery();
		List<ProductList> list = new ArrayList<>();

		while (rs.next()) {

			pdl = new ProductList(rs.getInt("product_id"), rs.getString("product_name"),
					selectFindCompany(rs.getInt("company_id")), selectFindCategory(rs.getInt("category_id")),
					rs.getString("product_sex"), rs.getInt("product_price"));
			list.add(pdl);
		}
		pstmt.close();
		conn.close();
		return list;
	}

	// companyId로 회사명 가져오기
	public String selectFindCompany(int companyId) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();

		String sql = "select company_name from company where company_id=? ";
		String cname = "";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, companyId);

		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			cname = rs.getString("company_name");
		}
		conn.close();
		pstmt.close();
		return cname;
	}

	// 아이디로 카테고리 명 가져오기
	public String selectFindCategory(int category) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();

		String sql = "select category_kind from category where category_id=?";
		String cname = "";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, category);

		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			cname = rs.getString("category_kind");

		}
		conn.close();
		pstmt.close();
		return cname;
	}

	
	// 상품하나만 가져오기 정은
		public Product selectProductOne(int productId) throws SQLException {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = null;
			Product p = null;
			try {
				String sql = "select product_id, product_name, product_price, category_id, company_id, product_sex, is_deleted from product where product_id=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, productId);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					int pid = rs.getInt("product_id");

					p = new Product(pid, rs.getString("product_name"), rs.getBoolean("is_deleted"),rs.getInt("product_price"),
							rs.getString("product_sex"), selectFindCompany(rs.getInt("company_id")),
							categoryDao.findCategoty(rs.getInt("category_id")),
							productAndColorService.findProductColors(pid), productAndSizeService.findProductSizeList(pid));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			pstmt.close();
			conn.close();
			return p;

		}

	

	// 트랜잭션
	public void selectCheckColor(Connection conn, List<Integer> colorList) {
		PreparedStatement pstmt = null;
		System.out.println("셀렉트체크");
		for (Integer i : colorList) {
			String sql = "select color_id from color where color_id=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, i);
				ResultSet rs = pstmt.executeQuery();

				if (!rs.next()) {
					throw new RuntimeException("컬러 없는 상품입니다.");
				}
			} catch (SQLException e) {
			} finally {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

	
	}

	public void selectCheckSize(Connection conn, List<Integer> sizeList) throws SQLException {
		PreparedStatement pstmt = null;

		for (Integer i : sizeList) {
			String sql = "select size_id from ssize where size_id=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, i);
				ResultSet rs = pstmt.executeQuery();

				if (!rs.next()) {
					throw new RuntimeException("사이즈 없는 상품입니다.");
				}
			} catch (SQLException e) {
			} finally {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}

		}

	}

	// 상품 등록하기
	public int insertProduct(Connection conn, RegisterProduct ap) throws SQLException {
		int pid = 0;
		String sql = "insert into product" + "(product_id, product_name, product_price, category_id, company_id , "
				+ "product_sex) values (product_seq.nextval,?,?,?,?,?)";
		// pstmt = conn.prepareStatement(sql, new String[] { "product_id" });
		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, ap.getProductName());
		pstmt.setInt(2, ap.getProductPrice());
		pstmt.setInt(3, ap.getCategory());
		pstmt.setInt(4, ap.getCompany());
		pstmt.setString(5, ap.getProductSex());
		int r = pstmt.executeUpdate();

		if (r == 1) {
			// ResultSet rs = pstmt.getGeneratedKeys();
			sql = "select product_id from product where product_name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ap.getProductName());
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				pid = rs.getInt("product_id");
				// pid = rs.getInt(1);
			}
			System.out.println(pid);
		}
		pstmt.close();
		return pid;

	}

	// 상품 삭제하기
	public boolean deleteProduct(int pId) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "update product set is_deleted = '1' where product_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, pId);
		int rows = pstmt.executeUpdate();

		boolean result;
		if (rows == 1) {
			result = true;
		} else
			result = false;

		conn.close();
		pstmt.close();
		return result;
	}

}
