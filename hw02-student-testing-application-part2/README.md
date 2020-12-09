[![Build Status](https://travis-ci.org/andreyzhegalov/2020-11-otus-spring-zhegalov.svg?branch=feature%2Fhw02-student-testing-application-part2)](https://travis-ci.org/andreyzhegalov/2020-11-otus-spring-zhegalov)
[![codecov](https://codecov.io/gh/andreyzhegalov/2020-11-otus-spring-zhegalov/branch/feature/hw02-student-testing-application-part2/graph/badge.svg?token=s9BbEd1xif)](https://codecov.io/gh/andreyzhegalov/2020-11-otus-spring-zhegalov)

# Приложение по проведению тестирования студентов (с самим тестированием)

## Описание

####  Цель

Конфигурировать Spring-приложения современным способом, как это и делается в современном мире.

#### Результат

Готовое современное приложение на чистом Spring.

#### Описание задание

Программа должна спросить у пользователя фамилию и имя, спросить 5 вопросов из CSV-файла и вывести результат тестирования.

Выполняется на основе предыдущего домашнего задания + , собственно, сам функционал тестирования.

#### Требования

1. Переписать конфигурацию в виде Java + Annotation-based конфигурации.
2. Добавить функционал тестирования студента.
3. Добавьте файл настроек для приложения тестирования студентов.
4. В конфигурационный файл можно поместить путь до CSV-файла, количество правильных ответов для зачёта - на Ваше усмотрение.
5. Если Вы пишите интеграционные тесты, то не забудьте добавить аналогичный файл и для тестов.
6. Scanner, PrintStream и другие стандартные типы в контекст класть не нужно!
7. Ввод-вывод на английском языке.
8. Помним, "без фанатизма" :)

### Использование

Для запуска приложения необходимо:
1. Собрать приложение
````
mvn clean package
````
2. перейти в папку подпроекта hw02-student-testing-application-part2/target
3. запустить приложение
````
java -jar hw02-student-testing-application-part2-1.0-jar-with-dependencies.jar
````

