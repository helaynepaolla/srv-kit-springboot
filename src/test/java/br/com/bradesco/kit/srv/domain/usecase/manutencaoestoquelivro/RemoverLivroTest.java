package br.com.bradesco.kit.srv.domain.usecase.manutencaoestoquelivro;

import br.com.bradesco.enge.logcloud.canal.ReturnCode;
import br.com.bradesco.kit.srv.domain.usecase.ManutencaoEstoqueLivro;
import br.com.bradesco.kit.srv.port.output.IEstoque;
import br.com.bradesco.kit.srv.port.output.ILogServicoCanal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RemoverLivroTest {
    @Mock
    private IEstoque estoque;
    @Mock
    private ILogServicoCanal log;

    @Test
    void consultaLivrosList() {
        Integer id = 1;
        ManutencaoEstoqueLivro subject = new ManutencaoEstoqueLivro(estoque, log);

        subject.removerLivro(id);

        then(estoque).should().removerLivro(id);
        then(log).should().logExclusaoLivro(ReturnCode.SUCESSO, id);
    }
}
