Desafio Hyperativa
==================

## Sobre o desafio

### Criação de API para cadastro e consulta de número de cartão completo

Você precisa criar uma API com os seguintes requisitos:

#### End-point para autenticação do usuário

* O cliente deve realizar uma autenticação (JWT ou OAuth2) para realizar o uso da API.

#### End-point para inserção de dados

* O cliente poderá inserir os dados através de requisições informando um único cartão ou a partir de arquivo TXT a API.
* Defina o contrato da API com os padrões a serem adotados para integração.
* Escolha o banco de dados que achar melhor e a estrutura que achar mais adequada.
* Por serem dados sensíveis toda informação deve ser armazenada de maneira segura no banco de dados.

#### End-point para consulta de dados

* O cliente consulta se determinado número de cartão completo existe na base de dados e retorna um identificador único do sistema;

#### Requisitos Obrigatórios

* Logar as requisições de uso da API e seus retornos.
* Usar linguagem C# com Framework .NET ou Python com Flask ou Django;

#### Requisitos Opcionais (Não necessário)

* Ter uma cobertura de teste unitários relativamente boa.
* Utilizar criptografia (end-to-end encryption) para tráfego de informações.

## Orientações
* Procure fazer uma API sucinta. 
* O arquivo TXT com o formato que o cliente irá enviar estão no repositório.
* Pensar em escalabilidade, pode ser uma quantidade muito grande de dados.
* Coloque isso em um repositório GIT.
* Colocar as orientações de setup e uso no README do seu repositório.

# Boa sorte 
