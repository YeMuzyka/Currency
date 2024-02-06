package test.example.currency.service.client.nbu;

import feign.Headers;
import feign.RequestLine;

import java.util.List;

public interface NBUApi {

    @RequestLine("GET /NBUStatService/v1/statdirectory/exchange/?json")
    @Headers({"Content-Type: application/json; charset=utf-8", "Accept: application/json"})
    List<CurrencyNBUResponse> getCurrenciesFromNBU();
}
