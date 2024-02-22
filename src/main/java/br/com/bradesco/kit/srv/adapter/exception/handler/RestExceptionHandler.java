package br.com.bradesco.kit.srv.adapter.exception.handler;

import br.com.bradesco.kit.srv.adapter.exception.handler.response.ApiErroResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Exception Handler que trata erros específicos do artefato + erros de tratamento Spring e Feign.
 * <p>
 * Para poder capturar erros específicos do Spring Web está sendo herdado ResponseEntityExceptionHandler.
 * <p>
 * ATENCAO: Alterar apenas no caso de quiser mapear novas excecoes
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Handle MissingServletRequestParameterException. Acionado quando um campo request param nao foi informado.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiErro objeto
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String erro = ex.getParameterName() + " parametro nao informado";
        return buildResponseEntity(new ApiErroResponse(BAD_REQUEST, erro, ex), ex);
    }

    /**
     * Handle HttpMediaTypeNotSupportedException. Acionado quando JSON eh invalido.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiErro objeto
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        var builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type nao eh suportado. Media suportada sao ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiErroResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0,
                builder.length() - 2), ex), ex);
    }

    /**
     * Handle MethodArgumentNotValidException. Acionado quando falha na validacao @Valid.
     *
     * @param ex      the MethodArgumentNotValidException lancado quando @Valid detecta inconsistencia
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiErro objeto
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem("Erro de validacao");
        apiErro.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiErro.addValidacaoErro(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiErro, ex);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Lancado quando @Validated falha.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError objeto
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem("Erro validacao");
        return buildResponseEntity(apiErro, ex);
    }

    /**
     * Handle HttpMessageNotReadableException. Ocorre quando requisicao JSON eh mal formada (Malformed).
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiErro objeto
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var servletWebRequest = (ServletWebRequest) request;
        LOGGER_TECNICO.debug("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        var error = "Malformed JSON request";
        return buildResponseEntity(new ApiErroResponse(HttpStatus.BAD_REQUEST, error, ex), ex);
    }

    /**
     * Handle HttpMessageNotWritableException. Para evitar a exceção é definir um método getter para cada propriedade do objeto que queremos retornar em JSON
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiErro objeto
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var error = "Erro ao realizar escrita de saida JSON";
        return buildResponseEntity(new ApiErroResponse(INTERNAL_SERVER_ERROR, error, ex), ex);
    }

    /**
     * Handle NoHandlerFoundException. Se configurado no spring quando uma recurso ou rota é chamado pelo Dispatcher e não encontrado essa exceção é lançada.
     *
     * @param ex      NoHandlerFoundException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiErro objeto
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem(String.format("Nao pode encontrar o %s metodo para a URL %s",
                ex.getHttpMethod(), ex.getRequestURL()));
        apiErro.setMensagemDetalhada(ex.getMessage());
        return buildResponseEntity(apiErro, ex);
    }

    /**
     * Handle HandleBindException. É usado o MethodArgumentNotValidException no lugar nas versão mais novas e será removido.
     * <p>
     * Ficará deprecated a partir do Spring Framework 6
     *
     * @param ex      BindException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiErro objeto
     * @see ResponseEntityExceptionHandler
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem("Erro validacao");
        apiErro.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiErro.addValidacaoErro(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiErro, ex);
    }

    /**
     * Handle NoHandlerFoundException. Quando um path para um controller com parametro é invocado com outro tipo. Ex `@param("ID") long id` e é passado uma string `/?id=aaaaa` para a url.
     *
     * @param ex      MethodArgumentTypeMismatchException
     * @param request WebRequest
     * @return the ApiErro objeto
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem(String.format("O parametro '%s' do valor '%s' nao pode ser convertido para o tipo '%s'",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
        apiErro.setMensagemDetalhada(ex.getMessage());
        return buildResponseEntity(apiErro, ex);
    }

    /**
     * Handle FeignException, relacionado como Feign Framework
     *
     * @param ex the FeignException
     * @return the ApiErro objeto
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException ex) {
        var apiErro = new ApiErroResponse(Objects.requireNonNull(HttpStatus.resolve(ex.status())), ex);
        apiErro.setMensagemDetalhada(ex.getMessage());
        return buildResponseEntity(apiErro, ex);
    }

    /**
     * Handle ConversionFailedException, relacionado com o Core Spring Framework
     *
     * @param ex the ConversionFailedException
     * @return the ApiErro objeto
     */
    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Object> handleConversionFailedException(ConversionFailedException ex) {
        var apiErro = new ApiErroResponse(BAD_REQUEST, ex);
        return buildResponseEntity(apiErro, ex);
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the ApiErro objeto
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        var apiErro = new ApiErroResponse(INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(apiErro, ex);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErroResponse apiErro, Exception ex) {
        LOGGER_TECNICO.error("Excecao sendo capturada pelo RestExceptionHandler, APIErrorCode: {}, Mensagem: {}, Excecao: ", apiErro.getCodigoErro(), apiErro.getMensagem(), ex);
        return new ResponseEntity<>(apiErro, apiErro.getStatus());
    }

}
