package br.com.bradesco.kit.srv.config;

import br.com.bradesco.kit.srv.adapter.exception.handler.RestTemplateResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Para o caso de não desejar utilizar o FeignClient, utilize o RestTemplate com OkHttpClient para realizar
 * requisições HTTP.
 */
@Configuration
public class RestTemplateConfig {


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, OkHttpConfig okHttpConfiguration, RestTemplateInterceptor interceptor) {
        var restTemplate = builder.requestFactory(okHttpConfiguration).build();
        // add the interceptor that will handle logging
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(interceptor);
        restTemplate.setInterceptors(interceptors);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        //Alterando o ErrorHandler default - DefaultResponseErrorHandler
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        return restTemplate;
    }
}
