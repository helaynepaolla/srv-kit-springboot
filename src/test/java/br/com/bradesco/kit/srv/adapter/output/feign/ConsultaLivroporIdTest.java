package br.com.bradesco.kit.srv.adapter.output.feign;

import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.adapter.output.feign.dto.LivroDTO;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ConsultaLivroporIdTest {
    private static final Integer ID_LIVRO1 = 1;

    @Mock
    private IManutencaoEstoqueFeign client;
    @Mock
    private IManutencaoEstoqueFeignFallback fallbackClient;
    @InjectMocks
    private EstoqueFeign subject;

    @Test
    void consultaLivroporId() {
        LivroDTO livro = LivroFactoryBot.createOutputFeignLivroDto(ID_LIVRO1);
        given(client.retornaLivro(1)).willReturn(livro);

        Optional<Livro> result = subject.consultaLivroporId(ID_LIVRO1);

        then(client).should().retornaLivro(ID_LIVRO1);
        Livro livroResult = result.get();
        assertThat(livroResult.getTitulo()).isEqualTo(livro.titulo());
    }

    @Test
    void consultaLivroporIdFallback() {
        LivroDTO livro = LivroFactoryBot.createOutputFeignLivroDto(ID_LIVRO1);
        RuntimeException ex = new RuntimeException("Gerando falha para invocacao do fallback");
        given(fallbackClient.retornaLivroFallback(ID_LIVRO1)).willReturn(livro);

        Optional<Livro> result = subject.fallbackConsultaLivroPorId(ID_LIVRO1, ex);

        then(fallbackClient).should().retornaLivroFallback(ID_LIVRO1);
        Livro livroResult = result.get();
        assertThat(livroResult.getTitulo()).isEqualTo(livro.titulo());
    }

    @Test
    void verificaExcecaoDoFeign() {
        FeignException ex = new feign.codec.EncodeException("somethings wrong happend");
        given(client.retornaLivro(1)).willThrow(ex);

        assertThrows(
                FeignException.class,
                () -> subject.consultaLivroporId(ID_LIVRO1),
                "Expected exception to throw, but it didn't");
    }
}
