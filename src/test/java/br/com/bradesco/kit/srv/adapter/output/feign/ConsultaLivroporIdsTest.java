package br.com.bradesco.kit.srv.adapter.output.feign;

import br.com.bradesco.kit.srv.LivroFactoryBot;
import br.com.bradesco.kit.srv.adapter.output.feign.dto.LivroDTO;
import br.com.bradesco.kit.srv.adapter.output.feign.dto.mapper.LivroMapper;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ConsultaLivroporIdsTest {
    private static final Integer ID_LIVRO1 = 1;
    private static final Integer ID_LIVRO2 = 200;
    @Mock
    private IManutencaoEstoqueFeign client;
    @InjectMocks
    private EstoqueFeign subject;

    @Test
    @DisplayName("Retornar livros com sucesso - Uma lista com 2 ids validos")
    void consultaLivrariaSucesso1Item() throws Exception {
        LivroDTO dto1 = LivroFactoryBot.createOutputFeignLivroDto(ID_LIVRO1);
        LivroDTO dto2 = LivroFactoryBot.createOutputFeignLivroDto(ID_LIVRO2);
        Integer[] givenIds = new Integer[]{ID_LIVRO1, ID_LIVRO2};
        given(client.retornaLivro(ID_LIVRO1)).willReturn(dto1);
        given(client.retornaLivro(ID_LIVRO2)).willReturn(dto2);

        List<Livro> result = subject.consultaLivroporIds(givenIds);

        Livro expectedLivro1 = LivroMapper.INSTANCE.livroDtoToLivro(dto1);
        Livro expectedLivro2 = LivroMapper.INSTANCE.livroDtoToLivro(dto2);
        assertThat(result).containsExactlyInAnyOrder(expectedLivro1, expectedLivro2);
    }

    @Test
    @DisplayName("Retornar livros não ordenados - Uma lista com 2 ids validos não ordenado por id")
    void consultaLivrariaSucessoNotInOrderItem() throws Exception {
        LivroDTO dto1 = LivroFactoryBot.createOutputFeignLivroDto(ID_LIVRO1);
        LivroDTO dto2 = LivroFactoryBot.createOutputFeignLivroDto(ID_LIVRO2);
        Integer[] givenIds = new Integer[]{ID_LIVRO2, ID_LIVRO1};
        given(client.retornaLivro(ID_LIVRO1)).willReturn(dto1);
        given(client.retornaLivro(ID_LIVRO2)).willReturn(dto2);

        List<Livro> result = subject.consultaLivroporIds(givenIds);

        Livro expectedLivro1 = LivroMapper.INSTANCE.livroDtoToLivro(dto1);
        Livro expectedLivro2 = LivroMapper.INSTANCE.livroDtoToLivro(dto2);
        assertThat(result).containsExactly(expectedLivro2, expectedLivro1);
    }

    @Test
    @DisplayName("Fazer varias consultas para o mesmo ID - Uma lista com 3 ids iguais")
    void consultaLivrariaSucessoVariasVezesIguais() throws Exception {
        LivroDTO dto1 = LivroFactoryBot.createOutputFeignLivroDto(ID_LIVRO1);
        Integer[] givenIds = new Integer[]{ID_LIVRO1, ID_LIVRO1, ID_LIVRO1};
        given(client.retornaLivro(ID_LIVRO1)).willReturn(dto1);

        List<Livro> result = subject.consultaLivroporIds(givenIds);

        Livro expectedLivro1 = LivroMapper.INSTANCE.livroDtoToLivro(dto1);
        assertThat(result).hasSize(3).containsOnly(expectedLivro1);
    }

    @Test
    @DisplayName("Retornar apenas consultas com sucesso  - Uma lista com 1 id sucesso e 1 com erro")
    void consultaLivrariaSucessoAnd1ErrorItem() throws Exception {
        LivroDTO dto1 = LivroFactoryBot.createOutputFeignLivroDto(ID_LIVRO1);
        Integer[] givenIds = new Integer[]{ID_LIVRO1, ID_LIVRO2};
        given(client.retornaLivro(ID_LIVRO1)).willReturn(dto1);

        List<Livro> result = subject.consultaLivroporIds(givenIds);

        Livro expectedLivro1 = LivroMapper.INSTANCE.livroDtoToLivro(dto1);
        assertThat(result).hasSize(1).containsOnly(expectedLivro1);
    }

    @Test
    @DisplayName("Retornar lista vazia  - Uma lista com erros")
    void consultaLivrariaFailureIfSomethingHappens() throws Exception {
        Integer[] givenIds = new Integer[]{ID_LIVRO1, ID_LIVRO2};

        List<Livro> result = subject.consultaLivroporIds(givenIds);

        assertThat(result).isEmpty();
    }
}
