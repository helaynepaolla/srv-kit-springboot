package br.com.bradesco.kit.srv.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class LoadCWSConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadCWSConfig.class);

    @PostConstruct
    void postConstruct() {
        // verify if it load the config
        String cwshost = System.getProperty("ENGE_CWS_SERVER");
        if (StringUtils.isBlank(cwshost)){
            LOGGER.warn("Tentando carregar as configuracoes de fallback .env apenas para execucao LOCAL do CWS... nao executar em DEV, HOM ou PR.");
            Dotenv.configure().ignoreIfMissing().systemProperties().load();
            cwshost = System.getProperty("ENGE_CWS_SERVER");
            LOGGER.error("""
                    As configuracoes do CWS (conector do mainframe) nao foram carregadas do configmap.
                        O conector CWS somente aceita configuracao por variavel de ambiente, nao sendo possivel configurar por 'application.yml'.
                        Como referencia para o configmap veja o arquivo '.env', uso exclusivo para desenvolvimento LOCAL.
                        Nunca usar o .env para ambiente DEV, HOM, ou PROD.
                    """);
        }
        LOGGER.info("Configuracao do CWS carregada com SUCESSO!");
    }
}
