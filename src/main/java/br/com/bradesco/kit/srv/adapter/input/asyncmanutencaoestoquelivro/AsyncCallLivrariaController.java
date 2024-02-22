package br.com.bradesco.kit.srv.adapter.input.asyncmanutencaoestoquelivro;

import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfrastructureException;
import br.com.bradesco.kit.srv.adapter.input.asyncmanutencaoestoquelivro.dto.LivroDTO;
import br.com.bradesco.kit.srv.adapter.input.asyncmanutencaoestoquelivro.dto.mapper.LivroMapper;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.port.input.IManutencaoEstoqueLivro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Classe controller do projeto de livraria
 * Responsável principalmente por:
 * - Comunicação asyncrona de requests;
 */
@RestController
@RequestMapping("/v1")
public class AsyncCallLivrariaController implements SwaggerAsyncCallLivrariaController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AsyncCallLivrariaController.class);

    @Autowired
    private IManutencaoEstoqueLivro manutencaoEstoqueLivroUseCase;

    @Override
    @GetMapping(value = "/livros-async", produces = APPLICATION_JSON_VALUE)
    public List<LivroDTO> getAsyncBooksById(@RequestParam(value = "ids[]", required = true) Integer[] ids) throws InfrastructureException {
        log.trace("### the search will retrive the results async");
        List<Livro> result = manutencaoEstoqueLivroUseCase.consultaVariosLivrosPorId(ids);
        log.info("### livros-async search done!");
        return LivroMapper.INSTANCE.listLivroToListLivroDto(result);
    }
}
