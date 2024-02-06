package test.example.currency.config.feign;

import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.example.currency.config.logging.FeignLogger;

@Configuration
@EnableFeignClients
public class FeignConfiguration {

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    FeignLogger feignLogger() {
        return new FeignLogger();
    }
}
