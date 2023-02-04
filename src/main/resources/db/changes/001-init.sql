--liquibase formatted sql

--changeset RobertoJavaDev:001_1

create table if not exists urls
(
    id              uuid not null primary key,
    long_url        varchar(255) not null,
    short_url       varchar(255) not null,
    creation_date   timestamp not null
);

create table if not exists url_analytics
(
    id              uuid not null primary key,
    total_clicks    bigint not null,
    localization    varchar(255),
    click_date      timestamp not null
);

create table if not exists url_expiry
(
    id              uuid not null primary key,
    creation_date   timestamp not null,
    expiration_date timestamp,
    is_premium      bit not null
);

--changeset RobertoJavaDev:001_2

insert into urls (id, long_url, short_url, creation_date) values
    (gen_random_uuid(), 'https://stormit.pl', 'def456', current_timestamp),
    (gen_random_uuid(), 'https://www.google.pl', 'abc123', current_timestamp);
