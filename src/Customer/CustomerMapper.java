package Customer;

import Mapper.UserMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements UserMapper<Customer> {

    @Override
    public Customer map(ResultSet rs) throws SQLException {
        return new Customer(rs.getInt("customer_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone"),
                rs.getString("address"));
    }
}
