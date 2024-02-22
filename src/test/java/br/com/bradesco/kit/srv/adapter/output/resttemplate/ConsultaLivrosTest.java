package br.com.bradesco.kit.srv.adapter.output.resttemplate;

import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.LivroDTO;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ConsultaLivrosTest {
    private static final Integer ID_LIVRO1 = 1;
    private final String fallbackConnectionURL = "https://fallback.to.some.magical.place";
    private final String connectionURL = "https://to.some.magical.place/";
    private final String url = connectionURL + "?_page=1&_limit=10";
    @Mock
    private RestTemplate restTemplate;
    private EstoqueRestTemplate subject;

    @BeforeEach
    void setup() {
        this.subject = new EstoqueRestTemplate(restTemplate, connectionURL);
    }

    @Test
    void consultaLivrosSucesso() {
        LivroDTO livro = LivroFactoryBot.createOutputResttemplateLivroDto(ID_LIVRO1);
        ResponseEntity<List<LivroDTO>> response = ResponseEntity.of(Optional.of(List.of(livro)));
        given(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<LivroDTO>>() {
        })).willReturn(response);

        List<Livro> result = subject.consultaLivros(1, 10);

        Livro expectedLivro = LivroFactoryBot.createLivro(livro);
        assertThat(result).containsExactly(expectedLivro);
    }

    @Test
    void verificaExcecaoDoRestTemplate() {
        RestClientException ex = new RestClientException("somethings wrong happend");
        given(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<LivroDTO>>() {
        })).willThrow(ex);

        assertThrows(
                RestClientException.class,
                () -> subject.consultaLivros(1, 10),
                "Expected exception to throw, but it didn't");
    }

    @Test
    void consultaLivrosFallback() throws Exception {
        this.subject = mock(EstoqueRestTemplate.class, Mockito.RETURNS_DEEP_STUBS);
        // Given
        LivroDTO livro = LivroFactoryBot.createOutputResttemplateLivroDto(000);
        ResponseEntity<List<LivroDTO>> response = ResponseEntity.ok((List.of(livro)));

        lenient().when(restTemplate.exchange(fallbackConnectionURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<LivroDTO>>() {
                })).thenReturn(response);

        // When
        List<Livro> result = subject.fallbackConsultaLivros(1, 10,
                new RuntimeException("Erro de conexão"));

        assertThat(result).isNotNull();
    }

}
