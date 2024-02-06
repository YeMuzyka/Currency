package test.example.currency.service;

import test.example.currency.web.dto.CurrencyDto;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyService {

    public List<CurrencyDto> getCurrentCurrencies();

    public List<CurrencyDto> getCurrenciesByExchangeDate(LocalDate date);

    public void deleteCurrenciesByExchangeDate(LocalDate date);

}
