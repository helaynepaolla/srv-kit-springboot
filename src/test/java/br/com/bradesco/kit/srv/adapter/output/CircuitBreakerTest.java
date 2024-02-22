package br.com.bradesco.kit.srv.adapter.output;

import br.com.bradesco.kit.srv.adapter.output.resttemplate.EstoqueRestTemplate;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerAutoConfiguration;
import io.github.resilience4j.decorators.Decorators;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import org.awaitility.Durations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = { "spring.main.allow-bean-definition-overriding=true", "spring.redis.port=6370",
        "spring.redis.host=localhost" })
@Import(value = { CircuitBreakerAutoConfiguration.class })
@Nested
@EnableAspectJAutoProxy
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CircuitBreakerTest {

    @Mock
    private RestTemplate restTemplate;
    private EstoqueRestTemplate subject;

    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .slidingWindowSize(5)
            .minimumNumberOfCalls(5)
            .waitDurationInOpenState(Duration.ofMillis(4000))
            .permittedNumberOfCallsInHalfOpenState(5)
            .failureRateThreshold(50)
            .automaticTransitionFromOpenToHalfOpenEnabled(true)
            .build();
    CircuitBreaker circuitBreaker = CircuitBreaker.of("srvKitSpringbootTest", circuitBreakerConfig);

    @BeforeEach
    void setup() {
        this.subject = mock(EstoqueRestTemplate.class);
    }

    @Test
    void circuitBreakerDeveRetornarCircuitoFechado() {

        Integer maxFailuresBeforeOpenCircuit = 4;

        // Given
        CheckedRunnable checkedRunnable = circuitBreaker.decorateCheckedRunnable(() -> {
            throw new RuntimeException("Error");
        });

        // When
        for (int i = 0; i < maxFailuresBeforeOpenCircuit; i++) {
            Try.run(checkedRunnable::run).onFailure(throwable -> {
            });
        }

        // Then
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }

    @Test
    void circuitBreakerDeveRetornarCircuitoAberto() {

        Integer neededFailuresToOpenCircuit = 5;

        // Given
        CheckedRunnable checkedRunnable = circuitBreaker.decorateCheckedRunnable(() -> {
            throw new RuntimeException("Error");
        });

        // When
        for (int i = 0; i < neededFailuresToOpenCircuit; i++) {
            Try.run(checkedRunnable::run).onFailure(throwable -> {
            });
        }

        // Then
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }

    @Test
    void circuitBreakerDeveRetornarMetodoFallback() {

        Integer ID_LIVRO1 = 000;

        circuitBreaker.transitionToOpenState();

        Supplier<Optional<Livro>> decoratedSubject = Decorators
                .ofSupplier(() -> subject.consultaLivroporId(ID_LIVRO1))
                .withCircuitBreaker(circuitBreaker)
                .withFallback(asList(CallNotPermittedException.class),
                        (e) -> subject.fallbackConsultaLivroPorId(ID_LIVRO1,
                                new RuntimeException("Falha na requisição HTTP")))
                .decorate();

        decoratedSubject.get();

        verify(subject, times(1)).fallbackConsultaLivroPorId(anyInt(), any(Exception.class));
    }

    @Test
    void circuitBreakerDeveRetornarCircuitoMeioAberto() {

        Integer neededFailuresToOpenCircuit = 5;

        // Given
        CheckedRunnable checkedRunnable = circuitBreaker.decorateCheckedRunnable(() -> {
            throw new RuntimeException("Error");
        });

        // When
        for (int i = 0; i < neededFailuresToOpenCircuit; i++) {
            Try.run(checkedRunnable::run).onFailure(throwable -> {
            });
        }

        // Then
        await().pollInterval(Durations.FIVE_SECONDS).atLeast(Durations.FIVE_SECONDS).atMost(Durations.TEN_SECONDS)
                .untilAsserted(() -> {
                    assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.HALF_OPEN);
                });
    }

}
