package by.timofeyzaytsev.textpolish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TextPolishApplication {

	public static void main(String[] args) {
		SpringApplication.run(TextPolishApplication.class, args);
	}

}
