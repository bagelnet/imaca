# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table image (
  id                        bigint not null,
  tag                       varchar(255) not null,
  image_name                varchar(255) not null,
  constraint uq_image_1 unique (image_name),
  constraint pk_image primary key (id))
;

create table request (
  id                        bigint not null,
  request_key               varchar(255) not null,
  image_id                  bigint not null,
  constraint uq_request_1 unique (request_key),
  constraint pk_request primary key (id))
;

create sequence image_seq;

create sequence request_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists image;

drop table if exists request;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists image_seq;

drop sequence if exists request_seq;

