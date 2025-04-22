package Admin;

import Mapper.UserMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMapper implements UserMapper<Admin> {

    @Override
    public Admin map(ResultSet rs) throws SQLException {
        return new Admin(rs.getInt("admin_id"),
                rs.getString("username"),
                rs.getString("password"));
    }
}
