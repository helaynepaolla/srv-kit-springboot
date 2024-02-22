package br.com.bradesco.kit.srv.domain.usecase;

import br.com.bradesco.kit.srv.domain.entity.Funcionario;
import br.com.bradesco.kit.srv.domain.entity.Juncao;
import br.com.bradesco.kit.srv.port.output.ConsultaJuncaoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarJuncaoTest {

	@Mock
	ConsultaJuncaoGateway juncaoGateway;

	Juncao retornoJuncaoEsperado = new Juncao(10, "juncao teste");
	String rotina = "sarh";
	Integer matricula = 9123123;


	@BeforeEach
	void setUp() {

	}

	@Test
	void getJuncaoPertence() {
		Funcionario funcionario = new Funcionario(rotina, matricula);
		Optional<Juncao> retornoJuncaoRealizado;

		when(juncaoGateway.consultarJuncaoPertence(any(Funcionario.class))).thenReturn(Optional.of(retornoJuncaoEsperado));

		ConsultarJuncao juncaoUseCase = new ConsultarJuncao(juncaoGateway);
		retornoJuncaoRealizado = juncaoUseCase.getJuncaoPertence(rotina, matricula);

		assertEquals(retornoJuncaoRealizado, Optional.of(retornoJuncaoEsperado));
	}

	@Test
	void getJuncaoTrabalho() {
		Funcionario funcionario = new Funcionario(rotina, matricula);
		Optional<Juncao> retornoJuncaoRealizado;

		when(juncaoGateway.consultarJuncaoTrabalho(any(Funcionario.class))).thenReturn(Optional.of(retornoJuncaoEsperado));

		ConsultarJuncao juncaoUseCase = new ConsultarJuncao(juncaoGateway);
		retornoJuncaoRealizado = juncaoUseCase.getJuncaoTrabalho(rotina, matricula);

		assertEquals(retornoJuncaoRealizado, Optional.of(retornoJuncaoEsperado));
	}
}
