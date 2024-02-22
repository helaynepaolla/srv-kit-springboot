# README - WSL no windows e Container REDIS:

### OBJETIVO<br /><br />

Os objetivos do texto abaixo são auxiliar:

- Instalação e atualização do WSL no Windows
- Instalação do ubuntu ou outra distro linux,
- Instalação do Docker na distro linux instalada
- Fazer o deploy de um container com imagem do **REDIS** para uso de cache.

<br />

### ÍNDICE

1. [ Instalar WSL no Windows ](#Instalação)
2. [ Ubuntu e outras Distribuição Linux ](#Distribuições)
3. [ Atualizar a versão do WSL 1 para o WSL 2 ](#Atualizar)
4. [ Como encontrar arquivos no /mnt ](#Mnt)
5. [ Instalação do Docker no WSL ](#Docker)
6. [ Execução do Container Redis ](#Redis)
   <br /><br />

<a name="instalarwslnowindows"></a>

### Pré-requisitos para instalação WSL

- Você deve estar executando o Windows 10 versão 2004 e superior (Build 19041 e superior) ou o Windows 11 para usar os
  comandos abaixo.

- Se você estiver em versões anteriores, consulte a página
  de [instalação manual](https://learn.microsoft.com/pt-br/windows/wsl/install-manual).

Para verificar a versão atual do windows instalado,execute o comando abaixo no CMD ou Windows Power Shell.

```
systeminfo
```

<br /><br />

<a name="instalação"></a>

### Instalação [[Tutorial Oficial Microsoft](https://learn.microsoft.com/pt-br/windows/wsl/install)]

É possível realizar a instalação de todas as dependências e do próprio WSL com apenas um comando.

- Abra o PowerShell ou o Prompt de Comando do Windows no modo de administrador clicando com o botão direito do mouse e
  selecionando "Executar como administrador";

- Insira o comando wsl --install e reinicie o computador
- Esse comando habilitará os recursos necessários para executar o WSL e instalar a distribuição Ubuntu do Linux.

```
wsl --install
```

<br /><br />

<a name="testes"></a>

### Distribuições Linux

Por padrão, a distribuição do Linux instalada será o Ubuntu. Isso pode ser alterado usando o sinalizador -d

- Para alterar a distribuição instalada, insira: wsl --install -d <Distribution Name>. Substitua <Distribution Name>
  pelo nome da distribuição que você gostaria de instalar.

- Para ver uma lista das distribuições do Linux disponíveis para download por meio da loja online, insira: wsl --list
  --online ou wsl -l -o.

- Para instalar distribuições adicionais do Linux após a instalação inicial, você também pode usar o comando: wsl
  --install -d <Distribution Name>.

**Distribuições Linux disponíveis para instalação:**

```
| Name           | FRIENDLY NAME                     |
| -------------- | ------------- ------------------- |
| Ubuntu         | Ubuntu                            |
| Debian         | Debian GNU/Linux                  |
| Kali-linux     | Kali Linux Rolling                |
| OpenSUSE-42    | OpenSUSE Leap 42                  |
| SLES-12        | SUSE Linux Enterprise Server v12  |
| Ubuntu-16.04   | Ubuntu-16.04 LTS                  |
| Ubuntu-18.04   | Ubuntu-18.04 LTS                  |
| Ubuntu-20.04   | Ubuntu-20.04 LTS                  |
```

Se você estiver executando um build mais antigo ou simplesmente prefere não usar o comando para
instalar e quer instruções passo a
passo:[Etapas de instalação manual para versões mais antigas do WSL | Microsoft Learn](https://learn.microsoft.com/pt-br/windows/wsl/install-manual)


<br />

#### Atualizar a versão do WSL 1 para o WSL 2 [[Tutorial Oficial Microsoft](https://learn.microsoft.com/pt-br/windows/wsl/install)]

- As novas instalações do Linux, instaladas usando o comando `wsl --install`, serão definidas como WSL 2 por padrão.

- Para ver se a distribuição do Linux está definida como WSL 1 ou WSL 2, use o comando: `wsl -l -v`.

- Para atualizar de versão, use o comando: `wsl --set-version <distro name> 2`, substituindo <distro name> pelo nome da
  distribuição do Linux que você quer atualizar.

- Exemplo, `wsl --set-version Ubuntu-20.04 2` definirá sua distribuição do Ubuntu 20.04 para usar o WSL 2.

- Se você instalou o WSL manualmente poderá ser necessário habilitar o componente opcional de máquina virtual usado pelo
  WSL 2 e instalar o pacote do kernel, se isso ainda não foi feito.

```
wsl --set-version Ubuntu-20.04 2
```

<br /><br />

<a name="comoencontrararquivosnomnt"></a>

### Como encontrar arquivos no /mnt

Para encontrar os arquivos que estejam no disco C utilize o comando `/mnt/c` ou `/mnt/<drive letter>/` dentro da
instância Linux instalada no WSL

```
/mnt/c
```

<br />


<a name="instalaçãododockernowsl"></a>

### Instalação do Docker no WSL [[Tutorial Instalação Docker no WSL](https://educoutinho.com.br/windows/instalando-docker-no-wsl/)]

É possível rodar o Docker dentro do WSL do Windows, sem precisar instalar o Docker Desktop evitando assim problemas com
licenças. Abaixo os detalhes da instalação:

- Abra um prompt do WSL, cline em iniciar/pesquise por “Ubuntu” e clique no ícone “Ubuntu”

- Utilizando esse terminal você pode executar comandos Linux, que serão executados na distribuição Linux instalada no
  WSL.

- Se você estiver rodando o Ubuntu em uma versão anterior ao Ubuntu 20.10, é necessário atualizar:
  Verificar a versão instalada:

```
cat /etc/os-release
```

- Se for inferior a 20.10, atualizar o Ubuntu:

```
sudo apt-get update
sudo apt dist-upgrade
sudo apt install update-manager-core
sudo apt autoremove
sudo do-release-upgrade -d
```

- Antes de Instalar o Docker, remova versões antigas com o comando abaixo, Se não tiver docker instalado, será exibida a
  seguinte mensagem “Unable to locate package docker-engine”

```
sudo apt-get remove docker docker-engine docker.io containerd runc
```

- Instale as dependências necessarias:

```
sudo apt-get update && sudo apt-get upgrade
sudo apt-get install ca-certificates curl gnupg lsb-release
sudo apt-get autoremove
```

- Adicione chave do repositório oficial do Docker:

```
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

- Instale o Docker Engine com o comando:

```
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

- Verifique se a instalação está correta:

```
docker –version
docker compose version
```

<br />

<a name="execuçãodocontainerredis"></a>

### Execução do Container Redis

- Localize o arquivo "docker -compose.yaml", dentro do diretório `srv-kit-springboot`, via terminal.


- Execute o comando abaixo para fazer o deploy de um container com a imagem do Redis.

```
docker-compose -f .\docker-compose.yml up -d 
```

- Para verificar status do container, execute:

```
docker ps
```

### Precisa de mais informações?

- Link
  para [Tutorial Oficial Microsoft](https://learn.microsoft.com/pt-br/windows/wsl/install)

- Link
  para [Tutorial Oficial Microsoft - Instalação Manual](https://learn.microsoft.com/pt-br/windows/wsl/install-manual)

- Link
  para [Tutorial Oficial Microsoft - Docker no WSL 2](https://learn.microsoft.com/pt-br/windows/wsl/tutorials/wsl-containers)

- Link
  para [Documentação Oficial Docker - Docker Desktop integrado ao WSL no Windows](https://docs.docker.com/desktop/windows/wsl/)

- Link
  para [Instalando o Docker no WSL (alternativa ao Docker Desktop)](https://educoutinho.com.br/windows/instalando-docker-no-wsl/)
