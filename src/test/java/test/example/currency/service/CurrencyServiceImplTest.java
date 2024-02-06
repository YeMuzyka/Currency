package test.example.currency.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.example.currency.domain.Currency;
import test.example.currency.exceptions.CurrencyByExchangeDateNotFoundException;
import test.example.currency.repository.CurrencyRepository;
import test.example.currency.service.client.nbu.CurrencyNBUResponse;
import test.example.currency.service.client.nbu.NBUApi;
import test.example.currency.web.dto.CurrencyDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {
    @Mock
    private CurrencyRepository repository;
    @Mock
    private NBUApi nbuApi;
    @InjectMocks
    private CurrencyServiceImpl service;

    @Test
    void shouldGetExistingCurrentCurrencies() {
        final LocalDate exchangeDate = LocalDate.now();
        final LocalDateTime createdDate = LocalDateTime.now();

        when(repository.existsCurrencyByExchangeDate(exchangeDate)).thenReturn(true);
        when(repository.findAllByExchangeDate(exchangeDate)).thenReturn(mockCurrencies(exchangeDate, createdDate));

        List<CurrencyDto> result = service.getCurrentCurrencies();

        verifyNoInteractions(nbuApi);

        assertNotNull(result);
        assertEquals(mockCurrenciesDtos(createdDate), result);

    }

    @Test
    void shouldGetCurrentCurrenciesFromNBU() {
        final LocalDate exchangeDate = LocalDate.now();
        final LocalDateTime createdDate = LocalDateTime.now();

        when(repository.existsCurrencyByExchangeDate(exchangeDate)).thenReturn(false);
        when(nbuApi.getCurrenciesFromNBU())
            .thenReturn(singletonList(CurrencyNBUResponse
                .builder()
                .r030(36)
                .txt("Австралійський долар")
                .rate(BigDecimal.ONE)
                .cc("AUD")
                .exchangedate(exchangeDate)
                .build()));
        when(repository.saveAll(anyIterable())).thenReturn(mockCurrencies(exchangeDate, createdDate));

        List<CurrencyDto> result = service.getCurrentCurrencies();

        assertNotNull(result);
        assertEquals(mockCurrenciesDtos(createdDate), result);

    }

    @Test
    void shouldGetExceptionWhenGetCurrenciesByExchangeDate() {
        final LocalDate exchangeDate = LocalDate.now();

        when(repository.existsCurrencyByExchangeDate(exchangeDate)).thenReturn(false);

        assertThatThrownBy(() -> service.getCurrenciesByExchangeDate(exchangeDate))
            .isInstanceOf(CurrencyByExchangeDateNotFoundException.class);
    }

    @Test
    void shouldGetCurrenciesByExchangeDate() {
        final LocalDate exchangeDate = LocalDate.now();
        final LocalDateTime createdDate = LocalDateTime.now();

        when(repository.existsCurrencyByExchangeDate(exchangeDate)).thenReturn(true);

        when(repository.findAllByExchangeDate(exchangeDate)).thenReturn(mockCurrencies(exchangeDate, createdDate));

        List<CurrencyDto> result = service.getCurrenciesByExchangeDate(exchangeDate);

        assertNotNull(result);
        assertEquals(mockCurrenciesDtos(createdDate), result);
    }

    @Test
    void shouldGetExceptionWhenDeleteCurrenciesByExchangeDate() {
        final LocalDate exchangeDate = LocalDate.now();

        when(repository.existsCurrencyByExchangeDate(exchangeDate)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteCurrenciesByExchangeDate(exchangeDate))
            .isInstanceOf(CurrencyByExchangeDateNotFoundException.class);
    }

    @Test
    void shouldDeleteCurrenciesByExchangeDate() {
        final LocalDate exchangeDate = LocalDate.now();

        when(repository.existsCurrencyByExchangeDate(exchangeDate)).thenReturn(true);

        service.deleteCurrenciesByExchangeDate(exchangeDate);

        verify(repository).deleteAllByExchangeDate(exchangeDate);

    }

    private List<Currency> mockCurrencies(LocalDate exchangeDate, LocalDateTime createdDate) {
        return singletonList(Currency
            .builder()
            .id(1L)
            .r030(36)
            .txt("Австралійський долар")
            .rate(BigDecimal.ONE)
            .cc("AUD")
            .exchangeDate(exchangeDate)
            .createdDate(createdDate)
            .build());
    }

    private List<CurrencyDto> mockCurrenciesDtos(LocalDateTime createdDate) {
        return singletonList(CurrencyDto
            .builder()
            .r030(36)
            .txt("Австралійський долар")
            .rate(BigDecimal.ONE)
            .cc("AUD")
            .createdDate(createdDate)
            .build());
    }
}
