package br.com.bradesco.kit.srv.domain.usecase.manutencaoestoquelivro;

import br.com.bradesco.enge.logcloud.canal.ReturnCode;
import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.domain.exception.QuantidadeEstoqueInsuficienteException;
import br.com.bradesco.kit.srv.domain.usecase.ManutencaoEstoqueLivro;
import br.com.bradesco.kit.srv.port.output.IEstoque;
import br.com.bradesco.kit.srv.port.output.ILogServicoCanal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class IncluirNovoLivroTest {
    @Mock
    private IEstoque estoque;
    @Mock
    private ILogServicoCanal log;

    @Test
    void incluirNovoLivro() {
        Livro livro = LivroFactoryBot.createLivro(1);
        ManutencaoEstoqueLivro subject = new ManutencaoEstoqueLivro(estoque, log);

        subject.incluirNovoLivro(livro);

        then(estoque).should().incluirNovoLivro(livro);
        then(log).should().logInclusaoLivro(ReturnCode.SUCESSO, livro);
    }

    @Test
    void verificaExcecaoDeSaldo() {
        int saldoInsuficiente = 0;
        Livro livro = LivroFactoryBot.createLivro(1, saldoInsuficiente);
        ManutencaoEstoqueLivro subject = new ManutencaoEstoqueLivro(estoque, log);

        QuantidadeEstoqueInsuficienteException thrown = assertThrows(
                QuantidadeEstoqueInsuficienteException.class,
                () -> subject.incluirNovoLivro(livro),
                "Expected exception to throw, but it didn't"
        );
        then(log).should().logInclusaoLivro(ReturnCode.ERRO_NEGOCIO, livro);
        assertEquals("Quantidade em estoque invalida", thrown.getMessage());
    }
}
