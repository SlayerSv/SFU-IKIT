package PR6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component("userDB")
public class UserDAO {

    Connection c = DBConnection.getConnection();

    public ResultSet get(String name) {
		try {
			String query = "SELECT * FROM \"users\" where name = ?";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setString(1, name);
			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nError getting user");
			return null;
		}
	}

    public ResultSet create(User user) {
		try {
			String query = "INSERT INTO \"users\"(name, password, role)"
					+ " VALUES (?, ?, ?)"
					+ " RETURNING *;";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
			return statement.executeQuery();
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
