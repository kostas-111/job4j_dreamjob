package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.City;

import java.util.Collection;

/*
Sql2o это клиент для работы с БД, который позволяет писать меньше boiler-plate кода.
В целом это небольшая ORM.
Модели в нашем коде соответствуют таблицам БД.
Т.е. наши объекты (Object) соответствуют (Mapping) таблицам (Relation) БД.
 */
@Repository
public class Sql2oCityRepository implements CityRepository {

    private final Sql2o sql2o;

    public Sql2oCityRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    /*
    Метод open() возвращает соедиение из пула. Далее мы создаем запрос через createQuery
    и выполняем его с помощью метода executeAndFetch, который за нас преобразует
    ResultSet в коллекцию городов.
    Sql2o оборачивает всё в необрабатываемые исключения.
    С точки зрения ООП, Sql2o предоставляет более высокий уровень абстракции для работы с БД.
    Например, мы работаем с запросом в привычном нам понимании, а не Statement/PrepareStatement,
    которые являются деталями реализации JDBC непосредственно.

    Используется try-with-resource. Это не значит, что соединение к БД закрывается.
    Во-первых, это не java.sql.Connection. Это Connection Sql2o.
    Во-вторых, этот объект это что-то вроде сессии работы с БД.
    В рамках него мы можем делать несколько операций и если произойдет исключение,
    то произойдет откат изменений, т.е. под капотом Sql2o работает с транзакциями,
    что также удобно т.к. не нужно делать самим commit(), rollback().
     */
    @Override
    public Collection<City> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM cities");
            return query.executeAndFetch(City.class);
        }
    }
}
