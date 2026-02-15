package by.timofeyzaytsev.textcorrector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TextCorrectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TextCorrectorApplication.class, args);
	}

}
