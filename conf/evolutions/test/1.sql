# --- !Ups

create table "people" (
  "id" BIGSERIAL NOT NULL PRIMARY KEY,
  "name" VARCHAR NOT NULL,
  "age" INT NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS "people";
