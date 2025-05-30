package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.File;

import java.util.Optional;

public interface FileRepository {

    File save(File file);

    Optional<File> fidById(int id);

    void deleteById(int id);
}