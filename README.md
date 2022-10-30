# java-filmorate

В проекте реализованы: 

* (Модуль 3. Спринт 9):
1. Модели данных приложения: Film, User
2. REST-контроллеры: FilmController, UserController
3. Настроена валидация данных (в REST-контроллерах и с помощью аннотаций spring-boot-starter-validation)
4. Настроено ручное логирование
5. Валидация проверяется тестами Unit5

* (Модуль 3. Спринт 10):
1. Доработана архитектура проекта, созданы 
* интерфейсы FilmStorage и UserStorage; 
* классы InMemoryFilmStorage и InMemoryUserStorage (@Component); 
* классы UserService и FilmService (@Service)
2. API доведен до соответствия REST
3. Настроен ExceptionHandler для централизованной обработки ошибок

* (Модуль 3. Спринт 11):
1. В класс User добавлено поле friendRequests;
2. В класс Film добавлены поля genres и ratingMPA;
3. Спроектировна схема БД:
   ![ER-диаграмма](/images/ER-filmorate.png)
* Связь между пользователями "Многие к многим", для их сопоставления создана таблица friends_line.
  Поле status отражает статус дружбы: подтверждена - TRUE, неподтверждена - FALSE
* Связь между пользователями и фильмами, а также фильмами и жанрами также "Многие к многим". Для их сопоставления
  созданы таблицы favorite_films_line и film_genre_line соответственно
* Связь между рейтингом и фильмом "Один к многим", т.к. rating_MPA_id уникален и может быть присвоен множеству фильмов
* Для таблиц friends_line, favorite_films_line и film_genre_line использованы составные первичные ключи из двух id.
