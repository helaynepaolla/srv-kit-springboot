package br.com.bradesco.kit.srv.adapter.output.resttemplate.dto;

import br.com.bradesco.kit.srv.domain.entity.GeneroLivro;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "titulo",
        "autor",
        "editora",
        "valor",
        "qtde_estoque",
        "genero"
})
public record LivroDTO(@NotNull
                       @JsonProperty("id")
                       Integer id,
                       @JsonProperty("titulo")
                       String titulo,
                       @JsonProperty("autor")
                       String autor,
                       @JsonProperty("editora")
                       String editora,
                       @JsonProperty("valor")
                       BigDecimal valor,
                       @JsonProperty("qtde_estoque")
                       int qtdEstoque,
                       @JsonProperty("genero")
                       GeneroLivro genero) implements Serializable {
}
