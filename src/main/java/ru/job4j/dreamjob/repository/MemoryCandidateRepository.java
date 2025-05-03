package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Потокобезопасный репозиторий, использующий ConcurrentHashMap и AtomicInteger
 * для безопасного хранения и управления кандидатами в памяти.
 */
@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(1, "Donald Trump", "Опыт работы 10 лет. Навыки: Java 17, Jakarta EE10: EJB, JMS, JSF, CDI, JPA; Application servers: Oracle WebLogic, EAP, Wildfly, Tomcat", 1));
        save(new Candidate(2, "Dobrynia Nikitich", "Опыт работы 40 лет. Навыки: Assembler, Pascal, QBasic, Java 1", 3));
        save(new Candidate(3, "Bruce Lee", "Опыт работы 2 года. SQL, PL/pgSQL, PostgreSQL, Exel, Java, Maven, Git", 3));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.getAndIncrement());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        Candidate candidate = candidates.remove(id);
        return candidate != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(oldCandidate.getId(), candidate.getName(), candidate.getDescription(), candidate.getCityId())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
