package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();

    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(1, "Donald Trump", "Опыт работы 10 лет. Навыки: Java 17, Jakarta EE10: EJB, JMS, JSF, CDI, JPA; Application servers: Oracle WebLogic, EAP, Wildfly, Tomcat"));
        save(new Candidate(2, "Dobrynia Nikitich", "Опыт работы 40 лет. Навыки: Assembler, Pascal, QBasic, Java 1"));
        save(new Candidate(3, "Bruce Lee", "Опыт работы 2 года. SQL, PL/pgSQL, PostgreSQL, Exel, Java, Maven, Git"));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
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
                (id, oldCandidate) -> new Candidate(oldCandidate.getId(), candidate.getName(), candidate.getDescription())) != null;
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
