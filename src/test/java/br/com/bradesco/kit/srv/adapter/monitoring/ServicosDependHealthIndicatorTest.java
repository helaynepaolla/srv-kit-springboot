package br.com.bradesco.kit.srv.adapter.monitoring;

import br.com.bradesco.kit.srv.config.HealthCheckEndpointConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicosDependHealthIndicatorTest {
    private ServicosDependHealthIndicator subject;

    @Mock
    private HealthCheckEndpointConfig healthCheckEndpointConfig;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        List<String> urls = Arrays.asList("http://localhost:8080/health", "http://localhost:8081/health");
        when(healthCheckEndpointConfig.getEndpoints()).thenReturn(urls);
        subject = new ServicosDependHealthIndicator(healthCheckEndpointConfig, restTemplate);
    }

    @Test
    void testDoHealthCheck_todosServicosUP() {
        Health health = new Health.Builder().up().build();
        when(restTemplate.getForEntity(anyString(), eq(Object.class))).thenReturn(new ResponseEntity<>(health, HttpStatus.OK));

        Mono<Health> monoHealth = subject.doHealthCheck(new Health.Builder());
        Health result = monoHealth.block();
        assertNotNull(result);
        assertEquals(Status.UP, result.getStatus());
    }

    @Test
    void testDoHealthCheck_algunsServicosDOWN() {
        Health healthUp = new Health.Builder().up().build();
        Health healthDown = new Health.Builder().down().build();
        when(restTemplate.getForEntity("http://localhost:8080/health", Object.class))
                .thenReturn(new ResponseEntity<>(healthUp, HttpStatus.OK));
        when(restTemplate.getForEntity("http://localhost:8081/health", Object.class))
                .thenReturn(new ResponseEntity<>(healthDown, HttpStatus.REQUEST_TIMEOUT));

        Mono<Health> monoHealth = subject.doHealthCheck(new Health.Builder());
        Health result = monoHealth.block();
        assertNotNull(result);
        assertEquals(Status.DOWN, result.getStatus());
    }
}
