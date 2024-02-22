package br.com.bradesco.kit.srv.domain.exception;

public class QuantidadeEstoqueInsuficienteException extends BusinessException {

    public QuantidadeEstoqueInsuficienteException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorCode, errorMsg, cause);
    }

    public QuantidadeEstoqueInsuficienteException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, errorMsg, cause);
    }

    public QuantidadeEstoqueInsuficienteException(Integer errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public QuantidadeEstoqueInsuficienteException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

}
