package br.com.bradesco.kit.srv.domain.usecase;

import br.com.bradesco.enge.logcloud.canal.ReturnCode;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.domain.exception.BusinessException;
import br.com.bradesco.kit.srv.domain.exception.LivroNaoEncontradoException;
import br.com.bradesco.kit.srv.domain.exception.QuantidadeEstoqueInsuficienteException;
import br.com.bradesco.kit.srv.port.input.IManutencaoEstoqueLivro;
import br.com.bradesco.kit.srv.port.output.IEstoque;
import br.com.bradesco.kit.srv.port.output.ILogServicoCanal;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ManutencaoEstoqueLivro implements IManutencaoEstoqueLivro {

    private final IEstoque estoqueOutputAdapter;
    private final ILogServicoCanal logServicoCanal;

    public ManutencaoEstoqueLivro(IEstoque estoqueOutputAdapter, ILogServicoCanal logServicoCanal) {
        this.estoqueOutputAdapter = estoqueOutputAdapter;
        this.logServicoCanal = logServicoCanal;
    }

    @Override
    public List<Livro> consultaLivros(Integer page, Integer pageSize) {
        List<Livro> retorno = estoqueOutputAdapter.consultaLivros(page, pageSize);

        logServicoCanal.logConsultaTodosLivros(ReturnCode.SUCESSO, retorno);

        return retorno;
    }

    @Override
    public Optional<Livro> consultaLivroPorId(Integer id) {
             Optional<Livro> result = estoqueOutputAdapter.consultaLivroporId(id);
        if (!result.isPresent()) {
            var ex = new LivroNaoEncontradoException(String.valueOf(HttpStatus.NOT_FOUND.value()), "NOT FOUND - Livro" +
                    " não " +
                    "encontrado");
            logServicoCanal.logConsultaLivroPorIdException(ReturnCode.ERRO_GRAVE, id, ex);
            throw ex;
        }
        var livro = result.get();
        logServicoCanal.logConsultaLivroPorId(ReturnCode.SUCESSO, id, livro);
        return Optional.of(livro);
    }

    @Override
    public void incluirNovoLivro(Livro novoLivro) throws BusinessException {
        if (novoLivro.getQtdEstoque() <= 1) {
            var ex = new QuantidadeEstoqueInsuficienteException("10101", "Quantidade em estoque invalida");
            logServicoCanal.logInclusaoLivro(ReturnCode.ERRO_NEGOCIO, novoLivro);
            throw ex;
        }
        estoqueOutputAdapter.incluirNovoLivro(novoLivro);
        logServicoCanal.logInclusaoLivro(ReturnCode.SUCESSO, novoLivro);
    }

    @Override
    public void removerLivro(Integer id) {
        estoqueOutputAdapter.removerLivro(id);
        logServicoCanal.logExclusaoLivro(ReturnCode.SUCESSO, id);
    }

    @Override
    public List<Livro> consultaVariosLivrosPorId(Integer[] ids) {
        Integer[] validIds = Stream.of(ids)
                .filter(id -> id > 0) //remove invalid ids
                .toArray(Integer[]::new);
        return estoqueOutputAdapter.consultaLivroporIds(validIds);
    }
}
