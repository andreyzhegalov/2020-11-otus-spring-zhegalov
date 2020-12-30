# Приложение хранящее информацию о книгах в библиотеке на базе JPA+Hibernate

## Описание

#### Цель

Полноценно работать с JPA + Hibernate для подключения к реляционным БД посредством ORM-фреймворка.

#### Результат

Высокоуровневое приложение с JPA-маппингом сущностей.

#### Описание задание

Домашнее задание выполняется переписыванием предыдущего на JPA.

#### Требования

1. Использовать JPA, Hibernate только в качестве JPA-провайдера.
2. Для решения проблемы N+1 можно использовать специфические для Hibernate аннотации @Fetch и @BatchSize.
3. Добавить сущность "комментария к книге", реализовать CRUD для новой сущности.
4. Покрыть репозитории тестами, используя H2 базу данных и соответствующий H2 Hibernate-диалект для тестов.
5. Не забудьте отключить DDL через Hibernate
6. @Transactional рекомендуется ставить только на методы сервиса.

### Использование

Для запуска приложения необходимо:
1. Собрать приложение
````
mvn clean package
````
2. перейти в папку подпроекта hw06-library-service-part2/target
3. запустить приложение
````
java -jar hw06-library-service-part2-1.0.jar
````
