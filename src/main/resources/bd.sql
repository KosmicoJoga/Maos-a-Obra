DROP DATABASE IF EXISTS MAOSAOBRA;
CREATE DATABASE MAOSAOBRA;
USE MAOSAOBRA;

CREATE TABLE pessoa(
	id_pessoa INT PRIMARY KEY auto_increment,
	nome VARCHAR(100) NOT NULL,
	idade INT NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	senha VARCHAR(255) NOT NULL,
	avaliacao DECIMAL(3,2) UNSIGNED DEFAULT 0.00
);

CREATE TABLE servico(
    id_servico INT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(100) NOT NULL
);

INSERT INTO servico VALUES (null, "Eletricista"), (null, "Faxineiro"), (null, "Encanador");
-- colocar o resto dos tipos de trabalhadores no insert acima!

CREATE TABLE trabalhador(
	id_pessoa INT PRIMARY KEY,
	id_servico INT,
	certificados MEDIUMBLOB,
	foreign key(id_pessoa) REFERENCES pessoa(id_pessoa) ON DELETE CASCADE,
	foreign key(id_servico) REFERENCES servico(id_servico)
);

CREATE TABLE cliente(
	id_pessoa INT PRIMARY KEY,
	foreign key(id_pessoa) REFERENCES pessoa(id_pessoa) ON DELETE CASCADE
);

CREATE TABLE mei(
	id_mei INT PRIMARY KEY,
	cnpj CHAR(14) NOT NULL UNIQUE,
	foreign key(id_mei) REFERENCES trabalhador(id_pessoa) ON DELETE CASCADE
);

CREATE TABLE obra(
	id_obra INT PRIMARY KEY auto_increment,
	id_cliente INT NOT NULL,
	id_trabalhador INT,
    id_servico INT,
    descricao VARCHAR(255) NOT NULL,
	endereco VARCHAR(255) NOT NULL,
	orcamento DECIMAL(10, 2),
	foreign key(id_cliente) REFERENCES cliente(id_pessoa) ON DELETE CASCADE,
	foreign key(id_trabalhador) REFERENCES trabalhador(id_pessoa),
	foreign key(id_servico) REFERENCES servico(id_servico)
);

CREATE TABLE avaliacao(
	id_avaliacao INT PRIMARY KEY auto_increment,
    id_cliente INT NOT NULL, 
    id_trabalhador INT NOT NULL,
    nota INT CHECK (nota BETWEEN 1 AND 5),
    descricao VARCHAR(255),
    data_avaliacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    foreign key(id_cliente) REFERENCES cliente(id_pessoa) ON DELETE CASCADE,
	foreign key(id_trabalhador) REFERENCES trabalhador(id_pessoa) ON DELETE CASCADE
);

CREATE TABLE contratacao(
    id_contratacao INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT,
    id_trabalhador INT,
    id_obra INT,
    status ENUM(
        'PENDENTE',
        'ACEITA',
        'RECUSADA',
        'CONCLUIDA'
    ),
    data_solicitacao DATETIME DEFAULT CURRENT_TIMESTAMP,
	foreign key(id_cliente) REFERENCES cliente(id_pessoa),
	foreign key(id_trabalhador) REFERENCES trabalhador(id_pessoa),
	foreign key(id_obra) REFERENCES obra(id_obra)
);