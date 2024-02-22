/**
 * Nome: br.com.bradesco.web.piloto.service.awbConnector.adapter.problema.response
 * Prop�sito: Classe gerada automaticamente a partir de um xsd
 * Data da Cria��o: 09/08/2019
 * Compilador: 1.5
 * Par�metros de Compila��o: -d
 */
package br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa.dto;

import br.com.bradesco.data.connector.commarea.annotations.transaction.TransactionDescriptor;
import br.com.bradesco.data.connector.commarea.annotations.types.CommareaBlock;
import br.com.bradesco.data.connector.header.response.FrameworkHeaderResponse;

/**
 * Response para execução do fluxo SARHIAAA.
 *
 * @author Engenharia DS
 * @version 1.0
 */
@TransactionDescriptor(connector = "CWS", name = "SARH")
public class ConsultarJuncaoResponse extends FrameworkHeaderResponse {

    @CommareaBlock(layoutSize = 105, layoutCode = "SARHW00S")
    protected SARHW00SResponse sarhw00SResponse = new SARHW00SResponse();

    /**
     * Construtor Padrão
     */
    public ConsultarJuncaoResponse() {
        super();
    }

    /**
     * Retorna os dados de requisição do book de saída SARHW00S.
     *
     * @return dados do SARHW00S.
     */
    public SARHW00SResponse getSARHW00SResponse() {
        return sarhw00SResponse;
    }

    /**
     * Altera a instância do book de saída SARHW00S.
     *
     * @param value nova instância do book SARHW00S.
     */
    public void setSARHW00SResponse(SARHW00SResponse value) {
        this.sarhw00SResponse = value;
    }
}
