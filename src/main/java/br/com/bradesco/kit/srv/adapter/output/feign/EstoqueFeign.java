package br.com.bradesco.kit.srv.adapter.output.feign;

import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfraCallNotPermitedException;
import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfrastructureException;
import br.com.bradesco.kit.srv.adapter.output.feign.dto.LivroDTO;
import br.com.bradesco.kit.srv.adapter.output.feign.dto.mapper.LivroMapper;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.port.output.IEstoque;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Component
@Qualifier("IEstoqueFeignClient")
public class EstoqueFeign implements IEstoque {

    private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(EstoqueFeign.class);
    private final IManutencaoEstoqueFeign iFeignClient;
    private final IManutencaoEstoqueFeignFallback iFallbackFeignClient;

    public EstoqueFeign(IManutencaoEstoqueFeign iFeignClient, IManutencaoEstoqueFeignFallback iFallbackFeignClient) {
        this.iFeignClient = iFeignClient;
        this.iFallbackFeignClient = iFallbackFeignClient;
    }

    @Override
    @CircuitBreaker(name = "srvKitSpringboot", fallbackMethod = "fallbackConsultaLivros")
    public List<Livro> consultaLivros(Integer page, Integer pageSize) throws InfrastructureException {
        List<LivroDTO> listaDTO = iFeignClient.retornaColecao();
        return LivroMapper.INSTANCE.listLivroDTOToListLivro(listaDTO);
    }

    @Override
    @CircuitBreaker(name = "srvKitSpringboot", fallbackMethod = "fallbackConsultaLivroPorId")
    public Optional<Livro> consultaLivroporId(Integer id) throws InfrastructureException {
        LivroDTO result = iFeignClient.retornaLivro(id);
        var livro = LivroMapper.INSTANCE.livroDtoToLivro(result);
        return Optional.of(livro);
    }

    @Override
    @CircuitBreaker(name = "srvKitSpringboot", fallbackMethod = "fallbackIncluirNovoLivro")
    public void incluirNovoLivro(Livro novoLivro) throws InfrastructureException {
        var livro = LivroMapper.INSTANCE.livroToLivroDto(novoLivro);
        iFeignClient.criarNovoLivro(livro);
    }

    @Override
    @CircuitBreaker(name = "srvKitSpringboot", fallbackMethod = "fallbackRemoverLivro")
    public void removerLivro(Integer id) throws InfrastructureException {
        iFeignClient.deletarLivro(id);
    }

    @Override

    public List<Livro> consultaLivroporIds(Integer... ids) throws InfrastructureException {
        return Stream.of(ids)
                .map(id -> CompletableFuture.supplyAsync(() -> iFeignClient.retornaLivro(id))
                        .exceptionally(err -> null)) // if a error occour the result will be null
                .map(CompletableFuture::join) // run the requests and wait the resposes
                .filter(Objects::nonNull)
                .map(LivroMapper.INSTANCE::livroDtoToLivro)
                .toList();
    }

    // Exemplo de fallback que realiza uma conexão a um HTTP externo secundário e o
    // retorna ao cliente junto de um log de erro
    public List<Livro> fallbackConsultaLivros(Integer page, Integer pageSize, Throwable ex) {
        List<LivroDTO> listaDTO = iFallbackFeignClient.retornaColecaoFallback();

        LOGGER_TECNICO.error("CircuitBreaker em status OPEN devido a falhas na conexão com HTTP externo, Excecao: ", ex);

        return LivroMapper.INSTANCE.listLivroDTOToListLivro(listaDTO);
    }

    // Exemplo de fallback que realiza uma conexão a um HTTP externo secundário e o
    // retorna ao cliente junto de um log de erro
    public Optional<Livro> fallbackConsultaLivroPorId(Integer id, Throwable ex) {
        LivroDTO result = iFallbackFeignClient.retornaLivroFallback(id);
        var livro = LivroMapper.INSTANCE.livroDtoToLivro(result);

        LOGGER_TECNICO.error("CircuitBreaker em status OPEN devido a falhas na conexão com HTTP externo, Excecao: ", ex);

        return Optional.of(livro);
    }

    // Exemplo de fallback que lança um erro informando que o circuito está aberto e
    // a requisição não poderá ser atendida
    public void fallbackIncluirNovoLivro(Livro novoLivro, Throwable ex) {
        throw new InfraCallNotPermitedException(ex);
    }

    // Exemplo de fallback que lança um erro informando que o circuito está aberto e
    // a requisição não poderá ser atendida
    public void fallbackRemoverLivro(Integer id, Throwable ex) {
        throw new InfraCallNotPermitedException(ex);
    }

}
