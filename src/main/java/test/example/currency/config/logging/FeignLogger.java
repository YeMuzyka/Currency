package test.example.currency.config.logging;

import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static feign.Util.UTF_8;

@Slf4j
public class FeignLogger extends feign.Logger {

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        String body = "";
        if (request.body() != null && request.charset() != null) {
            body = new String(request.body(), request.charset());
        }
        log(configKey, " Request: %s %s Body:%s", request.httpMethod(), request.url(), body);
        log(configKey, request.headers().toString());
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response,
                                              long elapsedTime) throws IOException {
        InputStream is = response.body().asInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        Response copiedResponse = Response.builder()
            .status(response.status())
            .reason(response.reason())
            .headers(response.headers())
            .body(bytes)
            .request(response.request())
            .build();
        if (response.body() != null) {
            final String body = Util.decodeOrDefault(bytes, UTF_8, "Binary data");
            log(configKey, " Response: %s %dms Body:%s", response.status(), elapsedTime, body);
            return copiedResponse;
        }
        log(configKey, " Response: %s %dms", response.status(), elapsedTime);
        return copiedResponse;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.debug(String.format(methodTag(configKey) + format, args));
    }

    @Override
    protected void logRetry(String configKey, Level logLevel) {
        // do nothing
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe,
                                         long elapsedTime) {
        log.error("Unexpected IO exception", ioe);
        return ioe;
    }
}
