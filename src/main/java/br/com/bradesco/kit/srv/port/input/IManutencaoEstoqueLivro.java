package br.com.bradesco.kit.srv.port.input;

import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.domain.exception.BusinessException;

import java.util.List;
import java.util.Optional;

public interface IManutencaoEstoqueLivro {

    List<Livro> consultaLivros(Integer page, Integer pageSize);

    Optional<Livro> consultaLivroPorId(Integer id);

    List<Livro> consultaVariosLivrosPorId(Integer[] ids);

    void incluirNovoLivro(Livro novoLivro) throws BusinessException;

    void removerLivro(Integer id);

}
