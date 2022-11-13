package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.dto.PagingVo;
import controller.dto.ProductList;
import provider.ConnectionProvider;

public class PfilteringDao {
	//싱글톤
	private static PfilteringDao pfilteringDao = new PfilteringDao();
	private PfilteringDao() {
	}
	public static PfilteringDao getInstance() {
		return pfilteringDao;
	}

	// 1) 카테고리
	public int selectCountCategory(int cateId) throws SQLException {// 선택한 카테고리상품의 총수량
		Connection conn = ConnectionProvider.getConnection();
		String sql = "select count(product_id) from product  where category_id=? and is_deleted = '0'";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, cateId);
		ResultSet rs = pstmt.executeQuery();

		int cnt = 0;
		if (rs.next()) {
			cnt = rs.getInt(1);
		}
		pstmt.close();
		conn.close();
		return cnt;
	}

	public List<ProductList> selectShowCategory(PagingVo pvo, int category) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		ProductList pdl = null;

		int endRn = pvo.getEndRowNo(); // 페이지의 끝행 번호
		int startRn = pvo.getStartRowNo();// 페이지의 시작 행 번호
		System.out.println(endRn);
		System.out.println(startRn);

		String sql = "select rm, product_id, product_name, company_id, category_id, product_sex, product_price  "
				+ "from( "
				+ "select rownum as rm, product_id,product_name, company_id, category_id, product_sex, product_price "
				+ "from (" + "select  product_id, product_name, company_id, category_id, product_sex, product_price "
				+ "from product " + "where category_id = ? and is_deleted = '0' " + "order by product_id" + ")" + " where rownum <= ? ) "
				+ "where rm >= ?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, category);
		pstmt.setInt(2, endRn);
		pstmt.setInt(3, startRn);

		ResultSet rs = pstmt.executeQuery();
		List<ProductList> list = new ArrayList<>();

		while (rs.next()) {
			System.out.println("~~~~~~~");
			pdl = new ProductList(rs.getInt("product_id"), rs.getString("product_name"),
					selectFindCompany(rs.getInt("company_id")), selectFindCategory(rs.getInt("category_id")),
					rs.getString("product_sex"), rs.getInt("product_price"));
			list.add(pdl);
		}
		pstmt.close();
		conn.close();
		return list;
	}

	// 2) 회사 기준
	public int selectCountCompany(int company) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "select count(product_id) from product  where company_id=? and is_deleted = '0'";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, company);
		ResultSet rs = pstmt.executeQuery();

		int cnt = 0;
		if (rs.next()) {
			cnt = rs.getInt(1);
		}
		pstmt.close();
		conn.close();
		return cnt;
	}

	public List<ProductList> selectShowCompany(PagingVo pvo, int company) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		ProductList pdl = null;

		int endRn = pvo.getEndRowNo(); // 페이지의 끝행 번호
		int startRn = pvo.getStartRowNo();// 페이지의 시작 행 번호
		System.out.println(endRn);
		System.out.println(startRn);

		String sql = "select rm, product_id, product_name, company_id, category_id, product_sex, product_price  "
				+ "from( "
				+ "select rownum as rm, product_id,product_name, company_id, category_id, product_sex, product_price "
				+ "from (" + "select  product_id, product_name, company_id, category_id, product_sex, product_price "
				+ "from product " + "where company_id = ? and is_deleted = '0' " + "order by product_id" + ")" + " where rownum <= ? ) "
				+ "where rm >= ?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, company);
		pstmt.setInt(2, endRn);
		pstmt.setInt(3, startRn);

		ResultSet rs = pstmt.executeQuery();
		List<ProductList> list = new ArrayList<>();

		while (rs.next()) {
			System.out.println("~~~~~~~");
			pdl = new ProductList(rs.getInt("product_id"), rs.getString("product_name"),
					selectFindCompany(rs.getInt("company_id")), selectFindCategory(rs.getInt("category_id")),
					rs.getString("product_sex"), rs.getInt("product_price"));
			list.add(pdl);
		}
		pstmt.close();
		conn.close();
		return list;
	}

	// 3) 성별 기준
	public int selectCountSex(String sex) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "select count(product_id) from product  where product_sex=? and is_deleted = '0'";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, sex);
		ResultSet rs = pstmt.executeQuery();

		int cnt = 0;
		if (rs.next()) {
			cnt = rs.getInt(1);
		}
		pstmt.close();
		conn.close();
		return cnt;
	}

	public List<ProductList> selectShowSex(PagingVo pvo, String sex) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		ProductList pdl = null;

		int endRn = pvo.getEndRowNo(); // 페이지의 끝행 번호
		int startRn = pvo.getStartRowNo();// 페이지의 시작 행 번호
		System.out.println(endRn);
		System.out.println(startRn);

		String sql = "select rm, product_id, product_name, company_id, category_id, product_sex, product_price  "
				+ "from( "
				+ "select rownum as rm, product_id,product_name, company_id, category_id, product_sex, product_price "
				+ "from (" + "select  product_id, product_name, company_id, category_id, product_sex, product_price "
				+ "from product " + "where product_sex = ? and is_deleted = '0' " + "order by product_id" + ")" + " where rownum <= ? ) "
				+ "where rm >= ?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, sex);
		pstmt.setInt(2, endRn);
		pstmt.setInt(3, startRn);

		ResultSet rs = pstmt.executeQuery();
		List<ProductList> list = new ArrayList<>();

		while (rs.next()) {
			System.out.println("~~~~~~~");
			pdl = new ProductList(rs.getInt("product_id"), rs.getString("product_name"),
					selectFindCompany(rs.getInt("company_id")), selectFindCategory(rs.getInt("category_id")),
					rs.getString("product_sex"), rs.getInt("product_price"));
			list.add(pdl);
		}
		pstmt.close();
		conn.close();
		return list;
	}
	// 4) 가격 기준

	public List<ProductList> selectShowPrice(PagingVo pvo, String price) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		ProductList pdl = null;

		int endRn = pvo.getEndRowNo(); // 페이지의 끝행 번호
		int startRn = pvo.getStartRowNo();// 페이지의 시작 행 번호
		System.out.println(endRn);
		System.out.println(startRn);

		String sql = "";
		if (price.equals("asc")) {
			sql = "select rm, product_id, product_name, company_id, category_id, product_sex, product_price  "
					+ "from( "
					+ "select rownum as rm, product_id,product_name, company_id, category_id, product_sex, product_price "
					+ "from ("
					+ "select  product_id, product_name, company_id, category_id, product_sex, product_price "
					+ "from product where is_deleted = '0' " + "order by product_price " + ")" + " where rownum <= ? ) " + "where rm >= ?";
		} else {
			sql = "select rm, product_id, product_name, company_id, category_id, product_sex, product_price  "
					+ "from( "
					+ "select rownum as rm, product_id,product_name, company_id, category_id, product_sex, product_price "
					+ "from ("
					+ "select  product_id, product_name, company_id, category_id, product_sex, product_price "
					+ "from product where is_deleted = '0' " + "order by product_price desc " + ")" + " where rownum <= ? ) "
					+ "where rm >= ?";
		}

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, endRn);
		pstmt.setInt(2, startRn);

		ResultSet rs = pstmt.executeQuery();
		List<ProductList> list = new ArrayList<>();

		while (rs.next()) {
			System.out.println("~~~~~~~");
			pdl = new ProductList(rs.getInt("product_id"), rs.getString("product_name"),
					selectFindCompany(rs.getInt("company_id")), selectFindCategory(rs.getInt("category_id")),
					rs.getString("product_sex"), rs.getInt("product_price"));
			list.add(pdl);
		}
		pstmt.close();
		conn.close();
		return list;
	}

	// 회사 아이디를 입력하면 회사 이름으로 출력해줌
	public String selectFindCompany(int companyId) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();

		String sql = "select company_name from company where company_id=?";
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

}
