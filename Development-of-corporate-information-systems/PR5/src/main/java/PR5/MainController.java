package PR5;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
	private static ArrayList<Product> products = new ArrayList<Product>();
	public static AnnotationConfigApplicationContext context = 
			new AnnotationConfigApplicationContext(SpringConfig.class);
    private static ProductDAO db = context.getBean("DB", ProductDAO.class);
    
    @GetMapping("/products")
    public String home() {
        return "home";
    }
    
    @GetMapping("/products/all")
    public String all(Model model) {
    	ResultSet res = db.getAll();
    	products.clear();
		try {
			while (res.next()) {
				Product product = context.getBean("product", Product.class);
				product.setValues(res);
				products.add(product);
			}
	        System.out.print("\nData loaded sucessfully\n");
		} catch(SQLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < products.size(); i++) {
			System.out.println(products.get(i).getItem());
		}
    	model.addAttribute("products", products);
        return "all";
    }
    
    @GetMapping("/products/add")
    public String addForm(Model model) {
    	Product product = context.getBean("product", Product.class);
    	model.addAttribute("product", product);
        return "add";
    }
    
    @GetMapping("/products/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
    	ResultSet res = db.get(id);
    	try {
			if (res.next()) {
				Product product = context.getBean("product", Product.class);
				product.setValues(res);
				model.addAttribute("product", product);
				return "/products/edit";
			} else {
				return "redirect:/notfound";
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return "redirect:/products";
		}
    }
    
    @PostMapping("/products/edit/{id}")
    public String editProduct(@ModelAttribute("product") Product product) {
    	db.edit(product);
    	return "redirect:/products/all";
    }
    
    @GetMapping("/products/edit")
    public String search(Model model) {
    	Product product = context.getBean("product", Product.class);
    	model.addAttribute("product", product);
    	return "search";
    }
    
    @PostMapping("/products/edit")
    public String find(@ModelAttribute("product") Product product) {
    	ResultSet res = db.get(product.getId());
    	try {
			if (res.next()) {
				return "/products/edit/" + product.getId();
			} else {
				return "/notfound";
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return "/products";
		}
    	
    }
    
    @GetMapping("/products/delete")
    public String delete() {
        return "delete";
    }
    
    @GetMapping("/products/filter")
    public String filter() {
        return "filter";
    }
    
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("product") Product product) {
    	db.create(product);
    	return "redirect:/products/all";
    }
}