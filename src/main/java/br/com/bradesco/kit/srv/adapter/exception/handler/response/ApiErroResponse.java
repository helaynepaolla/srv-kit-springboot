package br.com.bradesco.kit.srv.adapter.exception.handler.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
// @JsonTypeIdResolver(CaixaBaixaClassResolverConfig.class)
@JsonTypeName("apierro")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class ApiErroResponse {
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("codigoErro")
    private Integer codigoErro;
    @JsonProperty("mensagem")
    private String mensagem;
    @JsonProperty("mensagemDetalhada")
    private String mensagemDetalhada;
    @JsonProperty("subErros")
    private List<ApiSubErroResponse> subErros = new ArrayList<>();

    private ApiErroResponse() {
        timestamp = LocalDateTime.now();
    }

    public ApiErroResponse(HttpStatus status) {
        this();
        this.status = status;
        this.codigoErro = status.value();
    }

    public ApiErroResponse(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.codigoErro = status.value();
        this.mensagem = "Erro inesperado";
        this.mensagemDetalhada = ex.getLocalizedMessage();
    }

    public ApiErroResponse(HttpStatus status, String mensagem, Throwable ex) {
        this();
        this.status = status;
        this.codigoErro = status.value();
        this.mensagem = mensagem;
        this.mensagemDetalhada = ex.getLocalizedMessage();
    }

    private void addSubErro(ApiSubErroResponse subError) {
        if (subErros == null) {
            subErros = new ArrayList<>();
        }
        subErros.add(subError);
    }

    private void addValidacaoErro(String object, String field, Object rejectedValue, String message) {
        addSubErro(new ApiValidacaoErroResponse(object, field, rejectedValue, message));
    }

    private void addValidacaoErro(String object, String message) {
        addSubErro(new ApiValidacaoErroResponse(object, message));
    }

    public void addErroNeogocio(String codigo, String message) {
        addSubErro(new SubErroNegocio(codigo, message));
    }

    private void addValidacaoErro(FieldError fieldError) {
        this.addValidacaoErro(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidacaoErro);
    }

    private void addValidacaoErro(ObjectError objectError) {
        this.addValidacaoErro(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public void addValidacaoErro(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidacaoErro);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private void addValidacaoErro(ConstraintViolation<?> cv) {
        this.addValidacaoErro(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }

    public void addValidacaoErro(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidacaoErro);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Integer getCodigoErro() {
        return codigoErro;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagemDetalhada() {
        return mensagemDetalhada;
    }

    public void setMensagemDetalhada(String mensagemDetalhada) {
        this.mensagemDetalhada = mensagemDetalhada;
    }

    public List<ApiSubErroResponse> getSubErros() {
        return subErros;
    }


}
