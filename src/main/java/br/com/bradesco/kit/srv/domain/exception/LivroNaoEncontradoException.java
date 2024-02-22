package br.com.bradesco.kit.srv.domain.exception;

public class LivroNaoEncontradoException extends BusinessException {

    private static final long serialVersionUID = 1L;


    public LivroNaoEncontradoException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, errorMsg, cause);
    }

    public LivroNaoEncontradoException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
