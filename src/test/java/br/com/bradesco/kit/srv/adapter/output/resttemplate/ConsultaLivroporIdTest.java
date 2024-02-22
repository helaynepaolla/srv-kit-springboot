package br.com.bradesco.kit.srv.adapter.output.resttemplate;

import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.LivroDTO;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true", "spring.redis.port=6370",
		"spring.redis.host=localhost"})
@Nested
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConsultaLivroporIdTest {
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
	void consultaLivroporIdSucesso() {
		LivroDTO dto1 = LivroFactoryBot.createOutputResttemplateLivroDto(ID_LIVRO1);
		given(restTemplate.getForObject(connectionURL + ID_LIVRO1, LivroDTO.class)).willReturn(dto1);

		Optional<Livro> result = subject.consultaLivroporId(ID_LIVRO1);

		then(restTemplate).should().getForObject(connectionURL + ID_LIVRO1, LivroDTO.class);
		Livro livroResult = result.get();
		assertThat(livroResult.getTitulo()).isEqualTo(dto1.titulo());
	}

	@Test
	void verificaExcecaoDoRestTemplate() {
		RestClientException ex = new RestClientException("somethings wrong happend");
		given(restTemplate.getForObject(connectionURL + ID_LIVRO1, LivroDTO.class)).willThrow(ex);

		assertThrows(
				RestClientException.class,
				() -> subject.consultaLivroporId(ID_LIVRO1),
				"Expected exception to throw, but it didn't");
	}

	@Test
	void consultaLivroporIdFallback() {
		// Given
		LivroDTO dto1 = LivroFactoryBot.createOutputResttemplateLivroDto(ID_LIVRO1);
		given(restTemplate.getForObject(anyString(), eq(LivroDTO.class))).willReturn(dto1);

		// When
		Optional<Livro> result = subject.fallbackConsultaLivroPorId(ID_LIVRO1,
				new RuntimeException("Falha na requisição HTTP"));

        // Then
        then(restTemplate).should().getForObject(anyString(), eq(LivroDTO.class));
        Livro livroResult = result.get();
        assertThat(livroResult.getTitulo()).isEqualTo(dto1.titulo());
    }

}
