package br.com.bradesco.kit.srv.port.output;

import br.com.bradesco.kit.srv.domain.entity.Funcionario;
import br.com.bradesco.kit.srv.domain.entity.Juncao;

import java.util.Optional;

public interface ConsultaJuncaoGateway {

	/**
	 * Método para consultar a junção de pertencimento de um funcionário.
	 *
	 * @param funcionario Funcionário
	 * @return Junção do funcionário
	 */
	Optional<Juncao> consultarJuncaoPertence(Funcionario funcionario);

	/**
	 * Método para consultar a junção de trabalho de um funcionário.
	 *
	 * @param funcionario Funcionário
	 * @return Junção do funcionário
	 */
	Optional<Juncao> consultarJuncaoTrabalho(Funcionario funcionario);
}
