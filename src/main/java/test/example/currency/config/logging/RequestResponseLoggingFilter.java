package test.example.currency.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RequestResponseLoggingFilter extends CommonsRequestLoggingFilter {

    private static final String AFTER_MESSAGE_PREFIX = "[Request: ";
    private final String healthEndpointsBasePath;

    public RequestResponseLoggingFilter(WebEndpointProperties webEndpointProperties) {
        setIncludeQueryString(true);
        setIncludePayload(true);
        setMaxPayloadLength(10000);
        setIncludeHeaders(false);
        setIncludeClientInfo(false);
        setAfterMessagePrefix(AFTER_MESSAGE_PREFIX);
        healthEndpointsBasePath = webEndpointProperties.getBasePath();
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return super.shouldLog(request) && !request.getRequestURI().startsWith(healthEndpointsBasePath);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!shouldLog(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestToUse = (request instanceof ContentCachingRequestWrapper)
            ? (ContentCachingRequestWrapper) request : new ContentCachingRequestWrapper(request, getMaxPayloadLength());
        ContentCachingResponseWrapper responseToUse = (response instanceof ContentCachingResponseWrapper)
            ? (ContentCachingResponseWrapper) response : new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            String requestMessage = createMessage(requestToUse, AFTER_MESSAGE_PREFIX, DEFAULT_AFTER_MESSAGE_SUFFIX);
            String responseMessage = getResponseMessage(responseToUse);
            logger.debug(requestMessage + responseMessage);
        }
    }

    private String getResponseMessage(ContentCachingResponseWrapper responseToUse) throws IOException {
        final StringBuilder builder = new StringBuilder("[Response: status=")
            .append(responseToUse.getStatus());

        if (responseToUse.getContentSize() > 0) {
            builder
                .append(", payload=")
                .append(new String(responseToUse.getContentAsByteArray(), StandardCharsets.UTF_8.name()));
            responseToUse.copyBodyToResponse();
        }

        return builder.append(DEFAULT_AFTER_MESSAGE_SUFFIX)
            .toString();
    }
}
