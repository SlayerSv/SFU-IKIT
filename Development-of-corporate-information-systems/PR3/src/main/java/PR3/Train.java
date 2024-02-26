package PR3;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Train {
	private static int counter;
	private Engine engine;
	private int maxSpeed;
	private int number;
	
	public Train(Engine engine, int maxSpeed) {
		this.engine = engine;
		this.maxSpeed = maxSpeed;
		this.number = ++Train.counter;
	}
	
	@PostConstruct
	public void ready() {
		System.out.println("The train number " + this.number + " has finished maintenance and ready to go!");
	}
	
	@PreDestroy
	public void done() {
		System.out.println("The train number " + this.number + " is going back to maintenance.");
	}
	
	public int getMaxSpeed() {
		return this.maxSpeed;
	}
	
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public Engine getEngine() {
		return this.engine;
	}
	
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public String go() {
		return "The train  number " + this.number + " has " + engine.start()
		+ " and going with a speed of " + this.maxSpeed + " km/h.";
	}
}
