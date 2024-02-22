package br.com.bradesco.kit.srv.adapter.output;

import br.com.bradesco.kit.srv.config.SwaggerConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@DisplayName("Teste geral de Swagger")
@Import(SwaggerConfig.class)
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true", "spring.redis.port=6370",
        "spring.redis.host=localhost"})
@Nested
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class SwaggerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void requisitaEndpointSwaggerUI_ViaGet_deveRetornarHTTP200()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/swagger-ui/index.html"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void requisitaEndpointAPIDocs_ViaGet_deveRetornarHTTP200()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v3/api-docs"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
}
