package br.com.bradesco.kit.srv.adapter.output.feign;

import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfraCallNotPermitedException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RemoverLivroTest {
    private static final Integer ID_LIVRO1 = 1;
    @Mock
    private IManutencaoEstoqueFeign client;
    @InjectMocks
    private EstoqueFeign subject;

    @Test
    void removerLivroSucesso() {
        subject.removerLivro(ID_LIVRO1);

        then(client).should().deletarLivro(ID_LIVRO1);
    }

    @Test
    void removerLivroFallback() {
        RuntimeException ex = new RuntimeException("Gerando falha para invocacao do fallback");

        assertThrows(
                InfraCallNotPermitedException.class,
                () -> subject.fallbackRemoverLivro(ID_LIVRO1, ex),
                "Nao lancou excecao esperada");
    }

    @Test
    void verificaExcecaoDoFeign() {
        FeignException ex = new feign.codec.EncodeException("somethings wrong happend");
        given(client.deletarLivro(any())).willThrow(ex);

        assertThrows(
                FeignException.class,
                () -> subject.removerLivro(ID_LIVRO1),
                "Expected exception to throw, but it didn't");
    }

}
