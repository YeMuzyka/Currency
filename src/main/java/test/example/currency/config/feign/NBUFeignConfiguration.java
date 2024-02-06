package test.example.currency.config.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.Logger.Level;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.example.currency.service.client.nbu.NBUApi;

import java.util.Optional;

@Configuration
public class NBUFeignConfiguration {
    private final String url;
    private final ObjectMapper objectMapper;

    private final Level logLevel;

    private final Logger logger;

    @Autowired
    public NBUFeignConfiguration(@Value("${nbu.url}") String url,
                                 ObjectMapper objectMapper,
                                 Level logLevel,
                                 @Autowired(required = false) Logger logger) {
        this.url = url;
        this.objectMapper = objectMapper;
        this.logLevel = logLevel;
        this.logger = Optional.ofNullable(logger).orElseGet(Logger.NoOpLogger::new);
    }

    @Bean
    public NBUApi nbuApi() {
        return Feign.builder()
            .encoder(new FormEncoder(new JacksonEncoder(objectMapper)))
            .decoder(new JacksonDecoder(objectMapper))
            .logger(logger)
            .logLevel(logLevel)
            .target(NBUApi.class, url);
    }
}
