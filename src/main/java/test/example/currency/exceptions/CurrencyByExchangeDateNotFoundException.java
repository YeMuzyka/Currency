package test.example.currency.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CurrencyByExchangeDateNotFoundException extends RuntimeException {
    private String message;
}
