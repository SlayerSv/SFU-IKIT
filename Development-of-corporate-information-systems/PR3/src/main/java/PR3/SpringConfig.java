package PR3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("PR3")
@PropertySource("classpath:engine.properties")
public class SpringConfig {
	@Value("${DieselEngine.power}")
	int power;
	@Value("${ElectricEngine.voltage}")
	int voltage;
	@Value("${SteamEngine.fuelType}")
	String fuelType;
	
	@Bean
	public DieselEngine dieselEngine() {
		return DieselEngine.getDieselEngine(power);
	}
	
	@Bean
	public ElectricEngine electricEngine() {
		ElectricEngine electricEngine = new ElectricEngine();
		electricEngine.setVoltage(voltage);
		return electricEngine;
	}
	
	@Bean
	public SteamEngine steamEngine() {
		return new SteamEngine(fuelType);
	}
	@Bean
	public Train steamTrain() {
		return new Train(steamEngine(), 50);
	}
	
	@Bean
	public Train dieselTrain() {
		return new Train(dieselEngine(), 55);
	}
	
	@Bean
	public Train electricTrain() {
		return new Train(electricEngine(), 60);
	}
}
