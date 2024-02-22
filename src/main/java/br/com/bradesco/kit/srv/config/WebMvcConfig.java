package br.com.bradesco.kit.srv.config;

import br.com.bradesco.enge.logcloud.api.SrvCanalLoggerFactory;
import br.com.bradesco.enge.logcloud.canal.SrvCanalLogger;
import br.com.bradesco.frwk.connector.FrwkConnectorCws;
import br.com.bradesco.kit.srv.adapter.output.cws.frwk.sarhiaaa.SarhiaaaCaller;
import br.com.bradesco.kit.srv.adapter.output.log.LogServicoCanal;
import br.com.bradesco.kit.srv.domain.usecase.ConsultarJuncao;
import br.com.bradesco.kit.srv.domain.usecase.ManutencaoEstoqueLivro;
import br.com.bradesco.kit.srv.port.input.ConsultarJuncaoUseCase;
import br.com.bradesco.kit.srv.port.output.ConsultaJuncaoGateway;
import br.com.bradesco.kit.srv.port.output.IEstoque;
import br.com.bradesco.kit.srv.port.output.ILogServicoCanal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${external-endpoints.livrariaService.url}${external-endpoints.livrariaService.get.path}")
    private String connectionURL;

    @Bean(name = "connectionURL")
    public String getConnectionURL() {
        return connectionURL;
    }


    //Para utilizar Feign ao inves do RestTemplate, trocar o nome do bean (IEstoqueFeignClient) qualifier
    @Bean
    @ApplicationScope
    ManutencaoEstoqueLivro getManutencaoEstoqueLivro(@Qualifier("IEstoqueRestTemplate") IEstoque estoque, ILogServicoCanal logServicoCanal) {
        return new ManutencaoEstoqueLivro(estoque, logServicoCanal);
    }

    @Bean
    public SrvCanalLogger getLogSrvCanalLogger() {
        return SrvCanalLoggerFactory.getLogger(LogServicoCanal.class);
    }

    @Bean
    @ApplicationScope
    ILogServicoCanal getLogServicoCanal(SrvCanalLogger logger) {
        return new LogServicoCanal(logger);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        ApplicationConversionService.configure(registry);
    }

	@Bean
	@RequestScope
	public ConsultarJuncaoUseCase juncaoUseCase(ConsultaJuncaoGateway juncaoGateway) {
		return new ConsultarJuncao(juncaoGateway);
	}

	@Bean
	@RequestScope
	public ConsultaJuncaoGateway juncaoGateway(FrwkConnectorCws connector) {
		return new SarhiaaaCaller(connector);
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
}
