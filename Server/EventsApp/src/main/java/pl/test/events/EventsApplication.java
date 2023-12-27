package pl.test.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

// todo zrobic dokumentacje w javadoc
@SpringBootApplication
@EnableScheduling
@PropertySource("classpath:application-development.properties")
public class EventsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventsApplication.class, args);
	}

}
