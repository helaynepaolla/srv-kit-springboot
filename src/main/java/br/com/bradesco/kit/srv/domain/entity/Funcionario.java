package br.com.bradesco.kit.srv.domain.entity;

public class Funcionario {

    private final String rotina;
    private final Integer matricula;

    public Funcionario(String rotina, Integer matricula) {
        this.rotina = rotina;
        this.matricula = matricula;
    }

    public String getRotina() {
        return rotina;
    }

    public Integer getMatricula() {
        return matricula;
    }
}
