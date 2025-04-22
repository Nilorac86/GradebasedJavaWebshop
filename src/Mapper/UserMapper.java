package Mapper;

import User.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserMapper < T extends User> {
    T map (ResultSet rs) throws SQLException;
}
