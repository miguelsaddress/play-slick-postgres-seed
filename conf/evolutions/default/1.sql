# --- !Ups

create table users (
  "id" BIGSERIAL NOT NULL PRIMARY KEY,
  "name" VARCHAR NOT NULL,
  "username" VARCHAR NOT NULL UNIQUE,
  "email" VARCHAR NOT NULL UNIQUE,
  "password" VARCHAR NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS "users";
