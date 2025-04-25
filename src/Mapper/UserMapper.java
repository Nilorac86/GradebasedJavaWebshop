package Mapper;

import User.User;

import java.sql.ResultSet;
import java.sql.SQLException;

// En mapper för att kontrollera vilken typ av användare.
public interface UserMapper < T extends User> {
    T map (ResultSet rs) throws SQLException;
}
