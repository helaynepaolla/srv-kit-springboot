# Atualizações neste Diretório

_Onde estão os arquivos deployment.yaml.j2, service.yaml.j2, etc, que ficavam dentro deste diretório?_

Os templates de manifesto Kubernetes, usando o jinja2, que ficavam neste diretório foram movidos para o repo de scripts de pipeline e serão adicionados automaticamente durante os runs de sua pipe. O intuito desta iniciativa foi diminuir a carga cognitiva para o desenvolvedor no entendimento da pipeline e também a manutenção dos mesmos.

Caso você precise manipular, ou melhor, criar um template personalizado que atenda melhor seu caso de uso, por favor, consulte a [Documentação](https://confluence.bradesco.com.br:8443/x/JhCKFw).

**_WARN:_** Por favor, apesar de vazio, não remova esse diretório ou este arquivo do seu REPO.