package br.com.bradesco.kit.srv.port.output;

import br.com.bradesco.kit.srv.domain.entity.Livro;

import java.util.List;
import java.util.Optional;

public interface IEstoque {

    List<Livro> consultaLivros(Integer page, Integer pageSize);

    Optional<Livro> consultaLivroporId(Integer id);

    List<Livro> consultaLivroporIds(Integer... id);

    void incluirNovoLivro(Livro novoLivro);

    void removerLivro(Integer id);

}
