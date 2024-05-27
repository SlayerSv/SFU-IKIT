package PR7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
	public static AnnotationConfigApplicationContext context = 
			new AnnotationConfigApplicationContext(Main.class);
	
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
	public ArrayList<Product> getAll() {
		try {
			Statement statement = c.createStatement();
			String query = "SELECT * FROM \"products\" ORDER BY id desc";
			ResultSet rs = statement.executeQuery(query);
			return extract(rs);
		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<Product>();
		}
	}
	
	public Product get(int id) {
		try {
			String query = "SELECT * FROM \"products\" where id = ?";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			ArrayList<Product> products = extract(rs);
			if (products.size() > 0) {
				return products.get(0);
			} else {
				return null;
			}
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
	public Product create(Product product) {
		try {
			String query = "INSERT INTO \"products\"(item, type, producer, price, weight)"
					+ "VALUES (?, ?, ?, ?, ?)"
					+ "RETURNING *;";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setString(1, product.getItem());
			statement.setString(2, product.getType());
			statement.setString(3, product.getProducer());
			statement.setDouble(4, product.getPrice());
			statement.setInt(5, product.getWeight());
			ResultSet rs = statement.executeQuery();
			ArrayList<Product> products = extract(rs);
			if (products.isEmpty()) {
				return null;
			} else {
				return products.get(0);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * Edits a product in a database and
	 * returns ResultSet with modified data.
	 */
	public Product edit(Product product) {
		try {
			String query = "UPDATE \"products\" SET (item, type, producer, price, weight) = (?,?,?,?,?)"
					+ " where id = ? RETURNING *;";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setString(1, product.getItem());
			statement.setString(2, product.getType());
			statement.setString(3, product.getProducer());
			statement.setDouble(4, product.getPrice());
			statement.setInt(5, product.getWeight());
			statement.setInt(6, product.getId());
			ResultSet rs = statement.executeQuery();
			return extractOne(rs);
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Product> filterByPrice(PriceRange priceRange) {
		try {
			String query = "SELECT * FROM \"products\" where price >= ? and price <= ? order by price";
			PreparedStatement statement = c.prepareStatement(query);
			statement.setDouble(1, priceRange.getMinPrice());
			statement.setDouble(2, priceRange.getMaxPrice());
			return extract(statement.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nError filtering products");
			return new ArrayList<Product>();
		}
	}

	/*
	 * Deletes a product from a database by provided ID.
	 */
	public Product delete(int id) {
		String query = "DELETE FROM \"products\" WHERE ID = ? RETURNING *";
		try {
			PreparedStatement statement = c.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			return extractOne(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Product> extract(ResultSet rs) {
		ArrayList<Product> products = new ArrayList<>();
		try {
			while (rs.next()) {
				Product product = context.getBean("product", Product.class);
				product.setValues(rs);
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return products;
		}
		return products;
	}

	public Product extractOne(ResultSet rs) {
		ArrayList<Product> products = extract(rs);
		if (!products.isEmpty()) {
			return products.get(0);
		} else {
			return null;
		}
	}
}
