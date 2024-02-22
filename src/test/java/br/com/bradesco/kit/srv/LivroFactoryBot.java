package br.com.bradesco.kit.srv;

import br.com.bradesco.kit.srv.adapter.input.imanutencaoestoquelivro.dto.LivroDTO;
import br.com.bradesco.kit.srv.domain.entity.GeneroLivro;
import br.com.bradesco.kit.srv.domain.entity.Livro;

import java.math.BigDecimal;

public class LivroFactoryBot {
    /**
     *
     */
    private static final BigDecimal VALOR_PADRAO = BigDecimal.valueOf(3.23f);
    private static final int qtdEstoque = 5;
    private static final String titulo = "Titulo Teste ";
    private static final String autor = "Autor do ";
    private static final String editora = "editora global";
    private static final GeneroLivro generoLivro = GeneroLivro.ACAO;

    public static br.com.bradesco.kit.srv.adapter.output.feign.dto.LivroDTO createOutputFeignLivroDto(Integer id) {
        return new br.com.bradesco.kit.srv.adapter.output.feign.dto.LivroDTO(id, titulo, autor, editora,
                VALOR_PADRAO, qtdEstoque, generoLivro);

    }

    public static br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.LivroDTO createOutputResttemplateLivroDto(Integer id) {
        return new br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.LivroDTO(id, titulo, autor, editora,
                VALOR_PADRAO, qtdEstoque, generoLivro);
    }

    public static LivroDTO createInputLivroDto(Integer id) {
        return new LivroDTO(id, titulo + id, autor + id, editora, VALOR_PADRAO, 1, generoLivro);
    }

    public static Livro createLivro(Integer id) {
        return new Livro(id, titulo, autor, editora, VALOR_PADRAO, qtdEstoque, generoLivro);
    }

    public static Livro createLivro(Integer id, int qtdEstoque) {
        return new Livro(id, titulo, autor, editora, VALOR_PADRAO, qtdEstoque, generoLivro);
    }

    public static Livro createLivro(br.com.bradesco.kit.srv.adapter.output.feign.dto.LivroDTO livro) {
        return br.com.bradesco.kit.srv.adapter.output.feign.dto.mapper.LivroMapper.INSTANCE.livroDtoToLivro(livro);
    }

    public static Livro createLivro(br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.LivroDTO livro) {
        return br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.mapper.LivroMapper.INSTANCE.livroDtoToLivro(livro);
    }
}
