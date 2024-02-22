# srv-kit-springboot - Artefato Kit de SRV - microservice em Java e Spring Boot :rocket:

[![Quality Gate Status](https://sonarqube.bradesco.com.br:8443/sonarqubeenterprise/api/project_badges/measure?project=br.com.bradesco.kit%3Asrv-kit-springboot&metric=alert_status&token=f81f703d88798fa5b3584807a6fd5f242249afb4)](https://sonarqube.bradesco.com.br:8443/sonarqubeenterprise/dashboard?id=br.com.bradesco.kit%3Asrv-kit-springboot)
[![Bugs](https://sonarqube.bradesco.com.br:8443/sonarqubeenterprise/api/project_badges/measure?project=br.com.bradesco.kit%3Asrv-kit-springboot&metric=bugs&token=f81f703d88798fa5b3584807a6fd5f242249afb4)](https://sonarqube.bradesco.com.br:8443/sonarqubeenterprise/dashboard?id=br.com.bradesco.kit%3Asrv-kit-springboot)
[![Code Smells](https://sonarqube.bradesco.com.br:8443/sonarqubeenterprise/api/project_badges/measure?project=br.com.bradesco.kit%3Asrv-kit-springboot&metric=code_smells&token=f81f703d88798fa5b3584807a6fd5f242249afb4)](https://sonarqube.bradesco.com.br:8443/sonarqubeenterprise/dashboard?id=br.com.bradesco.kit%3Asrv-kit-springboot)
[![Coverage](https://sonarqube.bradesco.com.br:8443/sonarqubeenterprise/api/project_badges/measure?project=br.com.bradesco.kit%3Asrv-kit-springboot&metric=coverage&token=f81f703d88798fa5b3584807a6fd5f242249afb4)](https://sonarqube.bradesco.com.br:8443/sonarqubeenterprise/dashboard?id=br.com.bradesco.kit%3Asrv-kit-springboot)

<br /><br />
![Java](https://img.shields.io/badge/Java-17-green?style=plastic&logo=java)
![Spring Boot](https://img.shields.io/badge/SpringBoot-2.7.9-green?style=plastic&logo=spring)
![Spring Cloud](https://img.shields.io/badge/SpringCloud-2021.0.3-green?style=plastic&logo=spring)
![JUnit](https://img.shields.io/badge/JUnit-5-green?style=plastic&)
![Maven](https://img.shields.io/badge/Maven-green?style=plastic)

## Principais características e responsabilidades deste artefato

- Atua como intermediador para
  o [bff-kit-springboot](https://bitbucket.bradesco.com.br:8443/projects/ENGCL/repos/bff-kit-springboot/browse)
- Consome informações de entidades externas: Base de Dados, Mainframe, Cache Redis, SRV - microservices, etc
- Envio de status de execução da pipeline via MS Teams para o grupo e canal configurado em `pipeline-notifications.json`

## Por que algumas coisas são como são?

Foram tomadas algunas decisões para o kit de forma priorizar o desenvolvimento e manutenção das aplicações que virão utilizar o kit para construir seus serviços.
Entre eles podemos citar algumas decisões como:
- Arqutetura Hexagonal
- BDD (Behavior Driven Development)
- DDD (Domain driven Development)
- Spring Default Profile [ver mais sobre...](./docs/spring-profiles.md)

## Pré-requisitos para desenvolvimento

- Possuir acesso no [repositório NEXUS Bradesco](https://nexusrepository.bradesco.com.br:8443/)
- Possuir o [git](https://git-scm.com/) devidamente instalado e configurado
- Possuir o [Apache Maven](https://maven.apache.org/download.cgi) devidamente instalado e configurado.
- Possuir instalado Java Development Kit (JDK) - versão mínima 17 [download aqui](https://www.oracle.com/java/technologies/javase-downloads.html)
- Possuir uma IDE/editor da sua preferência:
  - [Intellij IDEA](https://www.jetbrains.com/idea/)
  - [Eclipse IDE](https://www.eclipse.org/ide/)
  - [VSCode](https://code.visualstudio.com/download)
- Possuir um container na sua estação (Docker Desktop/Rancher Desktop/Podman/containerd/etc).
  - Caso queira instalar o WSL (windows Subsystem Linux) [Veja aqui](./docs/installing-wsl-docker.md)

## Build

```
mvn clean install
```

Duvidas? [Mais informações aqui](./docs/build.md)

## Tests

```
mvn test
```

Duvidas? [Mais informações aqui](./docs/tests.md)

## Running

```
mvn spring-boot:run
```

ou

```
docker run -p 8080:8080 --rm -ti $(docker build -q .)
```

Duvidas? [Mais informações aqui](./docs/running.md)

## Dependencias Externas

Serviços mockados, redis, etc...

```
docker-compose up -d
```

Duvidas? [Mais informações aqui](./docs/external-deps.md)

## Instrumentação

- `Open Telemetry Agent` - Java Agent utilizado para geração de log de negócio e demais atribuidos de rastreabilidade (SpanID, TraceID) 

[Veja mais sobre](./docs/otlp-agent.md)

## Acessando a aplicação

Verifique qual o host a app está acessivel, localmente é usado o endereço `http://localhost:8080/`

- `/health` - Healthcheck generico para verificação da saude
- `/health/readiness` - Healthcheck para verificar aplicação está apta para receber requisições
- `/health/liveness` - Healthchech para verificar se aplicação está viva
- `/swagger-ui/index.html` - Home de acesso da API do swagger

## Precisa de mais informações?

- Link
  para [COE | Cloud > LEAP > Leap - Arquitetura de referência](https://confluence.bradesco.com.br:8443/pages/viewpage.action?pageId=136730699)

- Link
  para [CORPORATIVO | Programa Leap > bradesCode > Treinamento Cloud Pública Bradesco](https://confluence.bradesco.com.br:8443/pages/viewpage.action?pageId=244976149)