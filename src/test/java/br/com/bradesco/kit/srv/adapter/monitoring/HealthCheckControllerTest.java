package br.com.bradesco.kit.srv.adapter.monitoring;

import br.com.bradesco.kit.srv.config.redis.TestRedisConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest(classes = TestRedisConfiguration.class)
@AutoConfigureMockMvc
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void requisitaEndpointHealthLiveness_ViaGet_deveRetornar200ComServicoUP()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/health/liveness"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void requisitaEndpointHealthReadiness_ViaGet_deveRetornar200ComServicoUP()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/health/readiness"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void requisitaEndpointMetrics_ViaGet_deveRetornar404()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/metrics"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void requisitaEndpointInfo_ViaGet_deveRetornar404()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/info"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void requisitaEndpointEnv_ViaGet_deveRetornar404()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/env"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void requisitaEndpointHeapDump_ViaGet_deveRetornar404()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/heapdump"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void requisitaEndpointMappings_ViaGet_deveRetornar404()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/mappings"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
