package br.com.bradesco.kit.srv.adapter.output.resttemplate;

import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfraCallNotPermitedException;
import br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.LivroDTO;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class IncluirNovoLivroTest {
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
    void incluirNovoLivroSucesso() {
        Livro livro = LivroFactoryBot.createLivro(ID_LIVRO1);
        subject.incluirNovoLivro(livro);

        then(restTemplate).should().postForEntity(eq(connectionURL), any(HttpEntity.class), eq(LivroDTO.class));
    }

    @Test
    void incluirNovoLivroFallback() {
        Livro livro = LivroFactoryBot.createLivro(ID_LIVRO1);
        RuntimeException ex = new RuntimeException("Gerando falha para invocacao do fallback");

        assertThrows(
                InfraCallNotPermitedException.class,
                () -> subject.fallbackIncluirNovoLivro(livro, ex),
                "Nao lancou excecao esperada");
    }

    @Test
    void verificaExcecaoDoRestTemplate() {
        RestClientException ex = new RestClientException("somethings wrong happend");
        given(restTemplate.postForEntity(anyString(), any(), any())).willThrow(ex);

        assertThrows(
                RestClientException.class,
                () -> subject.incluirNovoLivro(null),
                "Expected exception to throw, but it didn't");
    }

}
