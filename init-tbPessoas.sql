CREATE TABLE tbPessoas (
    uuid        varchar(36) NOT NULL,
    apelido     varchar(32) NOT NULL UNIQUE,
    nome        varchar(255) NOT NULL,
    nascimento  varchar(10) NOT NULL,
    stack       varchar(1024),
    termo       varchar(1512)
);