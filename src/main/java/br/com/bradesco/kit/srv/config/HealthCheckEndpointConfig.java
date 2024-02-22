package br.com.bradesco.kit.srv.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * Terá carregamento condicional se APENAS houver mapeamento no arquivo application-
 * <ambiente>.yml com a chave 'health-check-endpoint-custom'.
 */
@Configuration
@ConfigurationProperties(prefix = "health-check-endpoint-custom")
@ConditionalOnProperty(prefix = "health-check-endpoint-custom", name = "endpoints")
public class HealthCheckEndpointConfig {

    private List<String> endpoints;

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }
}
