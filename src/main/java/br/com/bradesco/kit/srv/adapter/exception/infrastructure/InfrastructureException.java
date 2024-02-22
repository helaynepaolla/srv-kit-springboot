package br.com.bradesco.kit.srv.adapter.exception.infrastructure;

public class InfrastructureException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String errorCode;

    public InfrastructureException(String errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
    }

    public InfrastructureException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public String getErrCode() {
        return errorCode;
    }

    public String getErrMsg() {
        return this.getMessage();
    }

}
