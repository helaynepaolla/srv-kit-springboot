package br.com.bradesco.kit.srv.domain.usecase;

import br.com.bradesco.kit.srv.domain.entity.Funcionario;
import br.com.bradesco.kit.srv.domain.entity.Juncao;
import br.com.bradesco.kit.srv.port.input.ConsultarJuncaoUseCase;
import br.com.bradesco.kit.srv.port.output.ConsultaJuncaoGateway;

import java.util.Optional;

public class ConsultarJuncao implements ConsultarJuncaoUseCase {

	private final ConsultaJuncaoGateway juncaoGateway;

	public ConsultarJuncao(ConsultaJuncaoGateway juncaoGateway) {
		this.juncaoGateway = juncaoGateway;
	}
	@Override
	public Optional<Juncao> getJuncaoPertence(String rotina, Integer matricula) {
		return this.juncaoGateway
				.consultarJuncaoPertence(new Funcionario(rotina, matricula));
	}

	@Override
	public Optional<Juncao> getJuncaoTrabalho(String rotina, Integer matricula) {
		return this.juncaoGateway
				.consultarJuncaoTrabalho(new Funcionario(rotina, matricula));
	}
}
