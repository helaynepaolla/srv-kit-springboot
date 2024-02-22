package br.com.bradesco.kit.srv.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = HealthCheckEndpointConfig.class)
@TestConfiguration
@TestPropertySource(properties = {"health-check-endpoint-custom.endpoints=http://localhost:1080/mock-livraria"})
class HealthCheckEndpointConfigTest {

    @Autowired
    private HealthCheckEndpointConfig healthCheckEndpointConfig;

    @Test
    void testSetEndpoints() {
        List<String> newEndpoints = Arrays.asList("http://localhost:8080/health", "http://localhost:8081/health");
        healthCheckEndpointConfig.setEndpoints(newEndpoints);
        assertEquals(newEndpoints, healthCheckEndpointConfig.getEndpoints());
    }
}
