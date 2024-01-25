# clever_backend_task_13_hibernate

## Задача

1. Создать Web приложение учёта домов и жильцов
2. 2 сущности: House, Person
3. Система должна предоставлять REST API для выполнения следующих операций:

- CRUD для House
    - В GET запросах не выводить информацию о Person
- CRUD для Person
    - В GET запросах не выводить информацию о House
- Для GET операций использовать pagination (default size: 15)

### House:

1. У House обязаны быть поля id, uuid, area, country, city, street, number, create_date
2. House может иметь множество жильцов (0-n)
3. У House может быть множество владельцев (0-n)
4. create_date устанавливается один раз при создании

### Person:

1. У Person обязаны быть id, uuid, name, surname, sex, passport_series, passport_number, create_date, update_date
2. Person обязан жить только в одном доме и не может быть бездомным
3. Person не обязан владеть хоть одним домом и может владеть множеством домов
4. Сочетание passport_series и passport_number уникально
5. sex должен быть [Male, Female]
6. Все связи обеспечить через id
7. Не возвращать id пользователям сервисов, для этого предназначено поле uuid
8. create_date устанавливается один раз при создании
9. update_date устанавливается при создании и изменяется каждый раз, когда меняется информация о Person. При этом, если
   запрос не изменяет информации, поле не должно обновиться

### Примечание:

1. Ограничения и нормализацию сделать на своё усмотрение
2. Поля представлены для хранения в базе данных. В коде могут отличаться

### Обязательно:

1. GET для всех Person проживающих в House
2. GET для всех House, владельцем которых является Person
3. Конфигурационный файл: application.yml
4. Скрипты для создания таблиц должны лежать в classpath:db/
5. create_date, update_date - возвращать в формате ISO-8601 (https://en.wikipedia.org/wiki/ISO_8601). Пример:
   2018-08-29T06:12:15.156.
6. Добавить 5 домов и 10 жильцов. Один дом без жильцов и как минимум в 1 доме больше 1 владельца
7. Использовать репозиторий с JDBC Template для одного метода.

### Дополнительно:

1. *Добавить миграцию
2. *Полнотекстовый поиск (любое текстовое поле) для House
3. *Полнотекстовый поиск (любое текстовое поле) для Person
5. **PATCH для Person и House

### Самостоятельно:

1. Изучить отношения: OneToOne, OneToMany, ManyToOne, ManyToMany;
2. !!! Изучить Hibernate Persistence Context;
3. !!! Изучить кеширование в hibernate;

### Требования к приложению

1. Версия JDK: 17 – используйте Streams, java.time.* и т. д., где это возможно.
2. Корень пакетов приложений: ru.clevertec.house.
3. Можно использовать любой широко используемый пул соединений.
4. Для доступа к данным следует использовать шаблон Spring JDBC.
5. Используйте транзакции там, где это необходимо.
6. Соглашение Java Code является обязательным (исключение: размер поля – 120 символов).
7. Инструмент сборки: Gradle, последняя версия.
8. Веб-сервер: Apache Tomcat.
9. Контейнер приложения: Spring IoC. Spring Framework, последняя версия.
10. База данных: PostgreSQL, последняя версия.
11. Тестирование: JUnit 5.+, Mockito.
12. Сервисный уровень должен быть покрыт юнит-тестами не менее чем на 80%.
13. Уровень репозитория следует тестировать с помощью интеграционных тестов со встроенной в память базой данных или
    тестовыми контейнерами.
14. В качестве картографа используйте Mapstruct.
16. Используйте ломбок.

### Общие требования

1. Код должен быть чистым и не должен содержать никаких конструкций, предназначенных для разработчиков.
2. Приложение должно быть спроектировано и написано с учетом принципов OOD и SOLID.
3. Код должен содержать ценные комментарии, где это необходимо.
4. Публичные API должны быть задокументированы (Javadoc).
5. Следует использовать четкую многоуровневую структуру с определением обязанностей каждого прикладного уровня.
6. JSON следует использовать в качестве формата сообщений связи клиент-сервер.
7. Должен быть реализован удобный механизм обработки ошибок/исключений: все ошибки должны быть значимыми на серверной
   стороне.
   Пример: обработка ошибки 404:
   HTTP Status: 404
   response body

```http
{
 “errorMessage”: “Requested resource not found (uuid = f4fe3df1-22cd-49ce-a54d-86f55a7f372e)”,
 “errorCode”: 40401
 }

```

где “errorCode” — ваш собственный код (он может основываться на статусе http и запрошенном ресурсе — человеке или доме)

9. Абстракцию следует использовать везде, чтобы избежать дублирования кода.
10. Должно быть реализовано несколько конфигураций (минимум две — dev и prod).

### Ограничения приложения

Запрещено использовать:

1. Spring Boot.
2. Spring Data Repositories.
3. Spring Data JPA.

# Реализация

## CRUD операции:

### HouseController

#### GET getAll(int offset, int limit):

Default offset = 0
Default limit = 15

Request:

```http request
http://localhost:8080/api/houses
```

Response:

```json
[
  {
    "uuid": "6d316b83-126e-4090-bc81-4125a68923c0",
    "area": "AreaOne",
    "country": "Country",
    "city": "CityOne",
    "street": "StreetOne",
    "number": 19,
    "createDate": "2024-01-19T13:16:13.992"
  },
  {
    "uuid": "71d9d979-48d9-420c-9454-912225476fef",
    "area": "AreaTwo",
    "country": "Country",
    "city": "City",
    "street": "StreetTwo",
    "number": 56,
    "createDate": "2024-01-19T13:17:12.830"
  },
  {
    "uuid": "f291c4bb-58a0-4800-bfc8-104217051d24",
    "area": "AreaThree",
    "country": "Country",
    "city": "CityTwo",
    "street": "StreetThree",
    "number": 43,
    "createDate": "2024-01-19T13:17:47.442"
  },
  {
    "uuid": "7c204d2c-b694-4dc4-a73a-9fa99b02973f",
    "area": "AreaTwo",
    "country": "Country",
    "city": "City",
    "street": "StreetOne",
    "number": 98,
    "createDate": "2024-01-19T13:18:17.079"
  },
  {
    "uuid": "b6baf98c-ea5a-48ec-a4e5-645998c1cceb",
    "area": "AreaOne",
    "country": "Country",
    "city": "City",
    "street": "StreetOne",
    "number": 13,
    "createDate": "2024-01-19T13:18:39.911"
  }
]
```

Request:

```http request
http://localhost:8080/api/houses?offset=1&limit=2
```

Response:

```json
[
  {
    "uuid": "71d9d979-48d9-420c-9454-912225476fef",
    "area": "AreaTwo",
    "country": "Country",
    "city": "City",
    "street": "StreetTwo",
    "number": 56,
    "createDate": "2024-01-19T13:17:12.830"
  },
  {
    "uuid": "f291c4bb-58a0-4800-bfc8-104217051d24",
    "area": "AreaThree",
    "country": "Country",
    "city": "CityTwo",
    "street": "StreetThree",
    "number": 43,
    "createDate": "2024-01-19T13:17:47.442"
  }
]
```

#### GET getByUuid(UUID uuid):

Request:

```http request
http://localhost:8080/api/houses/f291c4bb-58a0-4800-bfc8-104217051d24
```

Response:

```json
{
  "uuid": "f291c4bb-58a0-4800-bfc8-104217051d24",
  "area": "AreaThree",
  "country": "Country",
  "city": "CityTwo",
  "street": "StreetThree",
  "number": 43,
  "createDate": "2024-01-19T13:17:47.442"
}
```

#### GET getAllResidents(UUID uuid):

GET для всех Person проживающих в House

Request:

```http request
http://localhost:8080/api/houses/residents/f291c4bb-58a0-4800-bfc8-104217051d24
```

Response:

```json
[
  {
    "uuid": "7736bed0-5b5c-4fbc-a919-e56f947a59b7",
    "name": "Liam",
    "surname": "Davis",
    "sex": "MALE",
    "passport": {
      "passportSeries": "GH",
      "passportNumber": "7654321"
    },
    "createDate": "2024-01-19T13:26:03.433",
    "updateDate": "2024-01-19T13:26:03.433"
  },
  {
    "uuid": "d21df8c3-2771-4dcb-9d4d-5f7a971b0c07",
    "name": "Emma",
    "surname": "Wilson",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "IJ",
      "passportNumber": "5432167"
    },
    "createDate": "2024-01-19T13:26:54.076",
    "updateDate": "2024-01-19T13:26:54.076"
  },
  {
    "uuid": "0953550a-4c67-4d3a-b95d-e7366d0c097c",
    "name": "Isabella",
    "surname": "Lee",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "QR",
      "passportNumber": "4321098"
    },
    "createDate": "2024-01-19T13:30:14.038",
    "updateDate": "2024-01-19T13:30:14.038"
  }
]
```

#### GET searchHouseByCity(String city):

Request:

```http request
http://localhost:8080/api/houses/search/wo
```

Response:

```json
[
  {
    "uuid": "f291c4bb-58a0-4800-bfc8-104217051d24",
    "area": "AreaThree",
    "country": "Country",
    "city": "CityTwo",
    "street": "StreetThree",
    "number": 43,
    "createDate": "2024-01-19T13:17:47.442"
  }
]
```

Empty list:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### POST create(HouseCreateDto houseCreateDto):

Request:

```http request
http://localhost:8080/api/houses
```

Body:

```json
{
  "area": "AreaOne",
  "country": "Country",
  "city": "City",
  "street": "StreetOne",
  "number": 35
}
```

Response:

```json
{
  "uuid": "ebd8b270-12ef-42f4-b8df-8944344623e3",
  "area": "AreaOne",
  "country": "Country",
  "city": "City",
  "street": "StreetOne",
  "number": 35,
  "createDate": "2024-01-26T00:51:48.720"
}
```

#### PUT update(HouseUpdateDto houseUpdateDto):

Request:

```http request
http://localhost:8080/api/houses
```

Body:

```json
{
  "uuid": "ebd8b270-12ef-42f4-b8df-8944344623e3",
  "area": "AreaTwo",
  "country": "Country",
  "city": "City",
  "street": "StreetOne",
  "number": 37
}
```

Response:

```json
{
  "uuid": "ebd8b270-12ef-42f4-b8df-8944344623e3",
  "area": "AreaTwo",
  "country": "Country",
  "city": "City",
  "street": "StreetOne",
  "number": 37,
  "createDate": "2024-01-26T00:51:48.720"
}
```

#### PATCH patch(HouseUpdateDto houseUpdateDto):

Request:

```http request
http://localhost:8080/api/houses
```

Body:

```json
{
  "uuid": "ebd8b270-12ef-42f4-b8df-8944344623e3",
  "area": "AreaOne"
}
```

Response:

```json
{
  "uuid": "ebd8b270-12ef-42f4-b8df-8944344623e3",
  "area": "AreaOne",
  "country": "Country",
  "city": "City",
  "street": "StreetOne",
  "number": 0,
  "createDate": "2024-01-26T00:51:48.720"
}
```

#### DELETE delete(UUID uuid):

Request:

```http request
http://localhost:8080/api/houses/0953550a-4c67-4d3a-b95d-e7366d0c097c
```

__

### PersonController

#### GET getAll(Integer offset, Integer limit):

Default offset = 0
Default limit = 15

Request:

```http request
http://localhost:8080/api/persons
```

Response:

```json
[
  {
    "uuid": "33447590-3f82-4ae7-a9f6-4b140475c18f",
    "name": "Sophia",
    "surname": "Johnson",
    "sex": "MALE",
    "passport": {
      "passportSeries": "AB",
      "passportNumber": "1234567"
    },
    "createDate": "2024-01-19T13:23:18.356",
    "updateDate": "2024-01-19T13:23:18.356"
  },
  {
    "uuid": "54b9906a-8747-4af7-b641-2a803849f2d1",
    "name": "Noah",
    "surname": "Smith",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "CD",
      "passportNumber": "9876543"
    },
    "createDate": "2024-01-19T13:24:19.361",
    "updateDate": "2024-01-19T13:24:19.361"
  },
  {
    "uuid": "6c19889a-bb5a-4125-b168-6a1b9c4dd418",
    "name": "Olivia",
    "surname": "Brown",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "EF",
      "passportNumber": "2468101"
    },
    "createDate": "2024-01-19T13:25:00.894",
    "updateDate": "2024-01-19T13:25:00.894"
  },
  {
    "uuid": "7736bed0-5b5c-4fbc-a919-e56f947a59b7",
    "name": "Liam",
    "surname": "Davis",
    "sex": "MALE",
    "passport": {
      "passportSeries": "GH",
      "passportNumber": "7654321"
    },
    "createDate": "2024-01-19T13:26:03.433",
    "updateDate": "2024-01-19T13:26:03.433"
  },
  {
    "uuid": "d21df8c3-2771-4dcb-9d4d-5f7a971b0c07",
    "name": "Emma",
    "surname": "Wilson",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "IJ",
      "passportNumber": "5432167"
    },
    "createDate": "2024-01-19T13:26:54.076",
    "updateDate": "2024-01-19T13:26:54.076"
  },
  {
    "uuid": "74c3452d-ec27-4cb7-9cd5-e177607bf75c",
    "name": "Jackson",
    "surname": "Martinez",
    "sex": "MALE",
    "passport": {
      "passportSeries": "KL",
      "passportNumber": "2109876"
    },
    "createDate": "2024-01-19T13:27:48.176",
    "updateDate": "2024-01-19T13:27:48.176"
  },
  {
    "uuid": "cd7c5f0f-337b-4db1-8bc0-794d0d98685d",
    "name": "Ava",
    "surname": "Anderson",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "MN",
      "passportNumber": "6789012"
    },
    "createDate": "2024-01-19T13:28:35.280",
    "updateDate": "2024-01-19T13:28:35.280"
  },
  {
    "uuid": "1e390f7a-2181-42cb-af3d-62fe2a263d08",
    "name": "Lucas",
    "surname": "Thompson",
    "sex": "MALE",
    "passport": {
      "passportSeries": "OP",
      "passportNumber": "0987654"
    },
    "createDate": "2024-01-19T13:29:24.912",
    "updateDate": "2024-01-19T13:29:24.912"
  },
  {
    "uuid": "0953550a-4c67-4d3a-b95d-e7366d0c097c",
    "name": "Isabella",
    "surname": "Lee",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "QR",
      "passportNumber": "4321098"
    },
    "createDate": "2024-01-19T13:30:14.038",
    "updateDate": "2024-01-19T13:30:14.038"
  },
  {
    "uuid": "f8cba101-e82b-4e43-8eeb-3c5fac1e5f45",
    "name": "Ethan",
    "surname": "Harris",
    "sex": "MALE",
    "passport": {
      "passportSeries": "ST",
      "passportNumber": "8765432"
    },
    "createDate": "2024-01-19T13:31:19.378",
    "updateDate": "2024-01-19T13:31:19.378"
  }
]
```

Request:

```http request
http://localhost:8080/api/persons?offset=1&limit=2
```

Response:

```json
[
  {
    "uuid": "54b9906a-8747-4af7-b641-2a803849f2d1",
    "name": "Noah",
    "surname": "Smith",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "CD",
      "passportNumber": "9876543"
    },
    "createDate": "2024-01-19T13:24:19.361",
    "updateDate": "2024-01-19T13:24:19.361"
  },
  {
    "uuid": "6c19889a-bb5a-4125-b168-6a1b9c4dd418",
    "name": "Olivia",
    "surname": "Brown",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "EF",
      "passportNumber": "2468101"
    },
    "createDate": "2024-01-19T13:25:00.894",
    "updateDate": "2024-01-19T13:25:00.894"
  }
]
```

#### GET getByUuid(UUID uuid):

Request:

```http request
http://localhost:8080/api/persons/54b9906a-8747-4af7-b641-2a803849f2d1
```

Response:

```json
{
  "uuid": "54b9906a-8747-4af7-b641-2a803849f2d1",
  "name": "Noah",
  "surname": "Smith",
  "sex": "FEMALE",
  "passport": {
    "passportSeries": "CD",
    "passportNumber": "9876543"
  },
  "createDate": "2024-01-19T13:24:19.361",
  "updateDate": "2024-01-19T13:24:19.361"
}
```

#### GET getAllHouses(UUID uuid):

GET для всех House, владельцем которых является Person

Request:

```http request
http://localhost:8080/api/persons/houses/cd7c5f0f-337b-4db1-8bc0-794d0d98685d
```

Response:

```json
[
  {
    "uuid": "71d9d979-48d9-420c-9454-912225476fef",
    "area": "AreaTwo",
    "country": "Country",
    "city": "City",
    "street": "StreetTwo",
    "number": 56,
    "createDate": "2024-01-19T13:17:12.830"
  },
  {
    "uuid": "7c204d2c-b694-4dc4-a73a-9fa99b02973f",
    "area": "AreaTwo",
    "country": "Country",
    "city": "City",
    "street": "StreetOne",
    "number": 98,
    "createDate": "2024-01-19T13:18:17.079"
  }
]
```

#### GET searchHouseBySurname(String surname):

Request:

```http request
http://localhost:8080/api/persons/search/s
```

Response:

```json
[
  {
    "uuid": "33447590-3f82-4ae7-a9f6-4b140475c18f",
    "name": "Sophia",
    "surname": "Johnson",
    "sex": "MALE",
    "passport": {
      "passportSeries": "AB",
      "passportNumber": "1234567"
    },
    "createDate": "2024-01-19T13:23:18.356",
    "updateDate": "2024-01-19T13:23:18.356"
  },
  {
    "uuid": "7736bed0-5b5c-4fbc-a919-e56f947a59b7",
    "name": "Liam",
    "surname": "Davis",
    "sex": "MALE",
    "passport": {
      "passportSeries": "GH",
      "passportNumber": "7654321"
    },
    "createDate": "2024-01-19T13:26:03.433",
    "updateDate": "2024-01-19T13:26:03.433"
  },
  {
    "uuid": "d21df8c3-2771-4dcb-9d4d-5f7a971b0c07",
    "name": "Emma",
    "surname": "Wilson",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "IJ",
      "passportNumber": "5432167"
    },
    "createDate": "2024-01-19T13:26:54.076",
    "updateDate": "2024-01-19T13:26:54.076"
  },
  {
    "uuid": "cd7c5f0f-337b-4db1-8bc0-794d0d98685d",
    "name": "Ava",
    "surname": "Anderson",
    "sex": "FEMALE",
    "passport": {
      "passportSeries": "MN",
      "passportNumber": "6789012"
    },
    "createDate": "2024-01-19T13:28:35.280",
    "updateDate": "2024-01-19T13:28:35.280"
  },
  {
    "uuid": "1e390f7a-2181-42cb-af3d-62fe2a263d08",
    "name": "Lucas",
    "surname": "Thompson",
    "sex": "MALE",
    "passport": {
      "passportSeries": "OP",
      "passportNumber": "0987654"
    },
    "createDate": "2024-01-19T13:29:24.912",
    "updateDate": "2024-01-19T13:29:24.912"
  },
  {
    "uuid": "f8cba101-e82b-4e43-8eeb-3c5fac1e5f45",
    "name": "Ethan",
    "surname": "Harris",
    "sex": "MALE",
    "passport": {
      "passportSeries": "ST",
      "passportNumber": "8765432"
    },
    "createDate": "2024-01-19T13:31:19.378",
    "updateDate": "2024-01-19T13:31:19.378"
  }
]
```

Empty list:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### POST create(HouseCreateDto houseCreateDto):

Request:

```http request
http://localhost:8080/api/persons
```

Body:

```json
{
  "name": "Noah",
  "surname": "Smith",
  "sex": "FEMALE",
  "passport": {
    "passportSeries": "CD",
    "passportNumber": "9650991"
  },
  "homeUuid": "6d316b83-126e-4090-bc81-4125a68923c0"
}
```

Response:

```json
{
  "uuid": "f3478035-aa97-4e10-89dc-f1edaed59726",
  "name": "Noah",
  "surname": "Smith",
  "sex": "FEMALE",
  "passport": {
    "passportSeries": "CD",
    "passportNumber": "9650991"
  },
  "createDate": "2024-01-26T00:55:26.071",
  "updateDate": "2024-01-26T00:55:26.071"
}
```

#### PUT update(PersonUpdateDto personUpdateDto):

Request:

```http request
http://localhost:8080/api/persons
```

Body:

```json
{
  "uuid": "06d562e4-4bfd-4897-88b7-5d00acf6ac26",
  "name": "Noa",
  "surname": "Smith",
  "sex": "MALE",
  "passport": {
    "passportSeries": "CD",
    "passportNumber": "9650991"
  },
  "homeUuid": "7c204d2c-b694-4dc4-a73a-9fa99b02973f"
}
```

Response:

```json
{
  "uuid": "f3478035-aa97-4e10-89dc-f1edaed59726",
  "name": "Noa",
  "surname": "Smit",
  "sex": "MALE",
  "passport": {
    "passportSeries": "CD",
    "passportNumber": "9650991"
  },
  "createDate": "2024-01-26T00:55:26.071",
  "updateDate": "2024-01-26T00:55:26.071"
}
```

#### PATCH patch(PersonUpdateDto personUpdateDto):

Request:

```http request
http://localhost:8080/api/persons
```

Body:

```json
{
  "uuid": "f3478035-aa97-4e10-89dc-f1edaed59726",
  "name": "NoaM",
  "surname": "SmitD"
}
```

Response:

```json
{
  "uuid": "f3478035-aa97-4e10-89dc-f1edaed59726",
  "name": "NoaM",
  "surname": "SmitD",
  "sex": "MALE",
  "passport": {
    "passportSeries": "CD",
    "passportNumber": "9650991"
  },
  "createDate": "2024-01-26T00:55:26.071",
  "updateDate": "2024-01-26T00:56:44.462"
}
```

#### DELETE delete(UUID uuid):

Request:

```http request
http://localhost:8080/api/persons/b6baf98c-ea5a-48ec-a4e5-645998c1cceb
```

__


