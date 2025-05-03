package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Потокобезопасный репозиторий, использующий ConcurrentHashMap и AtomicInteger
 * для безопасного хранения и управления вакансиями в памяти.
 */

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(1, "Intern Java Developer", "Начальная позиция для недавних выпускников или студентов с базовыми знаниями Java.", true, 1));
        save(new Vacancy(2, "Junior Java Developer", "Позиция для разработчиков с опытом работы до 2 лет в области разработки на Java.", true, 2));
        save(new Vacancy(3, "Junior+ Java Developer", "Роль для разработчиков с опытом 2-3 года и крепкими основами в Java.", true, 1));
        save(new Vacancy(4, "Middle Java Developer", "Позиция среднего уровня, требующая от 3 до 5 лет профессионального опыта разработки на Java.", true, 3));
        save(new Vacancy(5, "Middle+ Java Developer", "Продвинутая роль среднего уровня для опытных разработчиков с глубокими знаниями Java и связанных технологий.", true, 3));
        save(new Vacancy(6, "Senior Java Developer", "Руководящая роль для опытных профессионалов с более чем 5-летним практическим опытом разработки на Java.", true, 2));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.getAndIncrement());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        Vacancy removed = vacancies.remove(id);
        return removed != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(), vacancy.getVisible(), vacancy.getCityId())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}