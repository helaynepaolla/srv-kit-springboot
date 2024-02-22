package br.com.bradesco.kit.srv.adapter.output.log;

import br.com.bradesco.enge.logcloud.canal.ReturnCode;
import br.com.bradesco.enge.logcloud.canal.SrvCanalLog;
import br.com.bradesco.enge.logcloud.canal.SrvCanalLogger;
import br.com.bradesco.kit.srv.domain.entity.Livro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class LogServicoCanalTest {

    private static final ReturnCode CODE_INDEFINIDO = ReturnCode.INDEFINIDO;
    private final Integer idDoLivro = 1;
    @Mock
    private Livro livro;
    @Mock
    private SrvCanalLogger logger;
    @InjectMocks
    private LogServicoCanal subject;

    @Test
    void fazORegistroDoLogParaConsultaTodosLivros() {
        List<Livro> listaLivros = null;

        subject.logConsultaTodosLivros(CODE_INDEFINIDO, listaLivros);

        then(logger).should().log(any(SrvCanalLog.class));
    }

    @Test
    void fazORegistroDoLogParaConsultaLivroPorId() {
        subject.logConsultaLivroPorId(CODE_INDEFINIDO, idDoLivro, livro);

        then(logger).should().log(any(SrvCanalLog.class));
    }

    @Test
    void fazORegistroDoLogParaConsultaLivroPorIdException() {
        RuntimeException erro = new RuntimeException("To log this message!");

        subject.logConsultaLivroPorIdException(CODE_INDEFINIDO, idDoLivro, erro);

        then(logger).should().log(any(SrvCanalLog.class));
    }

    @Test
    void fazORegistroDoLogParaExclusaoLivro() {
        subject.logExclusaoLivro(CODE_INDEFINIDO, idDoLivro);

        then(logger).should().log(any(SrvCanalLog.class));
    }

    @Test
    void fazORegistroDoLogParaInclusaoLivro() {
        subject.logInclusaoLivro(CODE_INDEFINIDO, livro);

        then(logger).should().log(any(SrvCanalLog.class));
    }

}
