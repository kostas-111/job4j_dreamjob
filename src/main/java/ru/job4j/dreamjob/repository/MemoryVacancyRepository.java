package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.Vacancy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryVacancyRepository implements VacancyRepository {

    private static final MemoryVacancyRepository INSTANCE = new MemoryVacancyRepository();

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(1, "Intern Java Developer", "Начальная позиция для недавних выпускников или студентов с базовыми знаниями Java."));
        save(new Vacancy(2, "Junior Java Developer", "Позиция для разработчиков с опытом работы до 2 лет в области разработки на Java."));
        save(new Vacancy(3, "Junior+ Java Developer", "Роль для разработчиков с опытом 2-3 года и крепкими основами в Java."));
        save(new Vacancy(4, "Middle Java Developer", "Позиция среднего уровня, требующая от 3 до 5 лет профессионального опыта разработки на Java."));
        save(new Vacancy(5, "Middle+ Java Developer", "Продвинутая роль среднего уровня для опытных разработчиков с глубокими знаниями Java и связанных технологий."));
        save(new Vacancy(6, "Senior Java Developer", "Руководящая роль для опытных профессионалов с более чем 5-летним практическим опытом разработки на Java."));
    }

    public static MemoryVacancyRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
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
                (id, oldVacancy) -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription())) != null;
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
