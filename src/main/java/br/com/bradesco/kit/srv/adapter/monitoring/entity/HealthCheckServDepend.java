package br.com.bradesco.kit.srv.adapter.monitoring.entity;

import org.springframework.boot.actuate.health.Health;

/**
 * Healtch Check customizado especifico para identificar a saude dos servicos dependentes
 */
public record HealthCheckServDepend(String url, Health health) {
}
