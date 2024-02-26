package PR3;

public class ElectricEngine implements Engine {
	private int voltage;
	
	public ElectricEngine() {}
	
	public ElectricEngine(int voltage) {
		this.voltage = voltage;
	}
	
	@Override
	public String start() {
		return "started an electric engine using " + this.voltage + " volts";
	}
	
	public int getVoltage() {
		return this.voltage;
	}
	
	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}
}
