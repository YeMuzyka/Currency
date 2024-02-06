package test.example.currency.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import test.example.currency.service.CurrencyService;
import test.example.currency.web.dto.CurrencyDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTest {
    @MockBean
    private CurrencyService service;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCurrentCurrencies() throws Exception {
        when(service.getCurrentCurrencies()).thenReturn(mockCurrenciesDtos());

        this.mockMvc.perform(get("/api/currency/current"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].r030").value(10))
            .andExpect(jsonPath("$[0].txt").value("Австралійський долар"))
            .andExpect(jsonPath("$[0].rate").value(BigDecimal.ONE))
            .andExpect(jsonPath("$[0].cc").value("AUD"));
    }

    @Test
    void shouldReturnBadRequestWhenGetCurrenciesByDate() throws Exception {
        this.mockMvc.perform(get("/api/currency"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnCurrenciesByDate() throws Exception {
        final LocalDate now = LocalDate.now();
        when(service.getCurrenciesByExchangeDate(now)).thenReturn(mockCurrenciesDtos());

        this.mockMvc.perform(get("/api/currency?date=" + now))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].r030").value(10))
            .andExpect(jsonPath("$[0].txt").value("Австралійський долар"))
            .andExpect(jsonPath("$[0].rate").value(BigDecimal.ONE))
            .andExpect(jsonPath("$[0].cc").value("AUD"));
    }

    @Test
    void shouldReturnBadRequestWhenDeleteCurrenciesByDate() throws Exception {
        this.mockMvc.perform(delete("/api/currency"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRDeleteCurrenciesByDate() throws Exception {
        final LocalDate now = LocalDate.now();
        doNothing().when(service).deleteCurrenciesByExchangeDate(now);

        this.mockMvc.perform(delete("/api/currency?date=" + now))
            .andExpect(status().isOk());
    }

    private List<CurrencyDto> mockCurrenciesDtos() {
        return singletonList(CurrencyDto
            .builder()
            .r030(10)
            .txt("Австралійський долар")
            .rate(BigDecimal.ONE)
            .cc("AUD")
            .build());
    }

}
