
## Execução local - Utilizando o Maven

- É possível subir a aplicação utilizando o próprio Maven. É útil para testes de desenvolvimento onde é necessário uma agilidade maior
- No diretório root do projeto executar:

```
mvn spring-boot:run
```

## Execução local - Profile LOCAL

- O profile LOCAL é indicado para execução na estação de desenvolvimento
- Deve ser ativado o profile LOCAL
- Irá atender sob o HOST http://localhost:8080/srv-kit-springboot/health

```
export SPRING_PROFILES_ACTIVE=LOCAL
java -jar target/srv-kit-springboot.jar

```

## Execução localmente - Profile DEV

- O profile DEV é indicado para execução na estação de desenvolvimento obtendo e consumindo recursos do ambiente DEV
- Deve ser ativado o profile DEV
- Irá atender sob o HOST http://localhost:8080/health

```
export SPRING_PROFILES_ACTIVE=DEV
java -jar target/srv-kit-springboot.jar
```

## Executando com aplicação em container local

* Verificar se a imagem com o mesmo nome da sua aplicação já existe:

```
docker images
```

* Se a imagem já existir, delete-a ou mude o nome

```
  docker rmi <NOME_DA_IMAGEM_DOCKER>
```

* Gerar a imagem Docker com a aplicação (deve-se executar build antes)
* Antes, navegue até o diretório docker. Precisará do arquivo Dockerfile

```
docker build -t srv-kit-springboot:latest
```

* Iniciando o container a partir do nome da imagem

```
docker run -dp 8088:3000 srv-kit-springboot
```