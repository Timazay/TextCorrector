package by.timofeyzaytsev.textcorrector.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "by.timofeyzaytsev.textcorrector.feignclient")
public class FeignClientConfig {
}
