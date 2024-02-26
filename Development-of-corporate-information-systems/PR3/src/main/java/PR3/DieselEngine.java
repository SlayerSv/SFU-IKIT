package PR3;

public class DieselEngine implements Engine {
	
	public static DieselEngine getDieselEngine(int power) {
		DieselEngine engine = new DieselEngine();
		engine.setPower(power);
		return engine;
	}
	
	private int power;
	
	private DieselEngine() {}
	
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
