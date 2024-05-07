package PR6;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Component("user")
@Scope("prototype")
public class User {

    @NotNull
    @Size(min = 2, max = 30, message = "length must be 2-30 characters long")
    private String name;

    @NotNull
    @Size(min = 2, max = 30, message = "length must be 2-30 characters long")
    private String password;

    private String role;

    public User(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.role = "user";
    }

    public User() {
        this.role = "user";
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getRole() {
        return this.role;
    }

    public void setValues (ResultSet res) {
		try {
			String name = res.getString("name");
			String password = res.getString("password");
            String role = res.getString("role");
			this.name = name;
			this.password = password;
			this.role = role;
		} catch (SQLException e) {
			System.out.print("\nDB Error, product object was not changed\n");
			e.printStackTrace();
		}
	}
}
