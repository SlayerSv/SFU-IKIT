package PR5SB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

/*
 * Database Access Object to interact with a database.
 * Makes different kind of requests to a database and
 * returns ResultSet as a response.
 */
@Component("DB")
public class ProductDAO {
	private static final String URL = "jdbc:postgresql://localhost:5432/products";
	private static final String user = "postgres";
	private static final String pass = "postgres";
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
			String query = "SELECT * FROM \"products\" ORDER BY id desc";
			return statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nError getting products");
			return null;
		}
	}
	
	public ResultSet get(int id) {
		try {
			String query = "SELECT * FROM \"products\" where id = ?";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setInt(1, id);
			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nError getting product");
			return null;
		}
	}
	
	/*
	 * Creates a new product in a database with a database returning
	 * data about created product, then returns ResultSet with that data.
	 */
	public ResultSet create(Product product) {
		try {
			String query = "INSERT INTO \"products\"(item, type, producer, price, weight)"
					+ "VALUES (?, ?, ?, ?, ?)"
					+ "RETURNING *;";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setString(1, product.getItem());
			statement.setString(2, product.getItem());
			statement.setString(3, product.getProducer());
			statement.setDouble(4, product.getPrice());
			statement.setInt(5, product.getWeight());
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
	public ResultSet edit(Product product) {
		try {
			String query = "UPDATE \"products\" SET (item, type, producer, price, weight) = (?,?,?,?,?)"
					+ " where id = ?"
					+ " RETURNING *;";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setString(1, product.getItem());
			statement.setString(2, product.getType());
			statement.setString(3, product.getProducer());
			statement.setDouble(4, product.getPrice());
			statement.setInt(5, product.getWeight());
			statement.setInt(6, product.getId());
			return statement.executeQuery();
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet filterByPrice(PriceRange priceRange) {
		try {
			String query = "SELECT * FROM \"products\" where price >= ? and price <= ? order by price";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setDouble(1, priceRange.getMinPrice());
			statement.setDouble(2, priceRange.getMaxPrice());
			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nError filtering products");
			return null;
		}
	}

	/*
	 * Deletes a product from a database by provided ID and
	 * returns number of affected database rows.
	 */
	public int delete(int id) {
		String query = "DELETE FROM \"products\" WHERE ID = ?";
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
