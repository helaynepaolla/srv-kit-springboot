package br.com.bradesco.kit.srv.adapter.input.imanutencaoestoquelivro;


import br.com.bradesco.kit.srv.adapter.input.imanutencaoestoquelivro.dto.LivroDTO;
import br.com.bradesco.kit.srv.adapter.input.imanutencaoestoquelivro.dto.mapper.LivroMapper;
import br.com.bradesco.kit.srv.adapter.output.redis.LivroRepository;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.port.input.IManutencaoEstoqueLivro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v2")
@Validated
public class ManutencaoEstoqueLivroController implements SwaggerManutencaoEstoqueLivroController {

    private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(ManutencaoEstoqueLivroController.class);

    @Autowired
    private LivroRepository valueCache;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IManutencaoEstoqueLivro manutencaoEstoqueLivroUseCase;

    @GetMapping(value = "/livros", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LivroDTO>> consultaLivros(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "page-size", required = false) Integer pageSize) {
        //verifica se existe um item no cache
        boolean retorno = redisTemplate.hasKey(String.valueOf(page));

        if (retorno) {
            //consome dado do cache
            List<LivroDTO> paginaLivros = (List<LivroDTO>) valueCache.getCachedValue(String.valueOf(page));

            //trata retorno adequado
            return ResponseEntity.ok(paginaLivros);
        }
        //chama usecase
        List<LivroDTO> paginaLivros = LivroMapper.INSTANCE.listLivroToListLivroDto(manutencaoEstoqueLivroUseCase.consultaLivros(page, pageSize));

        //Adicionando ao Cache
        valueCache.saveToCache(paginaLivros.toString(), paginaLivros);

        LOGGER_TECNICO.debug("consulta de livro executada com sucesso.");
        return ResponseEntity.ok(paginaLivros);
    }

    @GetMapping(value = "/livro/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LivroDTO> listarLivro(@PathVariable Integer id) {
        LivroDTO livro;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(String.valueOf(id)))) {
            //consome dado do cache
            livro = (LivroDTO) valueCache.getCachedValue(String.valueOf(id));

            //trata retorno adequado
            return ResponseEntity.ok(livro);
        } else {
            Optional<Livro> result = manutencaoEstoqueLivroUseCase.consultaLivroPorId(id);

            if (result.isPresent()) {
                livro = LivroMapper.INSTANCE.livroToLivroDto(result.get());

                //Adicionando dado novo ao Cache
                valueCache.saveToCache(livro.id().toString(), livro);

                return ResponseEntity.ok(livro);
            }
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/livro", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> inserirLivro(@RequestBody LivroDTO livro) {
        manutencaoEstoqueLivroUseCase.incluirNovoLivro(LivroMapper.INSTANCE.livroDtoToLivro(livro));

        //adiciona livro ao cache
        valueCache.saveToCache(livro.id().toString(), livro);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/livro/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removerLivro(@PathVariable Integer id) {
        manutencaoEstoqueLivroUseCase.removerLivro(id);

        //deleta o item retirado acima, caso ele estivesse no cache
        valueCache.deleteCachedValue(String.valueOf(id));
        return ResponseEntity.ok().build();
    }
}
