package by.timofeyzaytsev.textpolish.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "by.timofeyzaytsev.textpolish.infrastructure.client")
public class FeignClientConfig {
}
