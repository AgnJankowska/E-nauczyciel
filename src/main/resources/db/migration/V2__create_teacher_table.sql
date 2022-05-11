CREATE table users(
id                  int primary key auto_increment,
login               varchar(100) not null,
password            varchar(100) not null,
role                varchar(100) not null
);


