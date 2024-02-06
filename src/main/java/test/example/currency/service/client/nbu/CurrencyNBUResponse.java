package test.example.currency.service.client.nbu;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyNBUResponse {

    private Integer r030;

    private String txt;

    private BigDecimal rate;

    private String cc;

    @JsonFormat(pattern = "dd.MM.uuuu")
    private LocalDate exchangedate;
}
