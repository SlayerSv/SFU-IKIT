package PR7;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.ArrayList;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    public static AnnotationConfigApplicationContext context = 
			new AnnotationConfigApplicationContext(Main.class);
    private static ProductDAO db = context.getBean("DB", ProductDAO.class);

    @GetMapping("/all")
    public ArrayList<Product> getAll() {
        ArrayList<Product> products = db.getAll();
        return products;
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable("id") int id) {
        Product product = db.get(id);
        if (product != null) {
            return product;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping()
    public Product edit(@RequestBody @Valid Product product) {
        Product pr = db.edit(product);
        if (pr != null) {
            return pr;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody @Valid Product product) {
        Product pr = db.create(product);
        if (pr != null) {
            return pr;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public Product delete(@PathVariable("id") int id) {
        Product pr = db.delete(id);
        if (pr != null) {
            return pr;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
