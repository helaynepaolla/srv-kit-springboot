package br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.mapper;

import br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.LivroDTO;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * pra entender a funcao dessa interface veja https://mapstruct.org/
 */

@Mapper
public interface LivroMapper {

    LivroMapper INSTANCE = Mappers.getMapper(LivroMapper.class);

    LivroDTO livroToLivroDto(Livro livro);

    Livro livroDtoToLivro(LivroDTO livro);


    List<LivroDTO> listLivroToListLivroDto(List<Livro> livro);

    List<Livro> listLivroDTOToListLivro(List<LivroDTO> livro);


}
