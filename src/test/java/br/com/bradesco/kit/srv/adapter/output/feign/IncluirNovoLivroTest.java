package br.com.bradesco.kit.srv.adapter.output.feign;

import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfraCallNotPermitedException;
import br.com.bradesco.kit.srv.domain.entity.Livro;
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
class IncluirNovoLivroTest {
    private static final Integer ID_LIVRO1 = 1;
    @Mock
    private IManutencaoEstoqueFeign client;
    @InjectMocks
    private EstoqueFeign subject;

    @Test
    void incluirNovoLivroSucesso() {
        Livro livro = LivroFactoryBot.createLivro(ID_LIVRO1);

        subject.incluirNovoLivro(livro);

        then(client).should().criarNovoLivro(any());
    }

    @Test
    void incluirNovoLivroFallback() {
        Livro livro = LivroFactoryBot.createLivro(ID_LIVRO1);
        RuntimeException ex = new RuntimeException("Gerando falha para invocacao do fallback");

        assertThrows(
            InfraCallNotPermitedException.class,
            () -> subject.fallbackIncluirNovoLivro(livro, ex),
            "Nao lancou excecao esperada");
    }

    @Test
    void verificaExcecaoDoFeign() {
        Livro livro = LivroFactoryBot.createLivro(ID_LIVRO1);
        FeignException ex = new feign.codec.EncodeException("somethings wrong happend");
        given(client.criarNovoLivro(any())).willThrow(ex);

        assertThrows(
                FeignException.class,
                () -> subject.incluirNovoLivro(livro),
                "Expected exception to throw, but it didn't");
    }

}
