package PR7;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jakarta.validation.Valid;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * Product controller responsible for interacting between
 * a database and a view. Accepts GET and POST HTTP methods,
 * validates form data, makes requests to database,
 * returns appropriate views.
 */
@Controller
public class ProductController {
	private static ArrayList<Product> products = new ArrayList<Product>();
	public static AnnotationConfigApplicationContext context = 
			new AnnotationConfigApplicationContext(Main.class);
    private static ProductDAO db = context.getBean("DB", ProductDAO.class);
    
    @GetMapping("/products/")
    public String home() {
        return "home";
    }
    
    @GetMapping("products/all")
    public String all(Model model) {
    	ResultSet res = db.getAll();
    	products.clear();
		try {
			while (res.next()) {
				Product product = context.getBean("product", Product.class);
				product.setValues(res);
				products.add(product);
			}
		} catch(SQLException e) {
			e.printStackTrace();
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
    
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult br) {
    	if (br.hasErrors()) {
    		return "add";
    	}
    	db.create(product);
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
				return "redirect:/products/edit/" + product.getId();
			} else {
				return "notfound";
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return "/products";
		}
    }
    
    @GetMapping("/products/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
    	ResultSet res = db.get(id);
    	try {
			if (res.next()) {
				Product product = context.getBean("product", Product.class);
				product.setValues(res);
				model.addAttribute("product", product);
				return "edit";
			} else {
				return "notfound";
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return "redirect:/products";
		}
    }
    
    @PostMapping("/products/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult br) {
    	if (br.hasErrors()) {
    		return "edit";
    	}
    	db.edit(product);
    	return "redirect:/products/all";
    }
    
    @GetMapping("/products/delete")
    public String delete(Model model) {
    	Product product = context.getBean("product", Product.class);
    	model.addAttribute("product", product);
        return "delete";
    }
    
    @PostMapping("/products/delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
    	int rows = db.delete(product.getId());
    	if (rows == 0) {
    		return "notfound";
    	}
        return "redirect:/products/all";
    }
    
    @GetMapping("/products/filter")
    public String filterPage(Model model) {
    	PriceRange priceRange = context.getBean("priceRange", PriceRange.class);
    	model.addAttribute("priceRange", priceRange);
        return "filter";
    }
    
    @PostMapping("/products/filter")
    public String filterPrice(@ModelAttribute("priceRange") @Valid PriceRange range, BindingResult br) {
    	if (br.hasErrors()) {
    		return "filter";
    	}
        return String.format("redirect:/products/filterPrice?min=%s&max=%s",
        		range.getMinPriceF(), range.getMaxPriceF());
    }
    
    @GetMapping("/products/filterPrice")
    public String filteredProducts(@RequestParam("min") double min,
    		@RequestParam("max") double max, Model model) {
    	PriceRange priceRange = context.getBean("priceRange", PriceRange.class);
    	priceRange.setMinPrice(min);
    	priceRange.setMaxPrice(max);
    	ResultSet res = db.filterByPrice(priceRange);
    	products.clear();
		try {
			while (res.next()) {
				Product product = context.getBean("product", Product.class);
				product.setValues(res);
				products.add(product);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
    	model.addAttribute("products", products);
    	model.addAttribute("priceRange", priceRange);
        return "filteredPrice";
    }
}