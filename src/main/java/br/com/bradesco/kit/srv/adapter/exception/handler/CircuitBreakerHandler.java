package br.com.bradesco.kit.srv.adapter.exception.handler;

import br.com.bradesco.kit.srv.adapter.exception.handler.response.ApiErroResponse;
import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfraCallNotPermitedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@ControllerAdvice
public class CircuitBreakerHandler {

    private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(CircuitBreakerHandler.class);

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    /**
     * Handles Circuit Breaker CallNotPermittedException. Lancado quando o Circuit Breaker está com status OPEN ou HALF_OPEN.
     *
     * @param ex the InfraCallNotPermitedException
     * @return the ApiError objeto
     */
    @Value("${resilience4j.circuitbreaker.instances.srvKitSpringboot.waitDurationInOpenState}")
    private String timeOpenState;

    @Value("${resilience4j.circuitbreaker.instances.srvKitSpringboot.permittedNumberOfCallsInHalfOpenState}")
    private String timeHalfOpenState;

    @ExceptionHandler(InfraCallNotPermitedException.class)
    protected ResponseEntity<Object> handleCallNotPermittedException(InfraCallNotPermitedException ex) {
        var apiErro = new ApiErroResponse(SERVICE_UNAVAILABLE);

        circuitBreakerRegistry.getAllCircuitBreakers()
                .forEach(circuitBreaker -> apiErro.setMensagem(String.format("LogCircuit breaker '%s' em status '%s'. Ele se mantem em status OPEN por '%s' e HALF_OPEN por '%s'",
                        circuitBreaker.getName(), circuitBreaker.getState(), timeOpenState, timeHalfOpenState)));
        return buildResponseEntity(apiErro, ex);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErroResponse apiErro, Exception ex) {
        LOGGER_TECNICO.error("Excecao sendo capturada pelo CircuitBreakerHandler, APIErrorCode: {}, Mensagem: {}, Excecao: ", apiErro.getCodigoErro(), apiErro.getMensagem(), ex);
        return new ResponseEntity<>(apiErro, apiErro.getStatus());
    }

}