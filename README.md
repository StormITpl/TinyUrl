[<ins>English</ins>](README.md) - [Polish](README.pl.md)

# TinyUrl Application
TinyUrl – application, which converts the full, long URL into a shortened form.

## Table of Contents
* [General Information](#general-information)
* [Technologies Used](#technologies-used)
* [Screenshots](#screenshots)
* [Prerequisites](#prerequisites)
* [Setup](#setup)
* [Authors](#authors)

## General Information
The project is aimed at practical learning, implementing the knowledge gained in Tomasz Woliński's courses - JOP (Java from scratch) and EAI (Efficient Internet Applications).
The application being developed is used to convert a long URL given by the user into a shortened form (modelled on platforms such as Bitly.com, Rebrandly.com, TinyURL.com, among others).
The application is based on the REST architecture and the Minimum Viable Product (MVP) model.

## Technologies Used
### Development
- [Java 18](https://openjdk.org/projects/jdk/18/)
- [Spring Boot 2](https://spring.io/projects/spring-boot)
- [Spring Data](https://spring.io/projects/spring-data)
- [PostgreSQL (docker)](https://www.postgresql.org/)
- [Maven 3.x](https://maven.apache.org/)
- [Git](https://git-scm.com/)


### Test
- [JUnit5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)

## Screenshots
![Test redirect](./images/redirect.png)

## Prerequisites
The following tools are required to start the application:

- [IntelliJ IDEA](https://www.jetbrains.com/idea/),
- [Java 18](https://openjdk.org/projects/jdk/18/)
- [Maven 3.x](https://maven.apache.org/download.cgi),
- [Docker](https://docs.docker.com/get-docker/)

## Setup

To run this project, please clone this repository and create a local copy on your computer.

After download project configurate your database and db server in few step:

- Create database connection with Docker pasting in command line:

docker run --name postgrestinyurl -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres

- Connect with server:

Login: postgres

Password: password

- Create database in server:

create database db_tinyurl;

Create table:

create table url(

id serial PRIMARY KEY ,

longURL VARCHAR(255) NOT NULL ,

shortURL VARCHAR(16) NOT NULL ,

creationDate DATE NOT NULL DEFAULT CURRENT_DATE
);

## Authors
Created by StormIT community: 
- bartek.karp93@gmail.com
- robertojavadev@gmail.com