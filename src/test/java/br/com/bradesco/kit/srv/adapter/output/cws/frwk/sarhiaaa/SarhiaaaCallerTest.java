package br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa;

import br.com.bradesco.data.connector.exception.ConnectorExecutionException;
import br.com.bradesco.frwk.connector.FrwkConnectorCws;
import br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa.dto.ConsultarJuncaoRequest;
import br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa.dto.ConsultarJuncaoResponse;
import br.com.bradesco.kit.srv.domain.entity.Funcionario;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class SarhiaaaCallerTest {

	private static final String rotina = "sarh";
	private static final Integer matricula = 9123123;
	private static final Funcionario funcionario = new Funcionario(rotina, matricula);

	private static final Integer TIPO_PESQUISA_JUNCAO_PERTENCE = 1;
	private static final Integer TIPO_PESQUISA_JUNCAO_TRABALHO = 2;

	@InjectMocks
	SarhiaaaCaller sarhCaller;

	@Mock
	FrwkConnectorCws conector;


	@BeforeEach
	void setUp() {
	}

	Funcionario criaFuncionario() {
		String rotina = "sarh";
		Integer matricula = 9123123;
		return new Funcionario(rotina, matricula);
	}

	;

	ConsultarJuncaoRequest criaRequestPertence(Funcionario funcionario) {
		ConsultarJuncaoRequest req = new ConsultarJuncaoRequest();
		req.getSARHW00ERequest().setRotina(funcionario.getRotina());
		req.getSARHW00ERequest().setCfuncBdsco(funcionario.getMatricula());
		req.getSARHW00ERequest().setTpoPesq(TIPO_PESQUISA_JUNCAO_PERTENCE);
		return req;
	}

	ConsultarJuncaoRequest criaRequestTrabalho(Funcionario funcionario) {
		ConsultarJuncaoRequest req = new ConsultarJuncaoRequest();
		req.getSARHW00ERequest().setRotina(funcionario.getRotina());
		req.getSARHW00ERequest().setCfuncBdsco(funcionario.getMatricula());
		req.getSARHW00ERequest().setTpoPesq(TIPO_PESQUISA_JUNCAO_TRABALHO);
		return req;
	}

	ConsultarJuncaoResponse criaResponsePertence() {
		ConsultarJuncaoResponse res = new ConsultarJuncaoResponse();
		res.getSARHW00SResponse().setCjuncDepdcPertc(5);
		res.getSARHW00SResponse().setIdepdcPertcSap("teste");
		return res;
	}

	ConsultarJuncaoResponse criaResponseTrabalho() {
		ConsultarJuncaoResponse res = new ConsultarJuncaoResponse();
		res.getSARHW00SResponse().setCjuncDepdcTrab(5);
		res.getSARHW00SResponse().setIdepdcTrabSap("teste");
		return res;
	}

	@Test
	void consultarJuncaoPertenceSucesso() {
		Funcionario func = criaFuncionario();
		ConsultarJuncaoRequest req = criaRequestPertence(func);
		ConsultarJuncaoResponse res = criaResponsePertence();

		doAnswer(
				invocation -> {
					var handler = invocation.getArgument(2, SarhiaaaCaller.SarhiaaaHandler.class);
					handler.sucesso(null, req, res);
					return null;
				}).when(conector).executar(isA(ConsultarJuncaoRequest.class), isA(ConsultarJuncaoResponse.class), isA(SarhiaaaCaller.SarhiaaaHandler.class));

		var retorno = sarhCaller.consultarJuncaoPertence(func);

		assertEquals(true, retorno.isPresent());
		assertEquals(5, retorno.get().getCodigo());
		assertEquals("teste", retorno.get().getDescricao());
		Assertions.assertThat(retorno).isPresent();
		BDDAssertions.then(retorno).isPresent();
	}

	@Test
	void consultarJuncaoPertenceSessaoExpirada() {
		Funcionario func = criaFuncionario();
		ConsultarJuncaoRequest req = criaRequestPertence(func);
		ConsultarJuncaoResponse res = criaResponsePertence();

		doAnswer(
				invocation -> {
					var handler = invocation.getArgument(2, SarhiaaaCaller.SarhiaaaHandler.class);
					handler.sessaoExpirada(null, req);
					return null;
				}).when(conector).executar(isA(ConsultarJuncaoRequest.class), isA(ConsultarJuncaoResponse.class), isA(SarhiaaaCaller.SarhiaaaHandler.class));

		try {
			var retorno = sarhCaller.consultarJuncaoPertence(func);
		} catch (ResponseStatusException e) {
			assertEquals(e.getStatus(), HttpStatus.UNAUTHORIZED);
		}
	}

	@Test
	void consultarJuncaoPertenceErroNegocio() {
		Funcionario func = criaFuncionario();
		ConsultarJuncaoRequest req = criaRequestPertence(func);
		ConsultarJuncaoResponse res = criaResponsePertence();

		doAnswer(
				invocation -> {
					var handler = invocation.getArgument(2, SarhiaaaCaller.SarhiaaaHandler.class);
					handler.erroNegocio(null, req, res);
					return null;
				}).when(conector).executar(isA(ConsultarJuncaoRequest.class), isA(ConsultarJuncaoResponse.class), isA(SarhiaaaCaller.SarhiaaaHandler.class));

		try {
			var retorno = sarhCaller.consultarJuncaoPertence(func);
		} catch (ResponseStatusException e) {
			assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
		}
	}

	@Test
	void consultarJuncaoPertenceErro() {
		Funcionario func = criaFuncionario();
		ConsultarJuncaoRequest req = criaRequestPertence(func);
		ConsultarJuncaoResponse res = criaResponsePertence();

		doAnswer(
				invocation -> {
					var handler = invocation.getArgument(2, SarhiaaaCaller.SarhiaaaHandler.class);
					handler.erro(null, req, new ConnectorExecutionException(new Throwable("erro generico de conexao CWS")));
					return null;
				}).when(conector).executar(isA(ConsultarJuncaoRequest.class), isA(ConsultarJuncaoResponse.class), isA(SarhiaaaCaller.SarhiaaaHandler.class));

		try {
			var retorno = sarhCaller.consultarJuncaoPertence(func);
		} catch (ResponseStatusException e) {
			assertEquals(e.getStatus(), HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@Test
	void consultarJuncaoPertenceExcecao() {
		Funcionario func = criaFuncionario();
		ConsultarJuncaoRequest req = criaRequestPertence(func);
		ConsultarJuncaoResponse res = criaResponsePertence();

		doAnswer(
				invocation -> {
					var handler = invocation.getArgument(2, SarhiaaaCaller.SarhiaaaHandler.class);
					handler.excecao(null, req, new ConnectorExecutionException(new Throwable("erro generico de conexao CWS")));
					return null;
				}).when(conector).executar(isA(ConsultarJuncaoRequest.class), isA(ConsultarJuncaoResponse.class), isA(SarhiaaaCaller.SarhiaaaHandler.class));

		try {
			var retorno = sarhCaller.consultarJuncaoPertence(func);
		} catch (ResponseStatusException e) {
			assertEquals(e.getStatus(), HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@Test
	void consultarJuncaoTrabalhoSucesso() {
		Funcionario func = criaFuncionario();
		ConsultarJuncaoRequest req = criaRequestTrabalho(func);
		ConsultarJuncaoResponse res = criaResponseTrabalho();

		doAnswer(
				invocation -> {
					var handler = invocation.getArgument(2, SarhiaaaCaller.SarhiaaaHandler.class);
					handler.sucesso(null, req, res);
					return null;
				}).when(conector).executar(isA(ConsultarJuncaoRequest.class), isA(ConsultarJuncaoResponse.class), isA(SarhiaaaCaller.SarhiaaaHandler.class));

		var retorno = sarhCaller.consultarJuncaoTrabalho(func);

		assertEquals(true, retorno.isPresent());
		assertEquals(5, retorno.get().getCodigo());
		assertEquals("teste", retorno.get().getDescricao());
		Assertions.assertThat(retorno).isPresent();
		BDDAssertions.then(retorno).isPresent();
	}
}
