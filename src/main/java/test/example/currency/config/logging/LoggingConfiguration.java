package test.example.currency.config.logging;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {

    @Bean
    public RequestResponseLoggingFilter requestResponseLoggingFilter(WebEndpointProperties webEndpointProperties) {
        return new RequestResponseLoggingFilter(webEndpointProperties);
    }
}
