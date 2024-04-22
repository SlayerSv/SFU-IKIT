package PR5;

import javax.validation.constraints.PositiveOrZero;

import org.springframework.stereotype.Component;

@Component("priceRange")
public class PriceRange {
	
	@PositiveOrZero(message = "price cannot be negative")
	private double minPrice;
	@PositiveOrZero(message = "price cannot be negative")
	private double maxPrice;
	
	public double getMinPrice() {
		return this.minPrice;
	}
	
	public String getMinPriceF() {
		return String.format("%.02f", this.minPrice).replace(",", ".");
	}
	
	public void setMinPrice(double price) {
		this.minPrice = price;
	}
	
	public double getMaxPrice() {
		return this.maxPrice;
	}
	
	public String getMaxPriceF() {
		return String.format("%.02f", this.maxPrice).replace(",", ".");
	}
	
	public void setMaxPrice(double price) {
		this.maxPrice = price;
	}
}
