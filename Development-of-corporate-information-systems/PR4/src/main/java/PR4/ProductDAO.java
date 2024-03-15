package PR4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

/*
 * Database Access Object to interact with a database.
 */
@Component("DB")
public class ProductDAO {
	private static final String URL = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/hjwvxjom";
	private static final String user = "hjwvxjom";
	private static final String pass = "96PjVVCZjw-WHcw5kFXBucHOoo9SHOYw";
	private static Connection c;
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
		try {
			c = DriverManager.getConnection(URL, user, pass);
		} catch (SQLException e){
			e.printStackTrace();
		} 
	}
	
	/*
	 * Requests all products data from a database and
	 * returns ResultSet with that data.
	 */
	public ResultSet getAll() {
		try {
			Statement statement = c.createStatement();
			String query = "SELECT * FROM \"Product\" ORDER BY id";
			return statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nError getting products");
			return null;
		}
	}
	
	/*
	 * Creates a new product in a database with a database returning
	 * data about created product, then returns ResultSet with that data.
	 */
	public ResultSet create(
			String item,
			String type,
			String producer,
			int price,
			int weight
			) {
		try {
			String query = """
					INSERT INTO \"Product\"(item, type, producer, price, weight)
					VALUES (?, ?, ?, ?, ?)
					RETURNING *;
					""";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setString(1, item);
			statement.setString(2, type);
			statement.setString(3, producer);
			statement.setInt(4, price);
			statement.setInt(5, weight);
			return statement.executeQuery();
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * Edits a product in a database and
	 * returns ResultSet with modified data.
	 */
	public ResultSet edit(
			int id,
			String item,
			String type,
			String producer,
			int price,
			int weight
			) {
		try {
			String query = """
					UPDATE \"Product\" SET (item, type, producer, price, weight) = (?,?,?,?,?)
					where id = ?
					RETURNING *;
					""";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setString(1, item);
			statement.setString(2, type);
			statement.setString(3, producer);
			statement.setInt(4, price);
			statement.setInt(5, weight);
			statement.setInt(6, id);
			return statement.executeQuery();
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Deletes a product from a database by provided ID and
	 * returns number of affected database rows.
	 */
	public int delete(int id) {
		String query = "DELETE FROM \"Product\" WHERE ID = ?";
		try {
			PreparedStatement statement = c.prepareStatement(query);
			statement.setInt(1, id);
			int rows = statement.executeUpdate();
			return rows;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
