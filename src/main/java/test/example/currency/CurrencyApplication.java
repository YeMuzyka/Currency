package test.example.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import test.example.currency.config.ApplicationProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({ApplicationProperties.class})
public class CurrencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyApplication.class, args);
    }

}
