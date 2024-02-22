package br.com.bradesco.kit.srv.adapter.input.asyncmanutencaoestoquelivro;

import br.com.bradesco.kit.srv.adapter.exception.infrastructure.InfrastructureException;
import br.com.bradesco.kit.srv.adapter.input.asyncmanutencaoestoquelivro.dto.LivroDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * interface com objetivo de reduzir code-smell das anottations do swagger
 */
@Tag(name = "AsyncCallLivrariaController")
public interface SwaggerAsyncCallLivrariaController {

    @Operation(summary = "Lista todos os livros da base de dados por IDs", description = "Endpoint que busca todos os livros na base de dados por ids.\\n\\nRealiza a pesquisa retornando os dados que estão expostos pelo mock.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok")})
    List<LivroDTO> getAsyncBooksById(Integer[] ids) throws InfrastructureException;
}
