package test.example.currency.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import test.example.currency.domain.Currency;
import test.example.currency.exceptions.CurrencyByExchangeDateNotFoundException;
import test.example.currency.repository.CurrencyRepository;
import test.example.currency.service.client.nbu.CurrencyNBUResponse;
import test.example.currency.service.client.nbu.NBUApi;
import test.example.currency.web.dto.CurrencyDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final NBUApi nbuApi;

    @Override
    public List<CurrencyDto> getCurrentCurrencies() {
        LocalDate now = LocalDate.now();
        if (currencyRepository.existsCurrencyByExchangeDate(now)) {
            return getCurrencyDtosByExchangeDate(now);
        }

        List<CurrencyNBUResponse> currenciesFromNBU = nbuApi.getCurrenciesFromNBU();

        List<Currency> listCurrencies = currenciesFromNBU
            .stream()
            .map(it -> Currency
                .builder()
                .r030(it.getR030())
                .txt(it.getTxt())
                .rate(it.getRate())
                .cc(it.getCc())
                .exchangeDate(now)
                .build())
            .toList();

        Iterable<Currency> currencyIterable = currencyRepository.saveAll(listCurrencies);

        return StreamSupport.stream(currencyIterable.spliterator(), false)
            .map(this::toDto)
            .toList();
    }

    @Override
    public List<CurrencyDto> getCurrenciesByExchangeDate(LocalDate date) {
        if (!currencyRepository.existsCurrencyByExchangeDate(date)) {
            throw new CurrencyByExchangeDateNotFoundException(
                String.format("Currency by exchange date: [%s] not found", date));
        }

        return getCurrencyDtosByExchangeDate(date);
    }

    @Override
    public void deleteCurrenciesByExchangeDate(LocalDate date) {
        if (!currencyRepository.existsCurrencyByExchangeDate(date)) {
            throw new CurrencyByExchangeDateNotFoundException(
                String.format("Currency by exchange date: [%s] not found", date));
        }

        currencyRepository.deleteAllByExchangeDate(date);
    }

    private List<CurrencyDto> getCurrencyDtosByExchangeDate(LocalDate date) {
        return currencyRepository.findAllByExchangeDate(date)
            .stream()
            .map(this::toDto)
            .toList();
    }

    private CurrencyDto toDto(Currency currency) {
        return CurrencyDto.builder()
            .r030(currency.getR030())
            .txt(currency.getTxt())
            .rate(currency.getRate())
            .cc(currency.getCc())
            .createdDate(currency.getCreatedDate())
            .build();
    }
}
