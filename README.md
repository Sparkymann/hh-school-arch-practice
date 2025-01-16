# hh-school-arch-practice
Практическая часть лекции "Архитектура приложений и межсервисное взаимодействие"

## Микросервисы

### user-service
Отвечает за создание и получение информации о пользователях

#### Запуск

```bash
./run.sh user-service 8080
```

### package-service
Отвечает за отправку посылок и их валидацию

#### Запуск

```bash
./run.sh package-service 8081
```
