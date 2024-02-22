package br.com.bradesco.kit.srv.adapter.exception.infrastructure;

public class InfraCallNotPermitedException extends InfrastructureException {

    public InfraCallNotPermitedException(Throwable cause) {
        super("CIRCUITBREAKER-CODE", "Circuit Breaker não fechado", cause);
    }

    public InfraCallNotPermitedException(String message) {
        super("CIRCUITBREAKER-CODE", message);
    }
}
