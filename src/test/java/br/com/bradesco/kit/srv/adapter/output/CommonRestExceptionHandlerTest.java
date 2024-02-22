package br.com.bradesco.kit.srv.adapter.output;

import br.com.bradesco.kit.srv.adapter.exception.handler.RestExceptionHandler;
import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfrastructureException;
import br.com.bradesco.kit.srv.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CommonRestExceptionHandlerTest {

    private MockMvc mockMvc;

    /**
     * Called before each test.
     */
    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(RestProcessingExceptionThrowingController.class)
                .setControllerAdvice(new RestExceptionHandler()).build();
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoInfraException_deveRetornarErroSrvResponseComHTTP422() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/output/infraException")).andDo(print())
                .andExpect(jsonPath("$.apierro.subErros").isEmpty())
                .andExpect(jsonPath("$.apierro.status").value(INTERNAL_SERVER_ERROR.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").exists())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoGenerica_deveRetornarErroSrvResponseComHTTP500() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/output/exception")).andDo(print())
                .andExpect(jsonPath("$.apierro.status").value(INTERNAL_SERVER_ERROR.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").exists())
                .andExpect(status().isInternalServerError());
    }

    @Controller
    @RequestMapping(path = "/tests/output/")
    public static class RestProcessingExceptionThrowingController {
        @GetMapping(value = "/infraException")
        public @ResponseBody String getInfrastructureException() {
            throw new InfrastructureException("100", "Algum erro de IO");
        }

        @GetMapping(value = "/businessException")
        public @ResponseBody String getBusinessException() {
            throw new BusinessException("101", "Regra de negocio violada.");
        }

        @GetMapping(value = "/exception")
        public @ResponseBody String getException() throws Exception {
            throw new Exception("Algum erro no app");
        }

    }

}
