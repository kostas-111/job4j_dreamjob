package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.File;

import java.util.Optional;

@Repository
public class Sql2oFileRepository implements FileRepository {

    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    /*
    Обратите внимание на флаг true, который указывает на необходимость возврата ключа из запроса.
     getKey() соответственно нам его возвращает
     */
    @Override
    public File save(File file) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("INSERT INTO files (name, path) VALUES (:name, :path)", true)
                    .addParameter("name", file.getName())
                    .addParameter("path", file.getPath());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            file.setId(generatedId);
            return file;
        }
    }

    /*
    Мы добавляем параметр с помощью addParameter.
    Удобный метод executeAndFetchFirst возвращает нам сразу первый элемент из выборки.
    Не нужно писать if, как с JDBC.
     */
    @Override
    public Optional<File> fidById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM files WHERE id = :id");
            var file = query.addParameter("id", id).executeAndFetchFirst(File.class);
            return Optional.ofNullable(file);
        }
    }

    @Override
    public void deleteById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM files WHERE id = :id");
            query.addParameter("id", id).executeUpdate();
        }
    }
}
