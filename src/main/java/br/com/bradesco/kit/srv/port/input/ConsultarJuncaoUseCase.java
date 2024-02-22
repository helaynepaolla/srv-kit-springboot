package br.com.bradesco.kit.srv.port.input;

import br.com.bradesco.kit.srv.domain.entity.Juncao;

import java.util.Optional;

public interface ConsultarJuncaoUseCase {
	Optional<Juncao> getJuncaoPertence(final String rotina, final Integer matricula);

	Optional<Juncao> getJuncaoTrabalho(final String rotina, final Integer matricula);
}
