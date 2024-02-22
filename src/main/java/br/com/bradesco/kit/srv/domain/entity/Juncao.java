package br.com.bradesco.kit.srv.domain.entity;

public class Juncao {

    private final Integer codigo;
    private final String descricao;

    public Juncao(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return this.codigo;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
