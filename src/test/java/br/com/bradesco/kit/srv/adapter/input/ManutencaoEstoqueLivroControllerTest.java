package br.com.bradesco.kit.srv.adapter.input;

import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.adapter.input.imanutencaoestoquelivro.ManutencaoEstoqueLivroController;
import br.com.bradesco.kit.srv.adapter.input.imanutencaoestoquelivro.dto.LivroDTO;
import br.com.bradesco.kit.srv.adapter.output.redis.LivroRepository;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.port.input.IManutencaoEstoqueLivro;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ManutencaoEstoqueLivroController.class)
@AutoConfigureMockMvc
class ManutencaoEstoqueLivroControllerTest {

    private static final String LIVRARIA_LIVRO_LIST = "/v2/livros";
    private static final String LIVRARIA_LIVRO = "/v2/livro";
    private static final String PAGINA = "page";
    private static final String REGISTROS_POR_PAGINA = "page-size";
    private static final Integer ID_DO_LIVRO = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IManutencaoEstoqueLivro manutencaoEstoque;

    @MockBean
    private LivroRepository livroRepository;

    @MockBean
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private RedisTemplate redisTemplate;
    private Livro livro;

    @BeforeEach
    public void setup() {
        this.livro = LivroFactoryBot.createLivro(ID_DO_LIVRO);
    }

    @Test
    @DisplayName("Consultar Lista Sucesso - Http Status Code 200 e JSON")
    void consultaListaDeLivrosPorPaginaELivrosPorPagina() throws Exception {
        when(manutencaoEstoque.consultaLivros(1, 2))
                .thenReturn(Arrays.asList(this.livro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRARIA_LIVRO_LIST)
                .accept(APPLICATION_JSON)
                .queryParam(REGISTROS_POR_PAGINA, "2")
                .queryParam(PAGINA, "1");

        this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].id").value(livro.getId()))
                .andExpect(jsonPath("$[0].autor").value(livro.getAutor()))
                .andExpect(jsonPath("$[0].editora").value(livro.getEditora()))
                .andExpect(jsonPath("$[0].valor").value(livro.getValor()))
                .andExpect(jsonPath("$[0].titulo").value(livro.getTitulo()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    @DisplayName("Consultar Livro por ID Sucesso - Http Status Code 200")
    void consulta1LivroPorID() throws Exception {
        when(manutencaoEstoque.consultaLivroPorId(1))
                .thenReturn(Optional.of(livro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRARIA_LIVRO + "/" + ID_DO_LIVRO)
                .accept(APPLICATION_JSON);

        this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(livro.getId()))
                .andExpect(jsonPath("$.autor").value(livro.getAutor()))
                .andExpect(jsonPath("$.editora").value(livro.getEditora()))
                .andExpect(jsonPath("$.valor").value(livro.getValor()))
                .andExpect(jsonPath("$.titulo").value(livro.getTitulo()));
    }

    @Test
    @DisplayName("Consultar Livro por ID Falha - Http Status Code 404")
    void consulta1LivroPorIDMasNaoExiste() throws Exception {
        when(manutencaoEstoque.consultaLivroPorId(1))
                .thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRARIA_LIVRO + "/" + ID_DO_LIVRO)
                .accept(APPLICATION_JSON);

        this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Incluir Livro Sucesso - Http Status Code 201")
    void incluiLivroComSucesso() throws Exception {
        LivroDTO livroDTO = LivroFactoryBot.createInputLivroDto(ID_DO_LIVRO);
        String jsonParam = new ObjectMapper().writeValueAsString(livroDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LIVRARIA_LIVRO)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(jsonParam);

        this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());
        then(manutencaoEstoque).should().incluirNovoLivro(any());
    }

    @Test
    @DisplayName("Excluir Livro Sucesso - Http Status Code 200")
    void removerLivroComSucesso() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(LIVRARIA_LIVRO + "/" + ID_DO_LIVRO)
                .accept(APPLICATION_JSON);

        this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        then(manutencaoEstoque).should().removerLivro(ID_DO_LIVRO);
    }
}
