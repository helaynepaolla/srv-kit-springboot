## Dependencia externas Redis, Mocks e outros

Para a execução de varios container ao mesmo tempo e controlar suas funcionalidades, usamos o docker-compose. Uma ferramenta que normalmente vem junto da instalação do DOCKER.

### Verificando o docker compose

Isso listará qual a versão do docker-compose instalado:
```
docker-compose version
```

Para ver quais comandos é possivel executar ou quais parametros:
```
docker-compose --help
docker-compose up --help
```

### Rodando as dependencias do kit

Para verificar as dependencias veja o arquivo [docker-compose.yml na raiz](./../docker-compose.yml).

Para isso alguns dependencias foram convenientemente agrupadas e configuradas para o kit.

Execute todas as dependencias em background:
```
docker-compose up -d
```

Voce pode consultar a api do mock-server no endereço: http://localhost:1080/mock-livraria/