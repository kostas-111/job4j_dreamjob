package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.File;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.*;

class Sql2oVacancyRepositoryTest {

    private static Sql2oVacancyRepository sql2oVacancyRepository;

    private static Sql2oFileRepository sql2oFileRepository;

    private static File file;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oVacancyRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oVacancyRepository = new Sql2oVacancyRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        /*
        нужно сохранить хотя бы один файл, т.к. Vacancy от него зависит
         */
        file = new File("test", "test");
        sql2oFileRepository.save(file);
    }

    @AfterAll
    public static void deleteFile() {
        sql2oFileRepository.deleteById(file.getId());
    }

    @AfterEach
    public void clearVacancies() {
        var vacancies = sql2oVacancyRepository.findAll();
        for (var vacancy : vacancies) {
            sql2oVacancyRepository.deleteById(vacancy.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var creationDate = LocalDateTime.now();
        var vacancy = sql2oVacancyRepository.save(new Vacancy(0, "title", "description", creationDate, true, 1, file.getId()));
        var savedVacancy = sql2oVacancyRepository.findById(vacancy.getId()).get();
        assertThat(savedVacancy.getId()).isEqualTo(vacancy.getId());
        assertThat(savedVacancy.getTitle()).isEqualTo(vacancy.getTitle());
        assertThat(savedVacancy.getDescription()).isEqualTo(vacancy.getDescription());
        assertThat(savedVacancy.getVisible()).isEqualTo(vacancy.getVisible());
        assertThat(savedVacancy.getCityId()).isEqualTo(vacancy.getCityId());
        assertThat(savedVacancy.getFileId()).isEqualTo(vacancy.getFileId());
        assertThat(savedVacancy.getCreationDate().withSecond(0).withNano(0))
                .isEqualTo(vacancy.getCreationDate().withSecond(0).withNano(0));
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var creationDate = LocalDateTime.now().withSecond(0).withNano(0);
        var vacancy1 = sql2oVacancyRepository.save(new Vacancy(0, "title1", "description1", creationDate, true, 1, file.getId()));
        var vacancy2 = sql2oVacancyRepository.save(new Vacancy(0, "title2", "description2", creationDate, false, 1, file.getId()));
        var vacancy3 = sql2oVacancyRepository.save(new Vacancy(0, "title3", "description3", creationDate, true, 1, file.getId()));
        var result = sql2oVacancyRepository.findAll();
        assertThat(result).isEqualTo(List.of(vacancy1, vacancy2, vacancy3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oVacancyRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oVacancyRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var creationDate = LocalDateTime.now().withSecond(0).withNano(0);
        var vacancy = sql2oVacancyRepository.save(new Vacancy(0, "title", "description", creationDate, true, 1, file.getId()));
        var isDeleted = sql2oVacancyRepository.deleteById(vacancy.getId());
        var savedVacancy = sql2oVacancyRepository.findById(vacancy.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedVacancy).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oVacancyRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        LocalDateTime creationDate = LocalDateTime.now();
        var vacancy = sql2oVacancyRepository.save(new Vacancy(0, "title", "description", creationDate, true, 1, file.getId()));
        var updatedVacancy = new Vacancy(
                vacancy.getId(), "new title", "new description", creationDate.plusDays(1),
                !vacancy.getVisible(), 1, file.getId()
        );
        var isUpdated = sql2oVacancyRepository.update(updatedVacancy);
        var savedVacancy = sql2oVacancyRepository.findById(updatedVacancy.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(savedVacancy.getId()).isEqualTo(updatedVacancy.getId());
        assertThat(savedVacancy.getTitle()).isEqualTo(updatedVacancy.getTitle());
        assertThat(savedVacancy.getDescription()).isEqualTo(updatedVacancy.getDescription());
        assertThat(savedVacancy.getVisible()).isEqualTo(updatedVacancy.getVisible());
        assertThat(savedVacancy.getCityId()).isEqualTo(updatedVacancy.getCityId());
        assertThat(savedVacancy.getFileId()).isEqualTo(updatedVacancy.getFileId());
        assertThat(savedVacancy.getCreationDate().withSecond(0).withNano(0))
                .isEqualTo(updatedVacancy.getCreationDate().withSecond(0).withNano(0));
    }

    @Test
    public void whenUpdateUnExistingVacancyThenGetFalse() {
        var creationDate = LocalDateTime.now().withSecond(0).withNano(0);
        var vacancy = new Vacancy(0, "title", "description", creationDate, true, 1, file.getId());
        var isUpdated = sql2oVacancyRepository.update(vacancy);
        assertThat(isUpdated).isFalse();
    }
}