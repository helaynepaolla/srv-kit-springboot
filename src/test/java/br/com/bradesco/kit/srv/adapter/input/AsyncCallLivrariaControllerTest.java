package br.com.bradesco.kit.srv.adapter.input;

import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.port.input.IManutencaoEstoqueLivro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AsyncCallLivrariaControllerTest {

    private static final String IDS = "ids[]";
    private static final String LIVRARIA_GET_MULTIPLES = "/v1/livros-async";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IManutencaoEstoqueLivro manutencaoEstoque;

    @Test
    @DisplayName("Consultar com Sucesso 1 item - Lista com 1 item")
    void consultaLivrariaSucesso1Item() throws Exception {
        Livro livro = LivroFactoryBot.createLivro(1);
        when(manutencaoEstoque.consultaVariosLivrosPorId(any(Integer[].class)))
                .thenReturn(Arrays.asList(livro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRARIA_GET_MULTIPLES)
                .accept(APPLICATION_JSON)
                .queryParam(IDS, "1");

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
    @DisplayName("Consultar com Sucesso 3 items - Lista com 3 items")
    void consultaLivrariaSucesso2Items() throws Exception {
        Livro livro = LivroFactoryBot.createLivro(1);
        Livro livro2 = LivroFactoryBot.createLivro(2);
        Livro livro3 = LivroFactoryBot.createLivro(3);
        when(manutencaoEstoque.consultaVariosLivrosPorId(any(Integer[].class)))
                .thenReturn(Arrays.asList(livro, livro2, livro3));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRARIA_GET_MULTIPLES)
                .accept(APPLICATION_JSON)
                .queryParam(IDS, "1")
                .queryParam(IDS, "2");

        this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[2]").exists())
                .andExpect(jsonPath("$[3]").doesNotExist());
    }

    @Test
    @DisplayName("Consultar o mesmo item 2 ou mais vezes - Lista com 2 ou mais items")
    void consultaLivrariaSucesso2oumaisitens() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRARIA_GET_MULTIPLES)
                .accept(APPLICATION_JSON)
                .queryParam(IDS, "1")
                .queryParam(IDS, "1")
                .queryParam(IDS, "1");

        this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        Integer[] expectedIds = new Integer[]{1, 1, 1};
        verify(manutencaoEstoque, times(1)).consultaVariosLivrosPorId(expectedIds);
    }

    @Test
    @DisplayName("Consultar Sucesso sem Registros - StatusCode 200 e Lista Vazia ")
    void consultaLivrariaSucessoComCorpoVazio() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRARIA_GET_MULTIPLES)
                .accept(APPLICATION_JSON)
                .queryParam(IDS, "1");
        when(manutencaoEstoque.consultaVariosLivrosPorId(any(Integer[].class)))
                .thenReturn(new ArrayList<Livro>());

        this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Consultar com Campo Invalido - Http Status Code 400")
    void consultaLivrosErroDeIdsInvalido() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRARIA_GET_MULTIPLES)
                .accept(APPLICATION_JSON)
                .queryParam("asdasdasd", "-1");

        this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.apierro.mensagem").value("ids[] parametro nao informado"));
        verifyNoInteractions(manutencaoEstoque);
    }
}
