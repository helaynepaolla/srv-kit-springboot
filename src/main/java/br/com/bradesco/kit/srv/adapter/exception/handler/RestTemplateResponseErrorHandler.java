package br.com.bradesco.kit.srv.adapter.exception.handler;

import br.com.bradesco.kit.srv.domain.exception.ServicoIndisponivelException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.IOException;

/**
 * O objetivo aqui será capturar possíveis erros HTTP retornados de requisições via RestTemplate para recursos
 * externos e realizar os devidos tratamentos quanto ao erro a ser devolvido ao requisitante deste artefato.
 * <p>
 * Aqui é possível mitigar retornar a cadeia de erro que possa retornar de outro recurso externo, pois não
 * importa ao Client saber com detalhes os erros retornados de terceiros.
 *
 * @see org.springframework.web.client.ResponseErrorHandler
 * <p>
 * ATENCAO: Pode ser oportunamente alterado/ajustado para atender as necessidades
 */
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return (response.getStatusCode().isError());
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        ServicoIndisponivelException sie = new ServicoIndisponivelException(response.getStatusCode().value(),
                "Houve um erro que impediu atender a requisicao devido recurso dependente estar com problema" + response.getStatusText());

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            throw sie;
        } else {
            UnknownHttpStatusCodeException unkHttpStatusCode = new UnknownHttpStatusCodeException("",
                    response.getStatusCode().value(), response.getStatusText(),
                    response.getHeaders(), null, null);

            sie = new ServicoIndisponivelException(response.getStatusCode().value(),
                    "Houve um erro que impediu atender a requisicao devido recurso dependente estar com problema" + response.getStatusText(), unkHttpStatusCode);
        }
        throw sie;
    }
}
