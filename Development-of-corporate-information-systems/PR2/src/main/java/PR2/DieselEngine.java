package PR2;

public class DieselEngine implements Engine {
	private int power;
	
	public DieselEngine() {}
	public DieselEngine(int power) {
		this.power = power;
	}
	
	@Override
	public String start() {
		return "started a diesel engine with " + this.power + " horse power";
	}
	
	public int getPower() {
		return this.power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
}
