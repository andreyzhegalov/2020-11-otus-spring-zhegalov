[![Build Status](https://travis-ci.org/andreyzhegalov/2020-11-otus-spring-zhegalov.svg?branch=feature%2Fhw04-student-testing-application-part4)](https://travis-ci.org/andreyzhegalov/2020-11-otus-spring-zhegalov)
[![codecov](https://codecov.io/gh/andreyzhegalov/2020-11-otus-spring-zhegalov/branch/feature/hw04-student-testing-application-part4/graph/badge.svg?token=s9BbEd1xif)](https://codecov.io/gh/andreyzhegalov/2020-11-otus-spring-zhegalov)

# Приложение для тестирования студентов на Spring Boot

## Описание

#### Цель
Перевести приложение для проведения опросов на Spring Shell. После выполнения ДЗ вы сможете использовать Spring Shell, чтобы писать интерфейс приложения без Web.

#### Результат

Приложение на Spring Shell

#### Описание задание

Программа должна спросить у пользователя фамилию и имя, спросить 5 вопросов из CSV-файла и вывести результат тестирования.
Выполняется на основе предыдущего домашнего задания + , собственно, сам функционал тестирования.
Набор команд зависит только от Вашего желания. Вы можете сделать одну команду, запускающую Ваш Main, а можете построить полноценный интерфейс на Spring Shell.
Локализовывать команды Spring Shell НЕ НУЖНО (хотя можно, но это долго и непросто).

#### Требования

1. Подключить Spring Shell, используя spring-starter.
2. Написать набор команд, позволяющий проводить опрос.
3. Написать Unit-тесты с помощью spring-boot-starter-test, учесть, что Spring Shell в тестах нужно отключить.


### Использование

Для запуска приложения необходимо:
1. Собрать приложение
````
mvn clean package
````
2. перейти в папку подпроекта hw04-student-testing-application-part4/target
3. запустить приложение
````
java -jar hw04-student-testing-application-part4-1.0.jar
````
