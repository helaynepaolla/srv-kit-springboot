package br.com.bradesco.kit.srv.adapter.exception.handler;

import br.com.bradesco.kit.srv.adapter.exception.handler.response.ApiErroResponse;
import br.com.bradesco.kit.srv.domain.exception.LivroNaoEncontradoException;
import br.com.bradesco.kit.srv.domain.exception.ServicoIndisponivelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

/**
 * Exception Handler que trata erros específicos de negócio.
 * <p>
 * ATENCAO: Alterar apenas no caso de quiser mapear novas excecoes
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BusinessExceptionHandler {

    private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    /**
     * Handle Exception, handle generic LivroNaoEncontradoException.class
     *
     * @param ex the LivroNaoEncontradoException
     * @return the ApiErro objeto
     */
    @ExceptionHandler(LivroNaoEncontradoException.class)
    public ResponseEntity<Object> handleException(LivroNaoEncontradoException ex) {
        var apiErro = new ApiErroResponse(NO_CONTENT, ex);
        apiErro.addErroNeogocio("04", "item-nao-encontrado");
        return buildResponseEntity(apiErro, ex);
    }

    /**
     * Handle Exception, handle generic ServicoIndisponivelException.class
     *
     * @param ex the ServicoIndisponivelException
     * @return the ApiErro objeto
     */
    @ExceptionHandler(ServicoIndisponivelException.class)
    public ResponseEntity<Object> handleException(ServicoIndisponivelException ex) {
        var apiErro = new ApiErroResponse(SERVICE_UNAVAILABLE, ex);
        apiErro.setMensagem(ex.getMessage());
        if (ex.getCause() != null) {
            apiErro.setMensagemDetalhada(ex.getCause().getMessage());
        }
        return buildResponseEntity(apiErro, ex);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErroResponse apiErro, Exception ex) {
        LOGGER_TECNICO.error("Excecao sendo capturada, APIErrorCode: {}, Mensagem: {}, Excecao: ", apiErro.getCodigoErro(), apiErro.getMensagem(), ex);
        return new ResponseEntity<>(apiErro, apiErro.getStatus());
    }

}
