# Instruções gerais de como executar o Mãos à Obra

1. Baixe o projeto em zip ou clone o repositorio.

2. Abra o projeto no IntelliJ IDEA.

3. Crie o banco no MYSQL a partir do bd.sql que está localizado [aqui](src/main/resources/bd.sql) (src/main/resources/bd.sql)

4. A partir do banco criado pelo bd.sql no MYSQL, confirme se as informações no [application.proprieties](src/main/resources/application.properties) estão coerentes com as informações do servidor onde o banco foi criado.


**EXEMPLO**:

   
spring.application.name = nome da aplicação (é para ser "maosaobra") 

spring.datasource.url = jdbc:mysql://localhost: *porta de acesso/nome do banco* (normalmente sendo "3306/MAOSAOBRA")

spring.datasource.username = nome do usuário (geralmente sendo "root" por padrão)

spring.datasource.password = senha (geralmente sendo "root" por padrão, mas se você usa senha diferente no MYSQL, usa a sua senha!)

5. Abra o projeto no IntelliJ e espere ele carregar o projeto todo antes de executar. Após carregar tudo, execute o arquivo [MaosaobraApplication](src/main/java/org/example/maosaobra/MaosaobraApplication.java) no IntelliJ.
6. Quando o IntelliJ terminar de executar o projeto, inicie o seu navegador de preferência e abra uma nova página com o endereço URL "localhost:8080" e pronto, poderá usufruir do projeto MÃOS À OBRA!


# Pré-requisitos

- JDK 21

- IntelliJ IDEA

- MYSQL