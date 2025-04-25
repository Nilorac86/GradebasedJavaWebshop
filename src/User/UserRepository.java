package User;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Mapper.UserMapper;



public class UserRepository {

    public static final String URL = "jdbc:sqlite:webbutiken.db";

    public <T extends User> T userLogin(String tableName, String loginField, String loginValue, String password, UserMapper<T> mapper) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + loginField+ " = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loginValue.toLowerCase());
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                    return mapper.map(rs);
            }

        } catch (SQLException e) {
            System.err.println("Fel vid körning av inloggningsfrågan: " + e.getMessage());
        }

        return null;
    }
}
