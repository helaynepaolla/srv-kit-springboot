package br.com.bradesco.kit.srv.adapter.output.resttemplate;

import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfraCallNotPermitedException;
import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfrastructureException;
import br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.LivroDTO;
import br.com.bradesco.kit.srv.adapter.output.resttemplate.dto.mapper.LivroMapper;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.port.output.IEstoque;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Component
@Qualifier("IEstoqueRestTemplate")
public class EstoqueRestTemplate implements IEstoque {
	private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(EstoqueRestTemplate.class);
	private final RestTemplate restTemplate;
	private final String connectionUrl;

	@Value("${external-endpoints.livrariaFallback.url}${external-endpoints.livrariaFallback.get.path}")

	private String connectionFallbackURL;

	public EstoqueRestTemplate(RestTemplate restTemplate, String connectionUrl) {
		this.restTemplate = restTemplate;
		this.connectionUrl = connectionUrl;
	}

	// Exemplo de Caching usando anotation
	// @Cacheable(key = "#id")
	@Override
	@CircuitBreaker(name = "srvKitSpringboot", fallbackMethod = "fallbackConsultaLivros")
	public List<Livro> consultaLivros(Integer page, Integer pageSize) throws InfrastructureException {
		ResponseEntity<List<LivroDTO>> request = restTemplate.exchange(
				connectionUrl + "?_page=" + page + "&_limit=" + pageSize, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<LivroDTO>>() {
				});
		return LivroMapper.INSTANCE.listLivroDTOToListLivro(request.getBody());
	}

	// Exemplo de Caching usando anotation
	// @Cacheable(key = "#id", value = "EstoqueRestTemplate")
	@Override
	@CircuitBreaker(name = "srvKitSpringboot", fallbackMethod = "fallbackConsultaLivroPorId")
	public Optional<Livro> consultaLivroporId(Integer id) throws InfrastructureException {
		LivroDTO response = restTemplate.getForObject(connectionUrl + id, LivroDTO.class);
		var livro = LivroMapper.INSTANCE.livroDtoToLivro(response);
		return Optional.of(livro);
	}

	// Exemplo de Caching usando anotation
	// @CachePut(key = "#id", value = "EstoqueRestTemplate")
	@Override
	@CircuitBreaker(name = "srvKitSpringboot", fallbackMethod = "fallbackIncluirNovoLivro")
	public void incluirNovoLivro(Livro novoLivro) throws InfrastructureException {
		var headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<LivroDTO> httpParams = new HttpEntity<>(LivroMapper.INSTANCE.livroToLivroDto(novoLivro), headers);
		restTemplate.postForEntity(connectionUrl, httpParams, LivroDTO.class);
	}

	// Exemplo de Caching usando anotation
	// @CacheEvict(key = "#id", value = "EstoqueRestTemplate" , allEntries =
	// false)
	@Override
	@CircuitBreaker(name = "srvKitSpringboot", fallbackMethod = "fallbackRemoverLivro")
	public void removerLivro(Integer id) throws InfrastructureException {
		restTemplate.exchange(connectionUrl + id, HttpMethod.DELETE, null, LivroDTO.class);
	}

	@Override
	public List<Livro> consultaLivroporIds(Integer... ids) throws InfrastructureException {
		return Stream.of(ids)
				.map(id -> CompletableFuture
						.supplyAsync(() -> restTemplate.getForObject(connectionUrl + id, LivroDTO.class))
						.exceptionally(err -> null)) // if a error occour the result will be null
				.map(CompletableFuture::join) // run the requests and wait the responses
				.filter(Objects::nonNull)
				.map(LivroMapper.INSTANCE::livroDtoToLivro)
				.toList();
	}

	// Exemplo de fallback que realiza uma consulta alternativa para o usuário e
	// realiza um log de erro
	public List<Livro> fallbackConsultaLivros(Integer page, Integer pageSize, Throwable ex) {
		ResponseEntity<List<LivroDTO>> request = restTemplate.exchange(connectionFallbackURL, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<LivroDTO>>() {
				});

		LOGGER_TECNICO.error("CircuitBreaker em status OPEN devido a falhas na conexão com {}, Excecao: ",
				connectionUrl, ex);

		return LivroMapper.INSTANCE.listLivroDTOToListLivro(request.getBody());
	}

	// Exemplo de fallback que realiza uma consulta alternativa para o usuário e
	// realiza um log de erro
	public Optional<Livro> fallbackConsultaLivroPorId(Integer id, Throwable ex) throws InfraCallNotPermitedException {
		LivroDTO response = restTemplate.getForObject(connectionFallbackURL + 000, LivroDTO.class);
		var livro = LivroMapper.INSTANCE.livroDtoToLivro(response);

		LOGGER_TECNICO.error("CircuitBreaker em status OPEN devido a falhas na conexão com {}, Excecao: ",
				connectionUrl, ex);

		return Optional.of(livro);
	}

	// Exemplo de fallback que lança uma exceção e retorna uma mensagem de erro
	// informando que o circuito está aberto
	public void fallbackIncluirNovoLivro(Livro novoLivro, Throwable ex) {
		throw new InfraCallNotPermitedException(ex);
	}

	// Exemplo de fallback que lança uma exceção e retorna uma mensagem de erro
	// informando que o circuito está aberto
	public void fallbackRemoverLivro(Integer id, Throwable ex) {
		throw new InfraCallNotPermitedException(ex);
	}

}
