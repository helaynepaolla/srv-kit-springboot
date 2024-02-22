package br.com.bradesco.kit.srv.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class CircuitBreakerTransitionConfig {
    private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(CircuitBreakerTransitionConfig.class);

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    private void circuitBreakerTransitionStateHandler() {
        circuitBreakerRegistry.getAllCircuitBreakers()
                .forEach(circuitBreaker -> circuitBreaker.getEventPublisher().onStateTransition(
                        event -> LOGGER_TECNICO.warn("Circuit breaker {} mudando de status: {}", circuitBreaker.getName(), event.getStateTransition())
                ));

        circuitBreakerRegistry.getEventPublisher().onEntryAdded(
                addedEvent -> addedEvent.getAddedEntry().getEventPublisher().onStateTransition(
                        event -> LOGGER_TECNICO.warn("Evento: {}", event.getStateTransition())
                )
        );
    }
}
