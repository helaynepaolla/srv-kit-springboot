package br.com.bradesco.kit.srv.adapter.input.consultajuncao;

import br.com.bradesco.kit.srv.domain.entity.Juncao;
import br.com.bradesco.kit.srv.port.input.ConsultarJuncaoUseCase;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ConsultaJuncaoController.class)
@AutoConfigureMockMvc
class ConsultaJuncaoControllerTest {

	private static final String URL_JUNCAO = "/frwk/";

	@MockBean
	private ConsultarJuncaoUseCase juncaoUseCase;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CircuitBreakerRegistry circuitBreakerRegistry;


	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("Teste de sucesso na consulta de Juncao do controller")
	void frwk() throws Exception {
		String rotina = "sarh";
		Integer matricula = 9123123;

		Juncao retornoJuncao = new Juncao(10, "juncao teste");

		when(juncaoUseCase.getJuncaoPertence(rotina, matricula))
				.thenReturn(Optional.of(retornoJuncao));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(URL_JUNCAO + "/" + rotina + "/" + matricula)
				.accept(APPLICATION_JSON);

		this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk());
	}
}
