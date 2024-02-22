package br.com.bradesco.kit.srv.adapter.output.resttemplate;

import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfraCallNotPermitedException;
import br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.LivroDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RemoverLivroTest {
    private static final Integer ID_LIVRO1 = 1;
    private final String connectionURL = "https://to.some.magical.place/";
    @Mock
    private RestTemplate restTemplate;
    private EstoqueRestTemplate subject;

    @BeforeEach
    void setup() {
        this.subject = new EstoqueRestTemplate(restTemplate, connectionURL);
    }

    @Test
    void removerLivroSucesso() {
        subject.removerLivro(ID_LIVRO1);

        then(restTemplate).should().exchange(connectionURL + ID_LIVRO1, HttpMethod.DELETE, null, LivroDTO.class);
    }

    @Test
    void removerLivroFallback() {
        RuntimeException ex = new RuntimeException("Gerando falha para invocacao do fallback");

        assertThrows(
                InfraCallNotPermitedException.class,
                () -> subject.fallbackRemoverLivro(ID_LIVRO1, ex),
                "Nao lancou excecao esperada");
    }

    @Test
    void verificaExcecaoDoRestTemplate() {
        RestClientException ex = new RestClientException("somethings wrong happend");
        given(restTemplate.exchange(connectionURL + ID_LIVRO1, HttpMethod.DELETE, null, LivroDTO.class)).willThrow(ex);

        assertThrows(
                RestClientException.class,
                () -> subject.removerLivro(ID_LIVRO1),
                "Expected exception to throw, but it didn't");
    }

}
