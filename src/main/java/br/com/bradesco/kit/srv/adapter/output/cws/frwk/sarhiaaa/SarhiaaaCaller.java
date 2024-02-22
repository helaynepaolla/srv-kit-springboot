package br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa;

import br.com.bradesco.data.connector.exception.ConnectorExecutionException;
import br.com.bradesco.frwk.connector.FrwkConnectorCws;
import br.com.bradesco.frwk.connector.execucao.FrwkExecucaoStatusHandler;
import br.com.bradesco.frwk.connector.execucao.model.FrwkExecucao;
import br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa.dto.ConsultarJuncaoRequest;
import br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa.dto.ConsultarJuncaoResponse;
import br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa.dto.SARHW00SResponse;
import br.com.bradesco.kit.srv.domain.entity.Funcionario;
import br.com.bradesco.kit.srv.domain.entity.Juncao;
import br.com.bradesco.kit.srv.port.output.ConsultaJuncaoGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Leap - Arquitetura & Engenharia
 * Para verificar quais variaveis sao necessarias para configurar o conector cws, olhar a classe:
 * br.com.bradesco.frwk.invoker.config.FrwkCwsConfig
 * no jar: frwk-lib-connector-cws-0.0.5.jar
 */
public final class SarhiaaaCaller implements ConsultaJuncaoGateway {

	private static final Logger LOGGER = LoggerFactory.getLogger(SarhiaaaCaller.class);
	private static final String ERRO_EXECUCAO_TRANSACAO = "Erro na execução da transação";
	private static final Integer TIPO_PESQUISA_JUNCAO_PERTENCE = 1;
	private static final Integer TIPO_PESQUISA_JUNCAO_TRABALHO = 2;

	private final FrwkConnectorCws connector;

	public SarhiaaaCaller(FrwkConnectorCws connector) {
		super();
		this.connector = connector;
	}

	@Override
	public Optional<Juncao> consultarJuncaoPertence(final Funcionario funcionario) {
		return consultarJuncao(funcionario, TIPO_PESQUISA_JUNCAO_PERTENCE, SarhiaaaCaller::getJuncaoPertence);
	}

	@Override
	public Optional<Juncao> consultarJuncaoTrabalho(final Funcionario funcionario) {
		return consultarJuncao(funcionario, TIPO_PESQUISA_JUNCAO_TRABALHO, SarhiaaaCaller::getJuncaoTrabalho);
	}

	private Optional<Juncao> consultarJuncao(
			final Funcionario funcionario,
			final int tipoPesquisa,
			final Function<SARHW00SResponse, Optional<Juncao>> success) {
		ConsultarJuncaoRequest req = new ConsultarJuncaoRequest();
		ConsultarJuncaoResponse res = new ConsultarJuncaoResponse();

		req.getSARHW00ERequest().setRotina(funcionario.getRotina());
		req.getSARHW00ERequest().setCfuncBdsco(funcionario.getMatricula());
		req.getSARHW00ERequest().setTpoPesq(tipoPesquisa);

		AtomicReference<Optional<Juncao>> memory = new AtomicReference<>(Optional.empty());

		connector.executar(req, res, new SarhiaaaHandler(sarhw00s -> memory.set(success.apply(sarhw00s))));
		return memory.get();
	}

	static Optional<Juncao> getJuncaoTrabalho(final SARHW00SResponse response) {
		return getJuncao(response.getCjuncDepdcTrab(), response.getIdepdcTrabSap());
	}

	static Optional<Juncao> getJuncaoPertence(final SARHW00SResponse response) {
		return getJuncao(response.getCjuncDepdcPertc(), response.getIdepdcPertcSap());
	}

	static Optional<Juncao> getJuncao(final Integer codigo, final String descricao) {
		if (Objects.isNull(codigo) || codigo < 0) {
			return Optional.empty();
		}
		if (Objects.isNull(descricao) || descricao.trim().isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(new Juncao(codigo, descricao));
	}

	static class SarhiaaaHandler implements FrwkExecucaoStatusHandler<ConsultarJuncaoRequest, ConsultarJuncaoResponse> {

		private final Consumer<SARHW00SResponse> success;

		SarhiaaaHandler(Consumer<SARHW00SResponse> success) {
			super();
			this.success = success;
		}

		@Override
		public void sucesso(
				final FrwkExecucao result,
				final ConsultarJuncaoRequest req,
				final ConsultarJuncaoResponse res) {
			this.success.accept(res.getSARHW00SResponse());
		}

		@Override
		public void sessaoExpirada(
				final FrwkExecucao result,
				final ConsultarJuncaoRequest req) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sessão Expirada");
		}

		@Override
		public void erroNegocio(
				final FrwkExecucao result,
				final ConsultarJuncaoRequest req,
				final ConsultarJuncaoResponse res) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro de Negócio.");
		}

		@Override
		public void erro(
				final FrwkExecucao result,
				final ConsultarJuncaoRequest req,
				final ConnectorExecutionException ex) {
			LOGGER.error(ERRO_EXECUCAO_TRANSACAO, ex);
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ERRO_EXECUCAO_TRANSACAO);
		}

		@Override
		public void excecao(
				final FrwkExecucao result,
				final ConsultarJuncaoRequest req,
				final ConnectorExecutionException ex) {
			LOGGER.error(ERRO_EXECUCAO_TRANSACAO, ex);
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ERRO_EXECUCAO_TRANSACAO, ex);
		}
	}
}
