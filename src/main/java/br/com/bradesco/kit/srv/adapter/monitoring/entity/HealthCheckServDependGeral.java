package br.com.bradesco.kit.srv.adapter.monitoring.entity;

import java.util.List;

/**
 * Healtch Check customizado especifico para identificar a saude dos servicos dependentes
 */
public record HealthCheckServDependGeral(List<HealthCheckServDepend> healthCheckServDependList,
                                         boolean todosServicosUP) {
}
