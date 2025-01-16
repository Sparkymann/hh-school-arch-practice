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

## Использование

### Создать пользователя
```shell
curl -X POST localhost:8080/user -H 'Content-Type: application/json' -d '{"name": "Ilya"}'
```

### Получить информацию о пользователе
```shell
curl localhost:8080/user/1
```

### Отправить посылку
```shell
curl -X POST localhost:8081/package -H 'Content-Type: application/json' -d '{"sender": "Ilya", "receiver": "Ilya"}'
```


### Получить информацию о посылке
```shell
curl localhost:8081/package/1
```

