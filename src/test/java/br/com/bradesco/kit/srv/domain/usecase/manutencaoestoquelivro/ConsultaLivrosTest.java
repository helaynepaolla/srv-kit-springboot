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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
class ConsultaLivrosTest {
    @Mock
    private IEstoque estoque;
    @Mock
    private ILogServicoCanal log;

    @Test
    void consultaLivrosList() {
        Livro livro = LivroFactoryBot.createLivro(1);
        List<Livro> livros = Arrays.asList(livro);
        given(estoque.consultaLivros(any(), any())).willReturn(livros);

        ManutencaoEstoqueLivro subject = new ManutencaoEstoqueLivro(estoque, log);
        subject.consultaLivros(2, 10);

        then(estoque).should().consultaLivros(2, 10);
        then(log).should().logConsultaTodosLivros(ReturnCode.SUCESSO, livros);
    }
}
