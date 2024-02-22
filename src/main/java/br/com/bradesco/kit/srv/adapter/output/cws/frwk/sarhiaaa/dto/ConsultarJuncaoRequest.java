/**
 * Nome: br.com.bradesco.web.piloto.service.awbConnector.adapter.problema.request
 * Prop�sito: Classe gerada automaticamente a partir de um xsd
 * Data da Cria��o: 09/08/2019
 * Compilador: 1.5
 * Par�metros de Compila��o: -d
 */
package br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa.dto;

import br.com.bradesco.data.connector.commarea.annotations.transaction.TransactionDescriptor;
import br.com.bradesco.data.connector.commarea.annotations.types.CommareaBlock;
import br.com.bradesco.data.connector.header.request.FrameworkHeaderRequest;

/**
 * Request para execução do fluxo SARHIAAA.
 *
 * @author Engenharia DS
 * @version 1.0
 */
@TransactionDescriptor(connector = "CWS", name = "SARHIAAA")
public class ConsultarJuncaoRequest extends FrameworkHeaderRequest {

    @CommareaBlock(layoutSize = 27, layoutCode = "SARHW00E")
    protected SARHW00ERequest sarhw00ERequest = new SARHW00ERequest();

    /**
     * Construtor Padrão
     */
    public ConsultarJuncaoRequest() {
        super();
    }

    /**
     * Retorna os dados de requisição do book de entrada SARHW00E.
     *
     * @return dados do SARHW00E.
     */
    public SARHW00ERequest getSARHW00ERequest() {
        return sarhw00ERequest;
    }

    /**
     * Altera a instância do book de entrada SARHW00E.
     *
     * @param value nova instância do book SARHW00E.
     */
    public void setSARHW00ERequest(SARHW00ERequest value) {
        this.sarhw00ERequest = value;
    }
}
