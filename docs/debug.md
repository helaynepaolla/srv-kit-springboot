
## Execução local em modo Debug

- Para executar em modo debug com a IDE
- A aplicação vai realizar bind na porta 5005:

```
java  -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=5005,suspend=y -jar target/srv-kit-springboot.jar
```

- Vá no IntelliJ Run > Edit configurations... > Clique '+' > Remote JVM Debug
- Preencha os campos e após, clique em Ok/Apply.
  - Name: AppRemote
  - Host: localhost
  - Port: 5005
- Por fim, ainda no IntelliJ, Run > Debug 'AppRemote'.
