package br.com.bradesco.kit.srv.port.output;

import br.com.bradesco.enge.logcloud.canal.ReturnCode;
import br.com.bradesco.kit.srv.domain.entity.Livro;

import java.util.List;

public interface ILogServicoCanal {

    void logConsultaTodosLivros(ReturnCode returnCode, List<Livro> livros);

    void logConsultaLivroPorId(ReturnCode returnCode, Integer idEntrada, Livro livroRetornado);

    void logInclusaoLivro(ReturnCode returnCode, Livro livroIncluido);

    void logExclusaoLivro(ReturnCode returnCode, Integer idExcluido);

    void logConsultaLivroPorIdException(ReturnCode returnCode, Integer idEntrada, RuntimeException ex);

}
