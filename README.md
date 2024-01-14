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

### PersonController

GET-запрос по uuid:

```http request
 http://localhost:8080/person?uuid=e3ca72c4-cc61-49ca-acd0-8e8c315abaf9
```

Результат (code 200):

```json
{
  "uuid": "e3ca72c4-cc61-49ca-acd0-8e8c315abaf9",
  "name": "name1",
  "surname": "surname1",
  "sex": "MALE",
  "passportSeries": "HM",
  "passportNumber": "4334564",
  "createDate": [
    2024,
    1,
    14,
    21,
    17,
    11,
    798718000
  ],
  "updateDate": [
    2024,
    1,
    14,
    21,
    17,
    11,
    798718000
  ]
}
```

Person не найден (code 404):

```text
Person not found
```

___
GET-запрос по page и count:

```http request
 http://localhost:8080/person?page=2&count=3
```

Результат (code 200):

```json
[
  {
    "uuid": "b74c342b-6151-4294-b7bb-d45eee9db547",
    "name": "name4",
    "surname": "surname4",
    "sex": "MALE",
    "passportSeries": "HM",
    "passportNumber": "6789054",
    "createDate": [
      2024,
      1,
      14,
      21,
      19,
      52,
      344881000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      19,
      52,
      344881000
    ]
  },
  {
    "uuid": "df4d28cb-e006-43a9-9a23-e4bb680e6248",
    "name": "name5",
    "surname": "surname5",
    "sex": "FEMALE",
    "passportSeries": "HJ",
    "passportNumber": "6547895",
    "createDate": [
      2024,
      1,
      14,
      21,
      20,
      58,
      699287000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      20,
      58,
      699287000
    ]
  },
  {
    "uuid": "10351a9b-76c3-475c-848a-ff68ff8160fe",
    "name": "name6",
    "surname": "surname6",
    "sex": "MALE",
    "passportSeries": "HM",
    "passportNumber": "6784501",
    "createDate": [
      2024,
      1,
      14,
      21,
      21,
      30,
      452633000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      21,
      30,
      452633000
    ]
  }
]
```

Ошибка выполнения (code 500):

```text
Error retrieving persons list
```

___
GET-запрос по page:

```http request
 http://localhost:8080/person?page=1
```

Результат (code 200):

```json
[
  {
    "uuid": "e3ca72c4-cc61-49ca-acd0-8e8c315abaf9",
    "name": "name1",
    "surname": "surname1",
    "sex": "MALE",
    "passportSeries": "HM",
    "passportNumber": "4334564",
    "createDate": [
      2024,
      1,
      14,
      21,
      17,
      11,
      798718000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      17,
      11,
      798718000
    ]
  },
  {
    "uuid": "fc0aee80-d3c0-45b2-87ab-e337a8d2e81d",
    "name": "name2",
    "surname": "surname2",
    "sex": "MALE",
    "passportSeries": "HM",
    "passportNumber": "8764963",
    "createDate": [
      2024,
      1,
      14,
      21,
      18,
      37,
      506266000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      18,
      37,
      506266000
    ]
  },
  {
    "uuid": "68b43535-6d0c-4439-9055-bbc83dc2448b",
    "name": "name3",
    "surname": "surname3",
    "sex": "FEMALE",
    "passportSeries": "HM",
    "passportNumber": "6542317",
    "createDate": [
      2024,
      1,
      14,
      21,
      19,
      14,
      717783000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      19,
      14,
      717783000
    ]
  },
  {
    "uuid": "b74c342b-6151-4294-b7bb-d45eee9db547",
    "name": "name4",
    "surname": "surname4",
    "sex": "MALE",
    "passportSeries": "HM",
    "passportNumber": "6789054",
    "createDate": [
      2024,
      1,
      14,
      21,
      19,
      52,
      344881000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      19,
      52,
      344881000
    ]
  },
  {
    "uuid": "df4d28cb-e006-43a9-9a23-e4bb680e6248",
    "name": "name5",
    "surname": "surname5",
    "sex": "FEMALE",
    "passportSeries": "HJ",
    "passportNumber": "6547895",
    "createDate": [
      2024,
      1,
      14,
      21,
      20,
      58,
      699287000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      20,
      58,
      699287000
    ]
  },
  {
    "uuid": "10351a9b-76c3-475c-848a-ff68ff8160fe",
    "name": "name6",
    "surname": "surname6",
    "sex": "MALE",
    "passportSeries": "HM",
    "passportNumber": "6784501",
    "createDate": [
      2024,
      1,
      14,
      21,
      21,
      30,
      452633000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      21,
      30,
      452633000
    ]
  },
  {
    "uuid": "38f413e0-d05a-4d7a-bbb3-8cbaf5de98ed",
    "name": "name7",
    "surname": "surname7",
    "sex": "FEMALE",
    "passportSeries": "HM",
    "passportNumber": "8796540",
    "createDate": [
      2024,
      1,
      14,
      21,
      21,
      57,
      200516000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      21,
      57,
      200516000
    ]
  },
  {
    "uuid": "3c07f047-43a7-4dd3-939e-ef134a7f711c",
    "name": "name8",
    "surname": "surname8",
    "sex": "MALE",
    "passportSeries": "HM",
    "passportNumber": "5546722",
    "createDate": [
      2024,
      1,
      14,
      21,
      22,
      22,
      611060000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      22,
      22,
      611060000
    ]
  },
  {
    "uuid": "4f7e3ae6-d5c7-4337-8cbb-edd65eed01b2",
    "name": "name9",
    "surname": "surname9",
    "sex": "MALE",
    "passportSeries": "HM",
    "passportNumber": "0098876",
    "createDate": [
      2024,
      1,
      14,
      21,
      22,
      55,
      446021000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      22,
      55,
      446021000
    ]
  },
  {
    "uuid": "318d2a12-1ecd-403e-b040-a1db8d81ed78",
    "name": "name10",
    "surname": "surname10",
    "sex": "FEMALE",
    "passportSeries": "HM",
    "passportNumber": "1123444",
    "createDate": [
      2024,
      1,
      14,
      21,
      23,
      26,
      997515000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      23,
      26,
      997515000
    ]
  }
]
```

Ошибка выполнения (code 500):

```text
Error retrieving persons list
```

___
GET-запрос по count:

```http request
 http://localhost:8080/person?count=3
```

Результат (code 200):

```json
[
  {
    "uuid": "e3ca72c4-cc61-49ca-acd0-8e8c315abaf9",
    "name": "name1",
    "surname": "surname1",
    "sex": "MALE",
    "passportSeries": "HM",
    "passportNumber": "4334564",
    "createDate": [
      2024,
      1,
      14,
      21,
      17,
      11,
      798718000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      17,
      11,
      798718000
    ]
  },
  {
    "uuid": "fc0aee80-d3c0-45b2-87ab-e337a8d2e81d",
    "name": "name2",
    "surname": "surname2",
    "sex": "MALE",
    "passportSeries": "HM",
    "passportNumber": "8764963",
    "createDate": [
      2024,
      1,
      14,
      21,
      18,
      37,
      506266000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      18,
      37,
      506266000
    ]
  },
  {
    "uuid": "68b43535-6d0c-4439-9055-bbc83dc2448b",
    "name": "name3",
    "surname": "surname3",
    "sex": "FEMALE",
    "passportSeries": "HM",
    "passportNumber": "6542317",
    "createDate": [
      2024,
      1,
      14,
      21,
      19,
      14,
      717783000
    ],
    "updateDate": [
      2024,
      1,
      14,
      21,
      19,
      14,
      717783000
    ]
  }
]
```

Ошибка выполнения (code 500):

```text
Error retrieving persons list
```

Неверный запрос (code 400):

```text
Invalid request parameters
```

___
POST-запрос:

```http request
  http://localhost:8080/person
```

Body:

```json
{
  "name": "name11",
  "surname": "surname11",
  "sex": "FEMALE",
  "passportSeries": "HM",
  "passportNumber": "4994560"
}
```

Результат (code 200):

```json
{
  "uuid": "aaef44fa-d2da-46c9-ab2f-3705d5dec36e",
  "name": "name11",
  "surname": "surname11",
  "sex": "FEMALE",
  "passportSeries": "HM",
  "passportNumber": "4994560",
  "createDate": [
    2024,
    1,
    15,
    0,
    58,
    2,
    734563000
  ],
  "updateDate": [
    2024,
    1,
    15,
    0,
    58,
    4,
    399659000
  ]
}
```

Ошибка выполнения (code 500):

```text
Person created error
```

___

PUT-запрос:

```http request
 http://localhost:8088/person
```

Body:

```json
{
  "uuid": "aaef44fa-d2da-46c9-ab2f-3705d5dec36e",
  "name": "name1",
  "surname": "surname1",
  "sex": "MALE",
  "passportSeries": "HM",
  "passportNumber": "4994560",
  "createDate": [
    2024,
    1,
    15,
    0,
    58,
    2,
    734563000
  ],
  "updateDate": [
    2024,
    1,
    15,
    0,
    58,
    4,
    399659000
  ]
}
```

Результат (code 200):

```json
{
  "uuid": "a2ac50ad-fc09-4da1-91cd-f7e32751f6ab",
  "name": "name1",
  "surname": "surname1",
  "sex": "MALE",
  "passportSeries": "HM",
  "passportNumber": "4994560",
  "createDate": [
    2024,
    1,
    15,
    1,
    4,
    7,
    394116000
  ],
  "updateDate": [
    2024,
    1,
    15,
    1,
    4,
    7,
    394392000
  ]
}
```

Ошибка выполнения (code 500):

```text
Person updated error
```

___
DELETE-запрос (code 200):

```http request
 http://localhost:8088/person?uuid=aaef44fa-d2da-46c9-ab2f-3705d5dec36e
```

Результат:

```text
Person is deleted
```

Ошибка выполнения (500):

```text
Person deleted error
```

___
___

### HouseController

GET-запрос по uuid:

```http request
 http://localhost:8080/house?uuid=a0fbbcb0-74ef-43be-a626-55d5b42c9dc1
```

Результат (code 200):

```json
{
  "uuid": "a0fbbcb0-74ef-43be-a626-55d5b42c9dc1",
  "area": 33.6,
  "country": "country1",
  "city": "city1",
  "street": "street1",
  "number": 1,
  "createDate": [
    2024,
    1,
    14,
    21,
    13,
    3,
    860747000
  ]
}
```

House не найден (code 404):

```text
House not found
```

___
GET-запрос по page и count:

```http request
 http://localhost:8088/house?page=1&count=3
```

Результат (code 200):

```json
[
  {
    "uuid": "a0fbbcb0-74ef-43be-a626-55d5b42c9dc1",
    "area": 33.6,
    "country": "country1",
    "city": "city1",
    "street": "street1",
    "number": 1,
    "createDate": [
      2024,
      1,
      14,
      21,
      13,
      3,
      860747000
    ]
  },
  {
    "uuid": "e0f4a6c5-b1d3-475d-83dc-98079744280d",
    "area": 4.9,
    "country": "country1",
    "city": "city1",
    "street": "street4",
    "number": 55,
    "createDate": [
      2024,
      1,
      14,
      21,
      13,
      35,
      591357000
    ]
  },
  {
    "uuid": "6dee35d5-e84b-4845-aed5-ed0d0daa8e4c",
    "area": 55.6,
    "country": "country1",
    "city": "city1",
    "street": "street2",
    "number": 34,
    "createDate": [
      2024,
      1,
      14,
      21,
      14,
      6,
      428911000
    ]
  }
]
```

Ошибка выполнения (code 500):

```text
Error retrieving houses list
```

___
GET-запрос по page:

```http request
 http://localhost:8088/house?page=1
```

Результат (code 200):

```json
[
  {
    "uuid": "a0fbbcb0-74ef-43be-a626-55d5b42c9dc1",
    "area": 33.6,
    "country": "country1",
    "city": "city1",
    "street": "street1",
    "number": 1,
    "createDate": [
      2024,
      1,
      14,
      21,
      13,
      3,
      860747000
    ]
  },
  {
    "uuid": "e0f4a6c5-b1d3-475d-83dc-98079744280d",
    "area": 4.9,
    "country": "country1",
    "city": "city1",
    "street": "street4",
    "number": 55,
    "createDate": [
      2024,
      1,
      14,
      21,
      13,
      35,
      591357000
    ]
  },
  {
    "uuid": "6dee35d5-e84b-4845-aed5-ed0d0daa8e4c",
    "area": 55.6,
    "country": "country1",
    "city": "city1",
    "street": "street2",
    "number": 34,
    "createDate": [
      2024,
      1,
      14,
      21,
      14,
      6,
      428911000
    ]
  },
  {
    "uuid": "34b9e030-6665-483e-a071-e43f5485951d",
    "area": 6.5,
    "country": "country1",
    "city": "city1",
    "street": "street3",
    "number": 45,
    "createDate": [
      2024,
      1,
      14,
      21,
      15,
      29,
      637739000
    ]
  },
  {
    "uuid": "a760913d-60eb-4d4b-8dfc-4364c39d99dd",
    "area": 88.9,
    "country": "country1",
    "city": "city1",
    "street": "street7",
    "number": 81,
    "createDate": [
      2024,
      1,
      14,
      21,
      16,
      4,
      10542000
    ]
  }
]
```

Ошибка выполнения (code 500):

```text
Error retrieving houses list
```

___
GET-запрос по count:

```http request
 http://localhost:8088/house?count=3
```

Результат (code 200):

```json
[
  {
    "uuid": "a0fbbcb0-74ef-43be-a626-55d5b42c9dc1",
    "area": 33.6,
    "country": "country1",
    "city": "city1",
    "street": "street1",
    "number": 1,
    "createDate": [
      2024,
      1,
      14,
      21,
      13,
      3,
      860747000
    ]
  },
  {
    "uuid": "e0f4a6c5-b1d3-475d-83dc-98079744280d",
    "area": 4.9,
    "country": "country1",
    "city": "city1",
    "street": "street4",
    "number": 55,
    "createDate": [
      2024,
      1,
      14,
      21,
      13,
      35,
      591357000
    ]
  },
  {
    "uuid": "6dee35d5-e84b-4845-aed5-ed0d0daa8e4c",
    "area": 55.6,
    "country": "country1",
    "city": "city1",
    "street": "street2",
    "number": 34,
    "createDate": [
      2024,
      1,
      14,
      21,
      14,
      6,
      428911000
    ]
  }
]
```

Ошибка выполнения (code 500):

```text
Error retrieving houses list
```

Неверный запрос (code 400):

```text
Invalid request parameters
```

___
POST-запрос:

```http request
  http://localhost:8088/house
```

Body:

```json
{
  "area": 343.6,
  "country": "country1",
  "city": "city1",
  "street": "street6",
  "number": 14
}
```

Результат (code 200):

```json
{
  "uuid": "4643b27b-aad7-48fa-a99f-6c024f3de1e9",
  "area": 343.6,
  "country": "country1",
  "city": "city1",
  "street": "street6",
  "number": 14,
  "createDate": [
    2024,
    1,
    15,
    1,
    19,
    26,
    472405000
  ]
}
```

Ошибка выполнения (code 500):

```text
House created error
```

___

PUT-запрос:

```http request
 http://localhost:8088/house
```

Body:

```json
{
  "uuid": "4643b27b-aad7-48fa-a99f-6c024f3de1e9",
  "area": 363.6,
  "country": "country",
  "city": "city",
  "street": "street",
  "number": 14
}
```

Результат (code 200):

```json
{
  "uuid": "4643b27b-aad7-48fa-a99f-6c024f3de1e9",
  "area": 363.6,
  "country": "country",
  "city": "city",
  "street": "street",
  "number": 14,
  "createDate": [
    2024,
    1,
    15,
    1,
    19,
    26,
    472405000
  ]
}
```

Ошибка выполнения (code 500):

```text
House updated error
```

___
DELETE-запрос (code 200):

```http request
 http://localhost:8088/house?uuid=4643b27b-aad7-48fa-a99f-6c024f3de1e9
```

Результат:

```text
House is deleted
```

Ошибка выполнения (500):

```text
House deleted error
```

___


