package test.example.currency.web.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import test.example.currency.service.CurrencyService;
import test.example.currency.web.dto.CurrencyDto;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/currency/current")
    public List<CurrencyDto> getCurrentCurrencies() {
        return currencyService.getCurrentCurrencies();
    }

    @GetMapping("/currency")
    public List<CurrencyDto> getCurrenciesByExchangeDate(@RequestParam("date") final LocalDate date) {
        return currencyService.getCurrenciesByExchangeDate(date);
    }

    @DeleteMapping("/currency")
    public void deleteCurrenciesByExchangeDate(@RequestParam("date") final LocalDate date) {
        currencyService.deleteCurrenciesByExchangeDate(date);
    }
}
