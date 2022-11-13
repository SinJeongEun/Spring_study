package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.Color;
import provider.ConnectionProvider;

public class ColorDao {
	private static ColorDao colorDao = new ColorDao();
	private ColorDao() {
	}
	public static ColorDao getInstance() {
		return colorDao;
	}
	
	// 색상 번호로 색상을 찾고 객체로 반환
	public Color selectColor(int colorId) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		
		String sql = "select color_id, color_name from color where color_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,  colorId);
		
		ResultSet rs = pstmt.executeQuery();
		Color cl = null;
		while(rs.next()) {
			cl = new Color(rs.getInt("color_id"), rs.getString("color_name"));

	    }
		pstmt.close();
		conn.close();
		return cl;
	}
}
