package br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa.dto;

import br.com.bradesco.data.connector.AnnotatedAdapter;
import br.com.bradesco.data.connector.commarea.annotations.types.CommareaField;

/**
 * Representação do copybook SARHW00E.
 *
 * @author Engenharia DS
 * @version 1.0
 */
public class SARHW00ERequest extends AnnotatedAdapter {

    /**
     * Atributo codLayout
     */
    @CommareaField(name = {"W00E-HEADER", "W00E-COD-LAYOUT"}, maxLength = 8, usage = "DISPLAY",
            fieldNumber = 0, maxBytes = 8, pic = "X(008)")
    protected String codLayout = "SARHW00E";

    /**
     * Atributo tamLayout
     */
    @CommareaField(name = {"W00E-HEADER", "W00E-TAM-LAYOUT"}, maxLength = 5, usage = "DISPLAY",
            fieldNumber = 1, maxBytes = 5, pic = "9(005)")
    protected int tamLayout = 27;

    /**
     * Atributo tpoPesq
     */
    @CommareaField(name = {"W00E-REGISTRO", "W00E-ENTRADA", "W00E-TPO-PESQ"}, maxLength = 1, usage = "DISPLAY",
            fieldNumber = 2, maxBytes = 1, pic = "9(01)")
    protected int tpoPesq = 0;

    /**
     * Atributo rotina
     */
    @CommareaField(name = {"W00E-REGISTRO", "W00E-ENTRADA", "W00E-ROTINA"}, maxLength = 4, usage = "DISPLAY",
            fieldNumber = 3, maxBytes = 4, pic = "X(04)")
    protected String rotina = null;

    /**
     * Atributo cfuncBdsco
     */
    @CommareaField(name = {"W00E-REGISTRO", "W00E-ENTRADA", "W00E-CFUNC-BDSCO"}, maxLength = 9, usage = "DISPL"
            + "AY", fieldNumber = 4, maxBytes = 9, pic = "9(09)")
    protected int cfuncBdsco = 0;

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
     * Retornar o valor de tpoPesq
     *
     * @return tpoPesq
     */
    public int getTpoPesq() {
        return tpoPesq;
    }

    /**
     * Atribuir valor ao tpoPesq
     *
     * @param value novo valor a ser atribuido para o campo
     */
    public void setTpoPesq(int value) {
        this.tpoPesq = value;
    }

    /**
     * Retornar o valor de rotina
     *
     * @return rotina
     */
    public String getRotina() {
        return rotina;
    }

    /**
     * Atribuir valor ao rotina
     *
     * @param value novo valor a ser atribuido para o campo
     */
    public void setRotina(String value) {
        this.rotina = value;
    }

    /**
     * Retornar o valor de cfuncBdsco
     *
     * @return cfuncBdsco
     */
    public int getCfuncBdsco() {
        return cfuncBdsco;
    }

    /**
     * Atribuir valor ao cfuncBdsco
     *
     * @param value novo valor a ser atribuido para o campo
     */
    public void setCfuncBdsco(int value) {
        this.cfuncBdsco = value;
    }

    /**
     * Construtor Padrão
     */
    public SARHW00ERequest() {
        super();
    }
}
