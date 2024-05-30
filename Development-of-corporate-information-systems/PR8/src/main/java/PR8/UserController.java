package PR8;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;


@Controller
public class UserController {

    @Autowired
    private PasswordEncoder encoder;

    private static AnnotationConfigApplicationContext context = 
			new AnnotationConfigApplicationContext(Main.class);

    private static UserDAO db = context.getBean("userDB", UserDAO.class);

    @GetMapping("/users/signup")
    public String signup(Model model) {
        User user = context.getBean("user", User.class);
    	model.addAttribute("user", user);
        return "signup";
    }

    @PostMapping("/users/signup")
    public String signup(@ModelAttribute("user") @Valid User user, BindingResult br) {
        if (br.hasErrors()) {
    		return "signup";
    	}
        ResultSet rs = db.get(user.getName());
        try {
            if (rs.next()) {
                return "redirect:/users/signup?exists";
            }
        } catch(SQLException e) {
			e.printStackTrace();
			return "redirect:/users/signup?error";
		}
        user.setPassword(encoder.encode(user.getPassword()));
        db.create(user);
        return "redirect:/users/login?created";
    }
    
    @GetMapping("/users/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users/logout")
    public String logout() {
        return "logout";
    }
}
