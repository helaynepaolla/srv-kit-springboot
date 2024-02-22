package br.com.bradesco.kit.srv.adapter.output.log;

import br.com.bradesco.enge.logcloud.canal.ReturnCode;
import br.com.bradesco.enge.logcloud.canal.SrvCanalLog;
import br.com.bradesco.enge.logcloud.canal.SrvCanalLogger;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import br.com.bradesco.kit.srv.port.output.ILogServicoCanal;

import java.util.List;

public class LogServicoCanal implements ILogServicoCanal {

    public static final String KIT_001 = "KIT001";
    private final SrvCanalLogger canalLogger;

    public LogServicoCanal(SrvCanalLogger canalLogger) {
        this.canalLogger = canalLogger;
    }

    @Override
    public void logConsultaTodosLivros(ReturnCode returnCode, List<Livro> livros) {
        this.canalLogger.log(
                SrvCanalLog.builder(returnCode)
                        // Entrada é um campo mandatório do log
                        .entrada("Entrada: Request para buscar a colecao de livros->: sem parametro")
                        // Saida não é um campo mandatório
                        .saida("Saida: Colecao de livros cadastrados nas base de dados->: " + livros)
                        .codigoTransacao(KIT_001)
                        .addMensagem(KIT_001, "Buscar colecao de livros")
                        .build());
    }

    @Override
    public void logConsultaLivroPorId(ReturnCode returnCode, Integer idEntrada, Livro livroRetornado) {
        this.canalLogger.log(
                SrvCanalLog.builder(returnCode)
                        // Entrada é um campo mandatório do log
                        .entrada("Entrada: Request para buscar a colecao de livros->: id->: " + idEntrada)
                        // Saida não é um campo mandatório
                        .saida("Saida: Livro retornado da consulta->: " + livroRetornado.toString())
                        .codigoTransacao(KIT_001)
                        .addMensagem(KIT_001, "Buscar livro por id")
                        .build());
    }

    public void logConsultaLivroPorIdException(ReturnCode returnCode, Integer idEntrada, RuntimeException ex) {
        this.canalLogger.log(
                SrvCanalLog.builder(returnCode)
                        // Entrada é um campo mandatório do log
                        .entrada("Entrada: Request para buscar a colecao de livros->: id->: " + idEntrada)
                        // Saida não é um campo mandatório
                        .saida("Saida: Livro retornado da consulta->: " + ex.toString())
                        .codigoTransacao(KIT_001)
                        .addMensagem(KIT_001, "Buscar livro por id")
                        .build());
    }

    @Override
    public void logInclusaoLivro(ReturnCode returnCode, Livro livroIncluido) {
        this.canalLogger.log(
                SrvCanalLog.builder(returnCode)
                        // Entrada é um campo mandatório do log
                        .entrada("Entrada: Request dados do livro pra ser incluido->: id->: " + livroIncluido.toString())
                        // Saida não é um campo mandatório
                        .saida("Saida: Livro retornado da inclusao->: " + livroIncluido)
                        .codigoTransacao(KIT_001)
                        .addMensagem(KIT_001, "Incluir novo livro")
                        .build());
    }

    @Override
    public void logExclusaoLivro(ReturnCode returnCode, Integer idExcluido) {
        this.canalLogger.log(
                SrvCanalLog.builder(returnCode)
                        // Entrada é um campo mandatório do log
                        .entrada("Entrada: Request dados do livro pra ser excluido->: id->: " + idExcluido.toString())
                        // Saida não é um campo mandatório
                        .saida("Saida: sem parametro de retorno da excluosa do livro")
                        .codigoTransacao(KIT_001)
                        .addMensagem(KIT_001, "Excluir Livro")
                        .build());
    }
}
