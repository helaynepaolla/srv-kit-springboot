package br.com.bradesco.kit.srv.adapter.output.feign;

import br.com.bradesco.kit.srv.adapter.output.feign.dto.LivroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${external-endpoints.livrariaFallback.name}", url = "${external-endpoints.livrariaFallback.url}")
public interface IManutencaoEstoqueFeignFallback {

    /**
     * Método de fallback para consulta de Coleção que irá retornar apenas um livro de "sugestão"
     *
     * @return List<LivroDTO>
     * O objeto retornado terá uma lista de objetos de LivroDTO contendo todos os objetos de livros disponíveis
     */
    @GetMapping(value = "${external-endpoints.livrariaFallback.get.path}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    List<LivroDTO> retornaColecaoFallback();

    /**
     * Método de fallback para consulta de livro que irá retornar apenas um livro de "sugestão"
     *
     * @return LivroDTO
     * O objeto retornado conterá apenas os dados de um dos livros requisitados
     */
    @GetMapping(value = "${external-endpoints.livrariaFallback.get.path}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    LivroDTO retornaLivroFallback(@PathVariable("id") Integer id);

}