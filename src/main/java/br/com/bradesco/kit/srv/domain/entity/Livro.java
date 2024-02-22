package br.com.bradesco.kit.srv.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Livro implements Serializable {

    private static final long serialVersionUID = 6456088113745217816L;

    private final Integer id;
    private final String titulo;
    private final String autor;
    private final String editora;
    private final BigDecimal valor;
    private final int qtdEstoque;
    private final GeneroLivro genero;

    public Livro(Integer id, String titulo, String autor, String editora, BigDecimal valor,
                 int qtdEstoque, GeneroLivro genero) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.valor = valor;
        this.qtdEstoque = qtdEstoque;
        this.genero = genero;
    }

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getEditora() {
        return editora;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public int getQtdEstoque() {
        return qtdEstoque;
    }

    public GeneroLivro getGenero() {
        return genero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(autor, editora, titulo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Livro other = (Livro) obj;
        return Objects.equals(autor, other.autor) && Objects.equals(editora, other.editora)
                && Objects.equals(titulo, other.titulo);
    }

    @Override
    public String toString() {
        return "Livro [autor=" + autor +
                ", editora=" + editora +
                ", id=" + id +
                ", qtdEstoque=" + qtdEstoque +
                ", titulo=" + titulo +
                ", valor=" + valor +
                ", genero=" + genero.toString() + "]";
    }

}
