package PR3;

public class SteamEngine implements Engine {
	private String fuelType;
	
	public SteamEngine() {}
	public SteamEngine(String fuelType) {
		this.fuelType = fuelType;
	}
	
	@Override
	public String start() {
		return "started a steam engine powered by " + this.fuelType;
	}
	
	public String getFuelType() {
		return this.fuelType;
	}
	
	public void setFuelType(String fueltype) {
		this.fuelType = fueltype;
	}
}
