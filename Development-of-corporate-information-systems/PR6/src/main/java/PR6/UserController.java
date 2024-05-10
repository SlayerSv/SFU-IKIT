package PR6;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;


@Controller
public class UserController {

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
        db.create(user);
        authenticate(user);
        return "home";
    }
    
    @GetMapping("/users/login")
    public String login(Model model) {
        User user = context.getBean("user", User.class);
    	model.addAttribute("user", user);
        return "login";
    }

    @PostMapping("/users/login")
    public String login(@ModelAttribute("user") @Valid User user, BindingResult br) {
        if (br.hasErrors()) {
    		return "login";
    	}
        ResultSet rs = db.get(user.getName());
        try {
            if (rs.next()) {
                user.setValues(rs);
                authenticate(user);
            } else {
                return "notfound";
            }
        } catch(SQLException e) {
			e.printStackTrace();
			return "home";
		}
        
        return "home";
    }

    public void authenticate(User user) {
        System.out.println("authenticated user: " + user.toString());
    }
}
