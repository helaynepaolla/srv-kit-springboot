package br.com.bradesco.kit.srv.config;

import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfrastructureException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = null;

        try {
            response = execution.execute(request, body);
        } catch (IOException t) {
            throw new InfrastructureException(HttpStatus.INTERNAL_SERVER_ERROR.name(), t.getMessage(), t);
        }
        return response;
    }
}
