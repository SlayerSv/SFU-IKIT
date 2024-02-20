package PR2;

public class Train {
	private Engine engine;
	private int maxSpeed;
	
	public Train(Engine engine, int maxSpeed) {
		this.engine = engine;
		this.maxSpeed = maxSpeed;
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
	
	public String go() {
		return "Train has " + engine.start()
		+ " and going with a speed of " + this.maxSpeed + " km/h.";
	}
}
