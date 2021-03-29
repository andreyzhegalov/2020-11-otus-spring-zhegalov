# Приложение с применением Spring Boot Actuator

## Описание

### Цель

Реализовать production-grade мониторинг и прозрачность в приложении.

#### Результат

Приложение с применением Spring Boot Actuator.

#### Требования

1. Подключить Spring Boot Actuator в приложение.
2. Включить метрики, healthchecks и logfile.
3. Реализовать свой собственный HealthCheck индикатор.
4. UI для данных от Spring Boot Actuator реализовывать не нужно.
5. *Опционально:* переписать приложение на HATEOAS принципах с помощью Spring Data REST Repository

#### Описание решения

Список актуаторов доступен по URI `/actuator`.
Добавлен пользовательский актуатор `/actuator/library`.
Добавлен пользовательский healthcheck `/actuator/health/library`.
Добавлены интеграционные тесты для тестирования web-сервера `actuator/IntegratedActuatorTest.java`. По умолчанию отключены.
Data REST repository доступен по базовому URI `/datarest`.
