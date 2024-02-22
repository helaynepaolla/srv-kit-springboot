## Endpoints padrão da aplicação

Mais detalhes abaixo

#### Spring Actuator

- Está configurado para atender APENAS o /health por questões de segurança
- Não expõs dados de disco (diskSpace)
- Está configurado para ser utilizado pelos [probes liveness e readiness - Kubernetes](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)
- Se executar com o profile LOCAL, irá atender sob HOST http://localhost:8080/health

#### Swagger

- Link da interface gráfica da aplicação para o Swagger UI
  - [Executando Localmente - Profile LOCAL](http://localhost:8080/srv-kit-springboot/swagger-ui/index.html)
- Por questões de segurança, o Swagger está devidamente configurado para estar desabilitado em ambiente produtivo - PRD
- Para utilização em outro ambiente deverá utilizar o IP do servidor: `http://IP_SERVIDOR:8080/swagger-ui/index.html`
