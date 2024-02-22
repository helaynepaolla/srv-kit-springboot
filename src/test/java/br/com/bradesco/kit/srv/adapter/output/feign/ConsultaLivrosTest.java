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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConsultaLivrosTest {
    private static final Integer ID_LIVRO1 = 1;
    @Mock
    private IManutencaoEstoqueFeign client;
    @Mock
    private IManutencaoEstoqueFeignFallback fallbackClient;
    @InjectMocks
    private EstoqueFeign subject;

    @Test
    void consultaLivros() {
        LivroDTO livro = LivroFactoryBot.createOutputFeignLivroDto(ID_LIVRO1);
        given(client.retornaColecao()).willReturn(List.of(livro));

        List<Livro> result = subject.consultaLivros(1, 10);

        Livro expectedLivro = LivroFactoryBot.createLivro(livro);
        assertThat(result).isEqualTo(List.of(expectedLivro));
    }

    @Test
    void consultaLivrosFallback() {
        LivroDTO livro = LivroFactoryBot.createOutputFeignLivroDto(ID_LIVRO1);
        RuntimeException ex = new RuntimeException("Gerando falha para invocacao do fallback");
        given(fallbackClient.retornaColecaoFallback()).willReturn(List.of(livro));

        List<Livro> result = subject.fallbackConsultaLivros(1, 10, ex);

        Livro expectedLivro = LivroFactoryBot.createLivro(livro);
        assertThat(result).isEqualTo(List.of(expectedLivro));
    }

    @Test
    void verificaExcecaoDoFeign() {
        FeignException ex = new feign.codec.EncodeException("somethings wrong happend");
        given(client.retornaColecao()).willThrow(ex);

        assertThrows(
                FeignException.class,
                () -> subject.consultaLivros(1, 10),
                "Expected exception to throw, but it didn't");
    }
}
