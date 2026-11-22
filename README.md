# instruções gerais de como utilizar o mãos á obra

 1. Baixe o projeto em zip ou clone o repositorio.

 2. Abra a o projeto no intelij.

 3. Crie o banco NO MYSQL a partir do código que está em bd.sql
4. A partir do banco onde o bd foi criado, confirme se as informações do application.proprieties estão corretas.
 ex:
   spring.application.name=nome da aplicação 

spring.datasource.url=jdbc:mysql://localhost:porta de acesso/nome do banco
spring.datasource.username=nome do usuario

spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.servlet.multipart.max-file-size=15MB
spring.servlet.multipart.max-request-size=15MB

spring.thymeleaf.cache=false

# Pré requisitos

JDK 21

Intelij

MYSql