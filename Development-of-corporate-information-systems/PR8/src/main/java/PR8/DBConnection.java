package PR8;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
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

    public static Connection getConnection() {
        return DBConnection.c;
    }
}
