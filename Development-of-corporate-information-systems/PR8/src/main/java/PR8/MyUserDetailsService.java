package PR8;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private static AnnotationConfigApplicationContext context = 
			new AnnotationConfigApplicationContext(Main.class);
    private static UserDAO db = context.getBean("userDB", UserDAO.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = context.getBean("user", User.class);
        ResultSet rs = db.get(username);
        try {
            if (rs.next()) {
                user.setValues(rs);
            } else {
                throw new UsernameNotFoundException(username);
            }
        } catch(SQLException e) {
			e.printStackTrace();
            throw new UsernameNotFoundException(username);
		}
        return new MyUserPrincipal(user);
    }
}
