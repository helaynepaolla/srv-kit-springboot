package br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa.dto;

import br.com.bradesco.data.connector.AnnotatedAdapter;
import br.com.bradesco.data.connector.commarea.annotations.types.CommareaField;

/**
 * Representação do copybook SARHW00S.
 *
 * @author Engenharia DS
 * @version 1.0
 */
public class SARHW00SResponse extends AnnotatedAdapter {

    /**
     * Atributo codLayout
     */
    @CommareaField(name = {"W00S-HEADER", "W00S-COD-LAYOUT"}, maxLength = 8, usage = "DISPLAY",
            fieldNumber = 0, maxBytes = 8, pic = "X(008)")
    protected String codLayout = "SARHW00S";

    /**
     * Atributo tamLayout
     */
    @CommareaField(name = {"W00S-HEADER", "W00S-TAM-LAYOUT"}, maxLength = 5, usage = "DISPLAY",
            fieldNumber = 1, maxBytes = 5, pic = "9(005)")
    protected int tamLayout = 105;

    /**
     * Atributo cjuncDepdcPertc
     */
    @CommareaField(name = {"W00S-REGISTRO", "W00S-SAIDA", "W00S-CJUNC-DEPDC-PERTC"}, maxLength = 5, usage = "D"
            + "ISPLAY", fieldNumber = 2, maxBytes = 5, pic = "9(005)")
    protected int cjuncDepdcPertc = 0;

    /**
     * Atributo idepdcPertcSap
     */
    @CommareaField(name = {"W00S-REGISTRO", "W00S-SAIDA", "W00S-IDEPDC-PERTC-SAP"}, maxLength = 40, usage = "D"
            + "ISPLAY", fieldNumber = 3, maxBytes = 40, pic = "X(040)")
    protected String idepdcPertcSap = null;

    /**
     * Atributo cjuncDepdcTrab
     */
    @CommareaField(name = {"W00S-REGISTRO", "W00S-SAIDA", "W00S-CJUNC-DEPDC-TRAB"}, maxLength = 5, usage = "DI"
            + "SPLAY", fieldNumber = 4, maxBytes = 5, pic = "9(005)")
    protected int cjuncDepdcTrab = 0;

    /**
     * Atributo idepdcTrabSap
     */
    @CommareaField(name = {"W00S-REGISTRO", "W00S-SAIDA", "W00S-IDEPDC-TRAB-SAP"}, maxLength = 40, usage = "DI"
            + "SPLAY", fieldNumber = 5, maxBytes = 40, pic = "X(040)")
    protected String idepdcTrabSap = null;

    /**
     * Atributo codRetornoSarh
     */
    @CommareaField(name = {"W00S-BLOCO-ERRO", "W00S-COD-RETORNO-SARH"}, maxLength = 2, usage = "DISPLAY",
            fieldNumber = 6, maxBytes = 2, pic = "9(002)")
    protected int codRetornoSarh = 0;

    /**
     * Retornar o valor de codLayout
     *
     * @return codLayout
     */
    public String getCodLayout() {
        return codLayout;
    }

    /**
     * Atribuir valor ao codLayout
     *
     * @param value novo valor a ser atribuido para o campo
     */
    public void setCodLayout(String value) {
        this.codLayout = value;
    }

    /**
     * Retornar o valor de tamLayout
     *
     * @return tamLayout
     */
    public int getTamLayout() {
        return tamLayout;
    }

    /**
     * Atribuir valor ao tamLayout
     *
     * @param value novo valor a ser atribuido para o campo
     */
    public void setTamLayout(int value) {
        this.tamLayout = value;
    }

    /**
     * Retornar o valor de cjuncDepdcPertc
     *
     * @return cjuncDepdcPertc
     */
    public int getCjuncDepdcPertc() {
        return cjuncDepdcPertc;
    }

    /**
     * Atribuir valor ao cjuncDepdcPertc
     *
     * @param value novo valor a ser atribuido para o campo
     */
    public void setCjuncDepdcPertc(int value) {
        this.cjuncDepdcPertc = value;
    }

    /**
     * Retornar o valor de idepdcPertcSap
     *
     * @return idepdcPertcSap
     */
    public String getIdepdcPertcSap() {
        return idepdcPertcSap;
    }

    /**
     * Atribuir valor ao idepdcPertcSap
     *
     * @param value novo valor a ser atribuido para o campo
     */
    public void setIdepdcPertcSap(String value) {
        this.idepdcPertcSap = value;
    }

    /**
     * Retornar o valor de cjuncDepdcTrab
     *
     * @return cjuncDepdcTrab
     */
    public int getCjuncDepdcTrab() {
        return cjuncDepdcTrab;
    }

    /**
     * Atribuir valor ao cjuncDepdcTrab
     *
     * @param value novo valor a ser atribuido para o campo
     */
    public void setCjuncDepdcTrab(int value) {
        this.cjuncDepdcTrab = value;
    }

    /**
     * Retornar o valor de idepdcTrabSap
     *
     * @return idepdcTrabSap
     */
    public String getIdepdcTrabSap() {
        return idepdcTrabSap;
    }

    /**
     * Atribuir valor ao idepdcTrabSap
     *
     * @param value novo valor a ser atribuido para o campo
     */
    public void setIdepdcTrabSap(String value) {
        this.idepdcTrabSap = value;
    }

    /**
     * Retornar o valor de codRetornoSarh
     *
     * @return codRetornoSarh
     */
    public int getCodRetornoSarh() {
        return codRetornoSarh;
    }

    /**
     * Atribuir valor ao codRetornoSarh
     *
     * @param value novo valor a ser atribuido para o campo
     */
    public void setCodRetornoSarh(int value) {
        this.codRetornoSarh = value;
    }

    /**
     * Construtor Padrão
     */
    public SARHW00SResponse() {
        super();
    }
}
