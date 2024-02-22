package br.com.bradesco.kit.srv.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LivroTest {

    private static final GeneroLivro generoLivro = GeneroLivro.ACAO;
    private final String titulo = "Meu Livro";
    private final String editor = "Meu Editor";
    private final String autor = "Meu Autor";
    private final int qtd = 1;
    private final Integer id = 1;
    private Livro subject;

    @BeforeEach
    public void autoSetup() {
        this.setup(id, titulo, autor, editor, BigDecimal.TEN, qtd, generoLivro);
    }

    public void setup(Integer id, String titulo, String autor, String editor, BigDecimal valor, int qtd, GeneroLivro generoLivro) {
        subject = new Livro(id, titulo, autor, editor, valor, qtd, generoLivro);
    }

    @Test
    void equalsSucessoPorTituloAutorEditorEIDDiferente() {
        Integer outroID = 2;
        Livro livroIgual = new Livro(outroID, titulo, autor, editor, BigDecimal.TEN, qtd, generoLivro);

        assertEquals(livroIgual, subject, "Ambos os livros devem ter Titulo, Editor e Autor para serem iguais");
    }

    @Test
    void equalsFalsoSeComparadoANull() {
        assertNotEquals(null, subject, "Objeto comparado a null é falso!");
    }

    @Test
    void equalsFalsoSeComparadoComClassesDiferentesPoremMesmosValores() {
        class LivroFilho extends Livro {
            public LivroFilho(Integer id, String tituloLivro, String autor, String editora, BigDecimal valorLivro,
                              int qtdEstoque) {
                super(id, tituloLivro, autor, editora, valorLivro, qtdEstoque, generoLivro);
            }
        }
        LivroFilho mesmosValoresPoremFilho = new LivroFilho(id, titulo, autor, editor, BigDecimal.TEN, qtd);
        assertNotEquals(mesmosValoresPoremFilho, subject, "Objeto comparado são de classes diferentes!");
    }

    @Test
    void testandoGettersESettersDoLivro() {
        String outroAutor = "sou outro";
        String outraEditora = "Lua Oculta";
        Integer outroId = 10;
        int outraQtd = 2;
        String outroTitulo = "O outro Lado";
        BigDecimal outroValor = BigDecimal.TEN;

        this.setup(outroId, outroTitulo, outroAutor, outraEditora, outroValor, outraQtd, generoLivro);

        assertEquals(outroAutor, subject.getAutor());
        assertEquals(outraEditora, subject.getEditora());
        assertEquals(outroId, subject.getId());
        assertEquals(outraQtd, subject.getQtdEstoque());
        assertEquals(outroTitulo, subject.getTitulo());
        assertEquals(outroValor, subject.getValor());
    }

    @Test
    void hashcodeUsandoTituloAutorEditora() {
        this.setup(id, titulo, autor, editor, BigDecimal.TEN, qtd, generoLivro);

        Livro livroComMesmosTituloAutorEditor = new Livro(null, titulo, autor, editor, null, 0, generoLivro);

        assertEquals(livroComMesmosTituloAutorEditor.hashCode(), subject.hashCode(), "Hashcode não deve se alterar por que os campos não são usados no hashcode.");
    }


    @Test
    void toStringComTodosOsCampos() {
        String expected = "Livro [autor=Meu Autor, editora=Meu Editor, id=1, qtdEstoque=1, titulo=Meu " +
                "Livro, valor=10, genero=ACAO]";

        assertEquals(expected, subject.toString());
    }
}
