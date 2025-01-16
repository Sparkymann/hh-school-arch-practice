# hh-school-arch-practice
Практическая часть лекции "Архитектура приложений и межсервисное взаимодействие"

## Микросервисы

### user-service
Отвечает за создание и получение информации о пользователях

### package-service
Отвечает за отправку посылок и их валидацию

## Запуск

```bash
./run.sh
```

## Использование

### Создать пользователя
```shell
curl -X POST localhost:8081/user -H 'Content-Type: application/json' -d '{"name": "Ilya"}'
```

### Получить информацию о пользователе
```shell
curl localhost:8081/user/1
```

### Отправить посылку
```shell
curl -X POST localhost:8082/package -H 'Content-Type: application/json' -d '{"sender": "Ilya", "receiver": "Ilya"}'
```


### Получить информацию о посылке
```shell
curl localhost:8082/package/1
```

## Consul

### Получить информацию о сервисе
```shell
curl localhost:8500/v1/catalog/service/user-service | jq
```

