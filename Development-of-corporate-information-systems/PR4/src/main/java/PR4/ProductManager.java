package PR4;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Manages ArrayList of different Products, can create, edit, delete products
 * from the list.
 * Interacts with the DAO to exchange data between itself and a database.
 */
public class ProductManager {
	private static ArrayList<Product> products = new ArrayList<Product>();
    public static AnnotationConfigApplicationContext context = 
			new AnnotationConfigApplicationContext(SpringConfig.class);
    private static ProductDAO db = context.getBean("DB", ProductDAO.class);
    
    public static void loadData() {
    	ResultSet res = db.getAll();
    	if (res == null) {
    		System.out.print("\nERROR: Result set was not returned\n");
    		return;
    	}
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
	}
    
    /*
     * Creates a new product, adds it to a database and to a local cache.
     * Accepts input from the user, then sends data to DAO, so it can
     * make a request to a database, then accepts ResultSet as a return value
     * and creates a product object, based on the data in that ResultSet.
     */
    public static void addNewProduct() {
    	String item = Validator.validateString("\nEnter product item name: ");
		String type = Validator.validateString("Enter product item type: ");
		String producer = Validator.validateString("Enter product's producer name: ");
		int price = Validator.validateNumber(0, "Enter product's price: ");
		int weight = Validator.validateNumber(0, "Enter product's weight: ");

		ResultSet res = db.create(item, type, producer, price, weight);
		if (res == null) {
			System.out.print("\nERROR: Result set was not returned, data reloaded.\n");
			loadData();
    		return;
		}
		try {
			if (res.next()) {
				Product product = context.getBean("product", Product.class);
				product.setValues(res);
				products.add(product);
				System.out.println("\nCurrent product was added sucessfully:\n"
						+ product.toString());
			} else {
				System.out.print("\nERROR: Database did not return any values.\n"
						+ "Reloading data, check if the product was added");
				loadData();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nError during execution,data reloaded");
			loadData();
		}
    }
    
    /*
     * Edits a product, updating information in  a database and in a local cache.
     * Accepts input from the user, then sends data to DAO, so it can
     * make a request to a database, then accepts ResultSet as a return value
     * and edits a product object, based on the data in that ResultSet.
     */
    public static void editProduct() {
    	if (products.size() < 1) {
            System.out.print("\nCan't edit, because there are no products in the list!\n");
            return;
        }
    	printProducts();
    	int id = Validator.validateNumber(0, "\nEnter product's ID you want to edit: ");
    	Product product = null;
    	for (int i = 0; i < products.size(); i++) {
    		if (products.get(i).getId() == id) product = products.get(i);
    	}
    	if (product == null) {
    		System.out.println("\nNo product with ID " + id);
    		return;
    	}
    	String item = Validator.validateString("Enter new product item name: ");
		String type = Validator.validateString("Enter new product item type: ");
		String producer = Validator.validateString("Enter new product's producer name: ");
		int price = Validator.validateNumber(0, "Enter new product's price: ");
		int weight = Validator.validateNumber(0, "Enter new product's weight: ");
		ResultSet res = db.edit(id, item, type, producer, price, weight);
		if (res == null) {
			System.out.print("\nERROR: Result set was not returned, reloading data\n");
			loadData();
    		return;
		}
		try {
			if (res.next()) {
				product.setValues(res);
				System.out.println("\nProduct was edited sucessfully:\n" + product.toString());
			} else {
				System.out.print("\nERROR: Database did not return any values.\n"
						+ "Reloading data, check if the product was edited");
				loadData();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nError during execution, data reloaded");
			loadData();
		}
    }
    
    /*
     * Deletes a product from a database and from a local cache.
     * Accepts ID from the user, then sends data to DAO, so it can
     * make a request to a database, then accepts integer as a return value
     * indicating whether or not operation completed.
     */
    public static void deleteProduct() {
    	if (products.size() < 1) {
            System.out.print("\nCan't delete, because there are no products in the list!\n");
            return;
        }
    	printProducts();
    	int id = Validator.validateNumber(0, "Enter ID of the product you want to delete: ");
    	int index = -1;
    	for (int i = 0; i < products.size(); i++) {
    		if (products.get(i).getId() == id) index = i;
    	}
    	if (index < 0) {
    		System.out.println("\nNo product with ID " + id);
    		return;
    	}
    	if (db.delete(id) == 0) {
    		System.out.println("\nDB did not confirm deletion, reloading data");
    		loadData();
    		return;
    	}
    	products.remove(index);
    	System.out.println("\nProduct was sucessfully deleted.");
    }
    
    /*
     * Filters products by price, using data from local cache.
     * Accepts input from the user, then loops over products ArrayList
     * in prints products with requested price.
     */
    public static void filterByPrice() {
    	if (products.size() < 1) {
            System.out.print("\nCan't filter, because there are no products in the list!\n");
            return;
        }
    	int low = Validator.validateNumber(0, "Enter min price: ");
    	int high = Validator.validateNumber(low, "Enter max price: ");
    	int count = 0;
    	for (int i = 0; i < products.size(); i++) {
    		Product product = products.get(i);
    		if (product.getPrice() >= low && product.getPrice() <= high) {
    			System.out.println(product.toString());
    			count++;
    		}	  
        }
    	if (count == 0) {
    		System.out.println("\nNo products with a price in the range " + low + " - " + high);
    	} else {
    		System.out.println("\nNumber of found products: " + count);
    	}
    }
    
    /**
     * Prints information about all products.
     * Uses toString() method to display information.
     */
    public static void printProducts() {
        if (products.size() < 1) {
            System.out.print("\nNo products in the list!\n");
            return;
        }
        for (int i = 0; i < products.size(); i++) {
            System.out.println(products.get(i).toString());
        }
    }
}