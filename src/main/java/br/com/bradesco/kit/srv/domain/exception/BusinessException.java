package br.com.bradesco.kit.srv.domain.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String errorCode;
    private final String errorMsg;

    public BusinessException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = Integer.toString(errorCode);
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = Integer.toString(errorCode);
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrCode() {
        return errorCode;
    }

    public String getErrMsg() {
        return errorMsg;
    }

}
