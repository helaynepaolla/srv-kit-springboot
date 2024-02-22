# spring profiles

São uma forma de vc categorizar e separar configurações e dependencias que são usadas em diferentes contextos e situações.

## O que são spring profiles?

Os profile são uma parte importante do framework Spring. E são configurações carregados no lugar do 'application.yml' e seguem o padrão "application-PROFILE.yml".

Por exemplo, sua aplicação está em produção com erros que não são logados, porem sua aplicação suporta uma configuração para aumentar o nivel de log e assim exibir logs de info que darão o que vc precisa.
Nesse caso, sua aplicação tem um profile de 'debugger' nesse profile estão as configurações de log das bibliotecas e aplicação no nivel de log INFO.

## Como usar?

Hoje o kit conta com o profile "DEFAULT" que é o padrão, então não precisa ser informado na config `spring.profiles.active`.

Esse profile padrão foi justamente usado para simplificar o desenvolvimento e o arquivo `application-default.yml` ser o unico arquivo de configuração que estarão as configurações locais.

A função desse profile default é justamente deixar mais no estilo do "CONVENTION OVER CONFIGURATION".


## Recomendação de uso no kit

Deixe apenas no `application-defaul.yml` as configurações explicitas e estaticas, sem uso de variaveis de ambiente.

```
server:
  port: 8080
  parallelism: 200
spring:
  redis:
    host: localhost
```

Coloque no `application.yml` as configurações que variam por ambiente, pegando o valor de variavel de ambiente no arquivo, exemplo:

```
server:
  port: ${SERVER_PORT}
  parallelism: ${SERVER_PARALELISM}
spring:
  redis:
    host: ${CACHE_REDIS_HOST}
```

Para o uso em tests faça as configurações programaticamente na sua classe/cenário de test sempre que tiver necessidade:
Como exemplo veja a classe [CircuitBreakerTest](./../src/test/java/br/com/bradesco/kit/srv/adapter/output/CircuitBreakerTest.java)

```
@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = { "spring.main.allow-bean-definition-overriding=true", "spring.redis.port=6370",
        "spring.redis.host=localhost" })
@Import(value = { CircuitBreakerAutoConfiguration.class })
@Nested
@EnableAspectJAutoProxy
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CircuitBreakerTest {

    @Mock
    private RestTemplate restTemplate;

    //...
}
```

### Vantagens:
- Evita problemas de configuração
- Torna explicito a configuração que está usando
- Facilita debugging
- Facilidade na identificação de quais cenarios estão cobertos por testes
- Evita side-effects causados por configurações para outros testes
- Aumenta a cobertura, por deixar em evidencia o que vc está testando vc fará os testes que faltam.
- Descreve as configurações das dependencias que um fluxo/cenário precisa ter configurado para funcionar.

# FAQ (why questions)
## Por que não temos arquivos de profile para cada ambiente?

Por que isso causa muitos problemas de atualização de valores com o decorrer do tempo. Alem de gerar retrabalho quando uma configuração dessas está no lugar ou valor errados.

Detectar um valor errado nem sempre é uma tarefa fácil de realizar. Fazer debuging e olhar varios logs com atenção podem ser necessários até entender que é uma configuração errada que está causando um comportamento errado na aplicação.

## Cade o profile de test?

Por usarmos o profile default, os testes executarão por padrão com as configs de execução local. Não sendo necessário ter um profile especifico para test.

## Mas eu preciso de configuração especifica para test! como eu faço?

Se vc precisa de uma configuração especifica a nivel de aplicação em um arquivo de configuração é melhor refatorar o arquivo de testes em mais cenários/arquivos/classes de testes.

Imagina que vc tem um arquivo de test para testar o resttemplate e outro que vc usa para testar o feign.
Para garantir o uso dessas libs vc já teria 2 arquivos de config.
Agora imagina que vc tenha uma config que permite opcionalmente o certificado.
Ai seriam mais 2 arquivos de configuração um para o resttemplate e outro para o feign.
Total de arquivos de configuração 4, e olha que nem colocamos todas as configurações necessárias.
