package br.com.bradesco.kit.srv.adapter.output.feign;

import br.com.bradesco.kit.srv.adapter.output.feign.dto.LivroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "${external-endpoints.livrariaService.name}", url = "${external-endpoints.livrariaService.url}")
public interface IManutencaoEstoqueFeign {

	/**
	 * Método responsável por retornar toda a coleção de livros disponível
	 *
	 * @return List<LivroDTO>
	 * O objeto retornado terá uma lista de objetos de LivroDTO contendo todos os objetos de livros disponíveis
	 */
	@GetMapping(value = "${external-endpoints.livrariaService.get.path}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	List<LivroDTO> retornaColecao();

	/**
	 * Método responsável por retornar apenas o livro requisitado
	 *
	 * @return LivroDTO
	 * O objeto retornado conterá apenas os dados de um dos livros requisitados
	 */
	@GetMapping(value = "${external-endpoints.livrariaService.get.path}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	LivroDTO retornaLivro(@PathVariable("id") Integer id);

	/**
	 * Método responsável por criar um novo livro na base de dados
	 *
	 * @return LivroDTO
	 */
	@PostMapping(value = "${external-endpoints.livrariaService.get.path.get.path}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	LivroDTO criarNovoLivro(@RequestBody LivroDTO novoLivro);

	/**
	 * Método responsável por deletar um novo livro na base de dados
	 *
	 * @return LivroDTO
	 */
	@DeleteMapping(value = "${external-endpoints.livrariaService.get.path.path}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	LivroDTO deletarLivro(@PathVariable("id") Integer id);
}
