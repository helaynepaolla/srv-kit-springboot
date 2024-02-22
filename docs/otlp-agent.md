# opentelemetry.io

Configurando o open telemetry automaticamente na aplicação!

https://opentelemetry.io/docs/instrumentation/java/automatic/

## EXECUTAR LOCALMENTE UMA APLICAÇÃO COM OPENTELEMETRY

Para rodar local:

```
java -javaagent:target/opentelemetry-javaagent.jar -Dotel.traces.exporter=none -Dotel.metrics.exporter=none -jar target/srv-kit-springboot.jar
```

## Guia de uso 

https://confluence.bradesco.com.br:8443/display/CPPLEAP/8.1.1+-+%5BLogCloud%5D+Guia+de+Uso

## Como adicionar outros dados no APPENDER

https://confluence.bradesco.com.br:8443/pages/viewpage.action?pageId=290426775

## Configuração do Logger automatico

```
br.com.bradesco.enge.logcloud.spring.EnableLoggingAutoConfiguration
```

https://confluence.bradesco.com.br:8443/pages/viewpage.action?pageId=290426765

## Projeto de exemplo de logs

https://confluence.bradesco.com.br:8443/pages/viewpageattachments.action?pageId=290426745