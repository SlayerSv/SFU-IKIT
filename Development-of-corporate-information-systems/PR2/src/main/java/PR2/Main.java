package PR2;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new
				ClassPathXmlApplicationContext("applicationContext.xml");
				Train electricTrain = context.getBean("electricTrain", Train.class);
				Train dieselTrain = context.getBean("dieselTrain", Train.class);
				Train steamTrain = context.getBean("steamTrain", Train.class);
				System.out.println(electricTrain.go());
				System.out.println(dieselTrain.go());
				System.out.println(steamTrain.go());
				context.close();
	}
}
