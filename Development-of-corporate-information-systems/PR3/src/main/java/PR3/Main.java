package PR3;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(SpringConfig.class);
		
		Train electricTrain = context.getBean("electricTrain", Train.class);
		Train dieselTrain = context.getBean("dieselTrain", Train.class);
		Train steamTrain = context.getBean("steamTrain", Train.class);
		System.out.println(electricTrain.go());
		System.out.println(dieselTrain.go());
		System.out.println(steamTrain.go());		
		context.close();
	}
}
