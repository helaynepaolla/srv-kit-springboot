package br.com.bradesco.kit.srv.adapter.exception.handler.response;

/**
 * Especifico para validacao de API
 */
public class ApiValidacaoErroResponse implements ApiSubErroResponse {
    private final String objeto;
    private final String mensagem;
    private String campo;
    private Object valorRejeitado;

    ApiValidacaoErroResponse(String objeto, String mensagem) {
        this.objeto = objeto;
        this.mensagem = mensagem;
    }

    public ApiValidacaoErroResponse(String objeto, String campo, Object valorRejeitado, String mensagem) {
        this.objeto = objeto;
        this.campo = campo;
        this.valorRejeitado = valorRejeitado;
        this.mensagem = mensagem;
    }

    public String getObjeto() {
        return objeto;
    }

    public String getCampo() {
        return campo;
    }

    public Object getValorRejeitado() {
        return valorRejeitado;
    }

    public String getMensagem() {
        return mensagem;
    }
}
