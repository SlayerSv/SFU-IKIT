package PR7;

import java.util.ArrayList;
import java.util.Arrays;

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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/*
 * Product controller responsible for interacting between
 * a database and a view. Accepts GET and POST HTTP methods,
 * validates form data, makes requests to database,
 * returns appropriate views.
 */
@Controller
public class ProductController {
	public static AnnotationConfigApplicationContext context = 
			new AnnotationConfigApplicationContext(Main.class);
    private static ProductDAO db = context.getBean("DB", ProductDAO.class);
	private static String URLprefix = "http://localhost:8080/api/products";
	private static RestTemplate restTemplate = new RestTemplate();
    
    @GetMapping("/products/")
    public String home() {
        return "home";
    }
    
    @GetMapping("products/all")
    public String all(Model model) {
		Product[] products = restTemplate.getForObject(URLprefix + "/all", Product[].class);
    	model.addAttribute("products", Arrays.asList(products));
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
    	restTemplate.postForObject(URLprefix, product, Product.class);
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
		try {
    		Product pr = restTemplate.getForObject(URLprefix + "/" + product.getId(),
					Product.class, product.getId());
			return "redirect:/products/edit/" + pr.getId();
		} catch (HttpClientErrorException e) {
			return "notfound";
		}
    }
    
    @GetMapping("/products/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
    	Product product = restTemplate.getForObject(URLprefix + "/{id}", Product.class, id);
			if (product != null) {
				model.addAttribute("product", product);
				return "edit";
			} else {
				return "notfound";
			}
		}
    
    @PostMapping("/products/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult br) {
    	if (br.hasErrors()) {
    		return "edit";
    	}
		restTemplate.put(URLprefix, product, product.getId());
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
		try {
			restTemplate.delete(URLprefix + "/" + product.getId());
			return "redirect:/products/all";
		} catch (HttpClientErrorException e) {
			return "notfound";
		}
    	
        
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
        		range.getMinPrice(), range.getMaxPrice());
    }
    
    @GetMapping("/products/filterPrice")
    public String filteredProducts(@RequestParam("min") double min,
    		@RequestParam("max") double max, Model model) {

    	PriceRange priceRange = context.getBean("priceRange", PriceRange.class);
    	priceRange.setMinPrice(min);
    	priceRange.setMaxPrice(max);
    	ArrayList<Product> products = db.filterByPrice(priceRange);
    	model.addAttribute("products", products);
    	model.addAttribute("priceRange", priceRange);
        return "filteredPrice";
    }
}