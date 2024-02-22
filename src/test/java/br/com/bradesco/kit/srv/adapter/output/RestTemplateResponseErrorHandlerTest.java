package br.com.bradesco.kit.srv.adapter.output;

import br.com.bradesco.kit.srv.adapter.exception.handler.RestTemplateResponseErrorHandler;
import br.com.bradesco.kit.srv.domain.exception.ServicoIndisponivelException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * Teste unitario especifico para validar se qualquer excpetion que ocorra oriundo de chamadas RestTemplate seja
 * devidamente capturado e devolva exception de Servico Indisponivel.
 *
 * @see RestClientResponseException
 * @see RestClientException
 */
@ExtendWith(SpringExtension.class)
@RestClientTest
class RestTemplateResponseErrorHandlerTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplateBuilder builder;

    @Test
    void realizaChamadaApi_ocorreErroHttp404_entaoLanca_thenThrowServicoIndisponivelException() {
        Assertions.assertNotNull(this.builder);
        Assertions.assertNotNull(this.server);

        RestTemplate restTemplate = this.builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();

        this.server
                .expect(ExpectedCount.once(), requestTo("/teste/123"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(ServicoIndisponivelException.class, () -> {
            restTemplate.getForObject("/teste/123", Object.class);
        });
    }
}

