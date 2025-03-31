
# Cartões Hyperativa

Este projeto tem como objetivo trabalhar um Sistema de Cartões da Hyperativa

O sistema foi subdividido em 3 projetos menores com finalidade de escalabilidade conforme:
   
    Um projeto para autentiacação (authentication)
    Um projeto para gerenciar cartoes (card) - criar e consultar
    Um projeto para processar lotes de criacao de cartoes (batch)

## Como rodar o projeto

Pré requisito: ter o docker e docker-compose instalados na máquina
    
Na pasta raiz do projeto existe um arquivo docker-compose que é responsável por rodar o sistema, para isso,
após fazer download do projeto e entrar nas pasta raiz do projeto basta rodar o comandos:

- Para iniciar: docker-compose up
- Para parar: docker-compose down

Quando executar o comando para iniciar, o docker subirá os serviços do mysql, ngnix, autorization-app, card-app e batch-app

## Detalhes do projeto

### Detalhes sobre o mysql:

Iniciará na porta padrão 3306, sendo o user hyperativa e senha password. As database de cada sistema
(authorization, card e batch) já serão criadas e concedidos os grants.
    
### Detalhes sobre o projeto nginx:

Está configurado o ssl mas como não tenho um certificado para  o mesmo foi gerado um certificado auto assinado:

openssl req -x509 -newkey rsa:4096 -keyout ./card.dazo.com.key -out ./card.dazo.com.crt -days 365 -nodes -subj "/CN=card.dazo.com"

openssl req -x509 -newkey rsa:4096 -keyout ./authorization.dazo.com.key -out ./authorization.dazo.com.crt -days 365 -nodes -subj "/CN=authorization.dazo.com"

Logo para funcionar deverá apontar o dns: card.dazo.com e authorization.dazo.com para o localhost. 
No linux que foi onde desenvolvi fiz a configuração no arquivo /etc/hosts. Ex.:
    127.0.0.1       card.dazo.com
    127.0.0.1       authorization.dazo.com

Os certificados e configuração do nginx estão na pasta raiz do projeto ./nginx.
    
### Detalhes sobre o sistema authorization:

Este projeto tem como objetivo authenticação para isso deve receber usuário e senha e retornara um token jwt que
deverá ser utilizado no sistema card.

O projeto foi criado utilizando spring boot 3.4, está conectando no mysql, está utilizando o liquibase para criar
tabelas e inserir registros, docker e maven para build, foi aplicado testes unitários que estão na pastas /test, 
foi aplicado testes de integração utilizando h2 que estão na pasta integration-test.

Para utilizar o sistema basta utilizar o postman conforme collection e environment que deixei na raiz do projeto. 
O único endpoint exposto é login onde deve executado um post e ser informado username e password. A fim de testes 
deixei cadastrado dois users: hyperativa e davison com password senha com grants diferenciados que serão demonstrado no 
projeto card. Na collection note que no menu tests do postman foi configurado para setar o token em uma variavel 
de ambiente para que seja utilizado em requições do projeto card

Além disso, toda requisição é logada conforme requisito.

### Detalhes sobre o sistema card:

Este projeto tem como objetivo gerenciar a criação e consulta de cartões para isso deve receber um token válido gerado no
sistema authentication e utilizado os endpoints card e file.

O projeto foi criado utilizando spring boot 3.4, está conectando no mysql, está utilizando o liquibase para criar 
tabelas e inserir registros, docker e maven para build, foi aplicado testes unitarios que estão na pastas /test, 
foi aplicado testes de integração utilizando h2 que estão na pasta integration-test.

Neste projeto é possível criar um cartão executando um post no endpoint /card e informando o number do cartão no body Ex:

    curl --location 'https://card.dazo.com/api/card' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6WyJBUFAiLCJVU0VSIl0sInN1YiI6Imh5cGVyYXRpdmEiLCJpYXQiOjE3NDMzNTU3NTgsImV4cCI6MTc0MzM1NzU1OH0.ClAeG8uib10g_6AoJD_pISagoKgoOoF3Sit6Glvp1CI' \
    --data '{
        "number": "4456897912999999"
    }'

Além disso, é possível consultar se um cartão esta na base de dados executando um get no endpoint /card/{number} Ex:

    curl --location 'https://card.dazo.com/api/card/4456897912999999' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6WyJBUFAiLCJVU0VSIl0sInN1YiI6Imh5cGVyYXRpdmEiLCJpYXQiOjE3NDMzNTg5NzUsImV4cCI6MTc0MzM2MDc3NX0.avphPcqNw3ooGaIuDCBh4AnvYijOsIkDxRjfWxfrQ-Y' \

Ainda nesse sistema é possível cadastrar um lote de cartões(FILE TXT) para serem processados no batch executando
um post no endpoint file informando um arquivo no form data Ex: 

curl --location 'https://card.dazo.com/api/file' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6WyJBUFAiLCJVU0VSIl0sInN1YiI6Imh5cGVyYXRpdmEiLCJpYXQiOjE3NDMzNTU3NTgsImV4cCI6MTc0MzM1NzU1OH0.ClAeG8uib10g_6AoJD_pISagoKgoOoF3Sit6Glvp1CI' \
    --form 'file=@"/tmp/DESAFIO-HYPERATIVA.txt"'

Para utilizar o sistema basta utilizar o postman conforme collection e environment que deixei na raiz do projeto.
Na collection de authenticação note que no menu tests do postman foi configurado para setar o token em uma variavel
de ambiente, assim sendo, basta executar uma chamada de sucesso no endpoint do sistema de authorizacao e após isso 
ja executar os endpoints do sistema card. Lembre se de setar o envinroment fornecido.

Notar que o usuário davison não tera acesso ao endpoint /file devido não ter a permissão de applicações (APP) já
o user hyperativa tem a permissão e por isso poderá utilizar o endpoint, isso foi pensado para ser assim. 

Além disso, toda requisição é logada conforme requisito.

### Detalhes sobre o sistema batch:

Este projeto tem como objetivo processar os lotes de cartões que chegaram via api do sistema card. Basicamente 
ele foi configurado para executar a cada 15 minutos e a cada execução recuperará os arquivos PENDENTES e para 
cada linha do arquivo txt encontrará o número do cartão do lote e validará se ele já existe na base, caso não
exista irá inserir na base, caso existe gravará num arquivo de erro para ser possível identificação de erros.
Se não houver erros gravará na tabela de files que o processamento finalizou com sucesso "COMPLETED" caso 
haja erros gravrá que finalizou com erros "ERROR".

O projeto foi criado utilizando spring boot 3.4 e sprig batch utilizando tasklet, está conectando no mysql, está 
utilizando o liquibase para criar tabelas e inserir registros, docker e maven para build, foi aplicado testes 
unitários que estão na pastas /test, foi aplicado testes de integração utilizando h2 que estão na pasta integration-test.
