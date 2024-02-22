package br.com.bradesco.kit.srv.config;

import br.com.bradesco.kit.srv.adapter.exception.handler.RestExceptionHandler;
import br.com.bradesco.kit.srv.domain.entity.GeneroLivro;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StringEnumConverterFactoryTest {

    private static MockMvc mockMvc;
    @Autowired
    ConversionService conversionService;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {

        mockMvc = MockMvcBuilders
                .standaloneSetup(new EnumTestController())
                .setControllerAdvice(new RestExceptionHandler()).build();
    }

    @Test
    void passaStringDeEnum_converteParaEnum_comSucesso() {
        assertThat(conversionService.convert("FANTASIA", GeneroLivro.class))
                .isEqualTo(GeneroLivro.FANTASIA);
    }

    @Test
    void passaStringEnumGenero_converteParaEnum_retornaHttp200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/converter-enum?generoLivro=ACAO"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void passaStringEnumInexistente_tentaConverterParaEnum_retornaHttp400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/converter-enum?generoLivro=MELANCIA"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Controller
    @RequestMapping(path = "/tests")
    public static class EnumTestController {

        @GetMapping("/converter-enum")
        public ResponseEntity<GeneroLivro> obtemInfraException(@RequestParam("generoLivro") GeneroLivro generoLivro) {
            return ResponseEntity.ok(generoLivro);
        }
    }
}