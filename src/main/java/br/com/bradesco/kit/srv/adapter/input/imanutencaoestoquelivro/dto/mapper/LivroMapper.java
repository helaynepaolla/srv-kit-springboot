package br.com.bradesco.kit.srv.adapter.input.imanutencaoestoquelivro.dto.mapper;

import br.com.bradesco.kit.srv.adapter.input.imanutencaoestoquelivro.dto.LivroDTO;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Interface necessaria para uso com o framework MapStrutc
 */
@Mapper
public interface LivroMapper {

    LivroMapper INSTANCE = Mappers.getMapper(LivroMapper.class);

    LivroDTO livroToLivroDto(Livro livro);

    Livro livroDtoToLivro(LivroDTO livro);


    List<LivroDTO> listLivroToListLivroDto(List<Livro> livro);

    List<Livro> listLivroDTOToListLivro(List<LivroDTO> livro);


}
