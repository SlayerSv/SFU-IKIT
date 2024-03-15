package PR4;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
 * Class representing food product.
 */
@Component
@Scope("prototype")
public class Product {
	private int id;
	private String item;
	private String type;
	private String producer;
	private int price;
	private int weight;
	
	public Product() {}
	
	public Product(String item, String type, String producer, int price, int weight) {
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
	
	public int getPrice() {
		return this.price;
	}
	
	public void setPrice(int price) {
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
			int price = res.getInt("price");
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
