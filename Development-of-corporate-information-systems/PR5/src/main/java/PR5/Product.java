package PR5;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
 * Class representing food product.
 * Fields must have certain restrictions to avoid
 * certain errors when saving product to a database.
 */
@Component("product")
@Scope("prototype")
public class Product {
	
	private int id;
	
	@NotNull(message = "this field cannot be empty")
	@Size(min=2, max=30, message= "length must be 2-30 characters long")
	private String item;
	
	@NotNull(message = "this field cannot be empty")
	@Size(min=2, max=30, message= "length must be 2-30 characters long")
	private String type;
	
	@NotNull(message = "this field cannot be empty")
	@Size(min=2, max=30, message= "length must be 2-30 characters long")
	private String producer;
	
	@PositiveOrZero(message = "price cannot be negative")
	private double price;
	
	@Positive(message = "weight must be a positive number")
	private int weight;
	
	public Product() {}
	
	public Product(String item, String type, String producer, double price, int weight) {
		this.item = item;
		this.type = type;
		this.producer = producer;
		this.price = price;
		this.weight = weight;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getItem() {
		return this.item;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getProducer() {
		return this.producer;
	}
	
	public void setProducer(String producer) {
		this.producer = producer;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public String getFormattedPrice() {
		return String.format("%.02f", this.price);
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	/*
	 * Sets values on a product object from a ResultSet
	 * returned by a database.
	 */
	public void setValues (ResultSet res) {
		try {
			int id = res.getInt("id");
			String item = res.getString("item");
			String type = res.getString("type");
			String producer = res.getString("producer");
			double price = res.getDouble("price");
			int weight = res.getInt("weight");
			this.id = id;
			this.item = item;
			this.type = type;
			this.producer = producer;
			this.price = price;
			this.weight = weight;
		} catch (SQLException e) {
			System.out.print("\nDB Error, product object was not changed\n");
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "\nID: " + this.id + "\nItem: " + this.item + "\nType: "
				+ this.type + "\nProducer: " + this.producer + "\nPrice: "
				+ this.price + "\nWeight: " + this.weight;
	}
}
