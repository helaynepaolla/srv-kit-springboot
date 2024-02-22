package br.com.bradesco.kit.srv.domain.usecase.manutencaoestoquelivro;

import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.domain.usecase.ManutencaoEstoqueLivro;
import br.com.bradesco.kit.srv.port.output.IEstoque;
import br.com.bradesco.kit.srv.port.output.ILogServicoCanal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ConsultaVariosLivrosPorIdTest {
    private static final Integer ID_LIVRO1 = 1;
    @Mock
    private IEstoque estoque;
    @Mock
    private ILogServicoCanal log;

    @Test
    @DisplayName("Remover ids invalidos - Uma lista com 1 id valido e 2 invalidos")
    void consultaLivrariaSucesso1Item() throws Exception {
        Livro livro1 = LivroFactoryBot.createLivro(ID_LIVRO1);
        Integer[] idLivros = new Integer[]{0, ID_LIVRO1, -1};
        given(estoque.consultaLivroporIds(ID_LIVRO1)).willReturn(Arrays.asList(livro1));

        ManutencaoEstoqueLivro subject = new ManutencaoEstoqueLivro(estoque, log);
        List<Livro> result = subject.consultaVariosLivrosPorId(idLivros);

        then(estoque).should().consultaLivroporIds(ID_LIVRO1);
        List<Livro> expected = Arrays.asList(livro1);
        assertEquals(expected, result);
    }
}
