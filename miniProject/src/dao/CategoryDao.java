package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import provider.ConnectionProvider;

public class CategoryDao {
	
	private static CategoryDao categoryDao = new CategoryDao();
	private CategoryDao() {
	}
	public static CategoryDao getInstance() {
		return categoryDao;
	}

	public String findCategoty(int categoryId) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		PreparedStatement pstmt= null;
		String sql = "select category_kind from category where category_id=?";
		String category = "";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, categoryId);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				category = rs.getString("category_kind");
				conn.close();
			} else return "없음";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally {
			pstmt.close();
			conn.close();
		}
		return category;

	}

}
