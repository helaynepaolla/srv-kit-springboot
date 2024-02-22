package br.com.bradesco.kit.srv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.ForkJoinPool;

/**
 * Configuração relacionado ao ForkJoin para permitir otimizar o pool de threads e apoiar em requisições paralelas e
 * que utilizem CompletableFuture ou classes relacionados a concorrência.
 *
 * @see java.util.concurrent.CompletableFuture
 */
@Configuration
public class ForkJoinConfig {

    @Value("${server.parallelism}")
    private String parallelism;

    @Bean
    public ForkJoinPool getForkJoinPool() {
        return new ForkJoinPool(Integer.valueOf(parallelism));
    }

    @PostConstruct
    public void setProperties() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", parallelism);
    }

}

