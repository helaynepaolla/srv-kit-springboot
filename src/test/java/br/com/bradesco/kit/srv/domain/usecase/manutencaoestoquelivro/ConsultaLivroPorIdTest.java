package br.com.bradesco.kit.srv.domain.usecase.manutencaoestoquelivro;

import br.com.bradesco.enge.logcloud.canal.ReturnCode;
import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.domain.usecase.ManutencaoEstoqueLivro;
import br.com.bradesco.kit.srv.port.output.IEstoque;
import br.com.bradesco.kit.srv.port.output.ILogServicoCanal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ConsultaLivroPorIdTest {
    @Mock
    private IEstoque estoque;
    @Mock
    private ILogServicoCanal log;

    @Test
    void consultaLivrosList() {
        Livro livro = LivroFactoryBot.createLivro(1);
        given(estoque.consultaLivroporId(anyInt())).willReturn(Optional.of(livro));

        ManutencaoEstoqueLivro subject = new ManutencaoEstoqueLivro(estoque, log);
        subject.consultaLivroPorId(1);

        then(estoque).should().consultaLivroporId(1);
        then(log).should().logConsultaLivroPorId(ReturnCode.SUCESSO, 1, livro);
    }

    @Test
    void verificaExcecaoCasoNaoEncontre() {
        given(estoque.consultaLivroporId(1)).willReturn(Optional.empty());
        ManutencaoEstoqueLivro subject = new ManutencaoEstoqueLivro(estoque, log);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> subject.consultaLivroPorId(1),
                "Expected exception to throw, but it didn't"
        );
        then(log).should().logConsultaLivroPorIdException(ReturnCode.ERRO_GRAVE, 1, thrown);
        assertEquals("NOT FOUND - Livro não encontrado", thrown.getMessage());
    }
}
