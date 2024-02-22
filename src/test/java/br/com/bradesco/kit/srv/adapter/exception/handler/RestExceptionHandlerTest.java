package br.com.bradesco.kit.srv.adapter.exception.handler;

import br.com.bradesco.kit.srv.domain.entity.GeneroLivro;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestExceptionHandlerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Called before each test.
     */
    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(RestProcessingExceptionThrowingController.class)
                .setControllerAdvice(new RestExceptionHandler()).build();
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoMissingServletRequestParameterException_deveRetornarHTTP400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/missingServletRequestParameterException")).andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apierro.subErros").isEmpty())
                .andExpect(jsonPath("$.apierro.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").exists())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoHttpMediaTypeNotSupportedException_deveRetornarHTTP415() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tests/httpMediaTypeNotSupported")).andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apierro.subErros").isEmpty())
                .andExpect(jsonPath("$.apierro.status").value(UNSUPPORTED_MEDIA_TYPE.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(UNSUPPORTED_MEDIA_TYPE.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").exists())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoMethodArgumentNotValid_deveRetornarHTTP400() throws Exception {
        TestUserBean usuario = new InvalidUserBean();

        mockMvc.perform(MockMvcRequestBuilders.post("/tests/methodArgumentNotValidException")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apierro.subErros").hasJsonPath())
                .andExpect(jsonPath("$.apierro.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").isEmpty())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoConstraintViolationException_deveRetornarHTTP400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/constraintViolationException")
                        .param("num", "12345"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apierro.subErros").isEmpty())
                .andExpect(jsonPath("$.apierro.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoHttpMessageNotReadableException_deveRetornarHTTP400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tests/httpMessageNotReadableException")).andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apierro.subErros").isEmpty())
                .andExpect(jsonPath("$.apierro.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").exists())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoHttpMessageNotWritableException_deveRetornarHTTP500() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/httpMessageNotWritableException")).andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apierro.subErros").isEmpty())
                .andExpect(jsonPath("$.apierro.status").value(INTERNAL_SERVER_ERROR.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").exists())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoNoHandlerFoundException_deveRetornarHTTP400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/noHandlerFoundException")).andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apierro.subErros").isEmpty())
                .andExpect(jsonPath("$.apierro.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").exists())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoMethodArgumentTypeMismatchException_deveRetornarHTTP400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/methodArgumentTypeMismatchException/{id}", "Teste"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apierro.subErros").isEmpty())
                .andExpect(jsonPath("$.apierro.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").exists())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoFeignException_deveRetornarHTTP500() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/feignException", "Teste"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apierro.status").value(INTERNAL_SERVER_ERROR.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").exists())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testRequisitaEndpoint_simulaExcecaoConversionFailedExceptiondeveRetornarHTTP400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/conversionFailedException?generoLivro=MELANCIA"))
                .andDo(print())
                .andExpect(jsonPath("$.apierro.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.apierro.codigoErro").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.apierro.timestamp").exists())
                .andExpect(jsonPath("$.apierro.mensagem").exists())
                .andExpect(jsonPath("$.apierro.mensagemDetalhada").exists())
                .andExpect(status().isBadRequest());
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class TestUserBean implements Serializable {
        @Size(min = 5)
        @JsonProperty("nome")
        protected String nome;

        public TestUserBean() {
            this.nome = "Nome valido aqui!";
        }
    }

    public static class InvalidUserBean extends TestUserBean {
        public InvalidUserBean() {
            super();
            this.nome = "BAD!";
        }
    }

    @Controller
    @RequestMapping(path = "/tests")
    public static class RestProcessingExceptionThrowingController {

        @GetMapping(value = "/missingServletRequestParameterException")
        public @ResponseBody String getMissingServletRequestParameter(@RequestParam boolean param) throws Exception {
            throw new MissingServletRequestParameterException("Quando um parametro é obrigatorio. Necessário informa-lo na requisicao.", "Campo 'param'");
        }

        @PostMapping(value = "/httpMediaTypeNotSupported", consumes = MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody String getHttpMediaTypeNotSupported(@RequestBody Boolean param) throws Exception {
            throw new HttpMediaTypeNotSupportedException("Formato de conteudo do http. Alguem enviou um html onde era um json.");
        }

        @PostMapping(value = "/methodArgumentNotValidException", consumes = MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody String getMethodArgumentNotValidException(@RequestBody @Valid TestUserBean usuario) throws Exception {
            MethodParameter parametroQueDeuErrado = null;
            BindingResult resultadoDaValidacaoQueFalhou = null;
            throw new MethodArgumentNotValidException(parametroQueDeuErrado, resultadoDaValidacaoQueFalhou);
        }

        @GetMapping(value = "/constraintViolationException")
        public @ResponseBody String getInvalidDefinitionException(@RequestParam("num") @Valid @NotNull @Min(1) @Max(4) String num) throws Exception {
            throw new ConstraintViolationException("Falhou na validação do BeanValidation do spring.", null);
        }

        @PostMapping(value = "/httpMessageNotReadableException")
        public @ResponseBody String getHttpMessageNotReadableException(@RequestBody TestUserBean usuario) throws Exception {
            HttpInputMessage headerAndBody = null;
            throw new HttpMessageNotReadableException("Quando o JsonParser encontra algum problema ou campo", headerAndBody);
        }

        @GetMapping(value = "/httpMessageNotWritableException")
        public @ResponseBody String getHttpMessageNotWritableException() throws Exception {
            throw new HttpMessageNotWritableException("Problema de conversao de objetos com o Spring. Veja campos Getter ou Setter do objeto.");
        }

        @GetMapping(value = "/noHandlerFoundException")
        public @ResponseBody String getNoHandlerFoundException() throws Exception {
            throw new NoHandlerFoundException("nenhum controler ou metodo encontrado para essa rota!", "/test/noHandlerFoundException", HttpHeaders.EMPTY);
        }

        @GetMapping(value = "/methodArgumentTypeMismatchException/{id}")
        public @ResponseBody String getMethodArgumentTypeMismatchException(@PathVariable("id") Long id) throws Exception {
            Throwable cause = null;
            String msg = "EX: O parametro/path do controller informa um variavel do tipo long, e foi passado uma string";
            Class<Long> reqType = long.class;
            MethodParameter methodParam = null;
            String nomeParam = "Nome do param";
            throw new MethodArgumentTypeMismatchException(msg, reqType, nomeParam, methodParam, cause);
        }

        @GetMapping(value = "/feignException")
        public @ResponseBody String getFeignException() throws Exception {
            throw FeignException.errorStatus("", Response.builder().status(INTERNAL_SERVER_ERROR.value()).build());
        }

        @GetMapping("/conversionFailedException")
        public ResponseEntity<GeneroLivro> obtemInfraException(@RequestParam("generoLivro") GeneroLivro generoLivro) {
            return ResponseEntity.ok(generoLivro);
        }
    }

}
