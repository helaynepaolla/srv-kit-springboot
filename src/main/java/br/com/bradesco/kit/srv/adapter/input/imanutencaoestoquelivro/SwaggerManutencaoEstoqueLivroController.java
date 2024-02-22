package br.com.bradesco.kit.srv.adapter.input.imanutencaoestoquelivro;

import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfrastructureException;
import br.com.bradesco.kit.srv.adapter.input.imanutencaoestoquelivro.dto.LivroDTO;
import br.com.bradesco.kit.srv.domain.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * interface com objetivo de reduzir code-smell das anottations do swagger
 */
@Tag(name = "ManutencaoEstoqueLivroController")
public interface SwaggerManutencaoEstoqueLivroController {

    @Operation(summary = "Lista todos os livros da base de dados", description = "Endpoint que busca todos os livros na base de dados.\\n\\nRealiza a pesquisa retornando os dados que estão expostos pelo mock.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok")})
    ResponseEntity<List<LivroDTO>> consultaLivros(@Min(1) @Max(999) Integer page, @Min(1) @Max(99) Integer pageSize) throws BusinessException, InfrastructureException;

    @Operation(summary = "Pesquisa livro por ID", description = "Endpoint que pesquisa o livro de acordo com o ID na BD.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok")})
    ResponseEntity<LivroDTO> listarLivro(@Min(1) @Max(999) Integer id) throws BusinessException, InfrastructureException;


    @Operation(summary = "Pesquisa livro por ID", description = "Endpoint que pesquisa o livro de acordo com o ID na BD.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok")})
    ResponseEntity<String> inserirLivro(@Valid LivroDTO livro) throws BusinessException, InfrastructureException;

    @Operation(summary = "Remove livro por ID", description = "Endpoint que pesquisa o livro de acordo com o ID na BD.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok")})
    ResponseEntity<String> removerLivro(@Min(1) @Max(999) Integer id) throws BusinessException, InfrastructureException;
}
