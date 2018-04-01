package test.task.search.repository;

import test.task.search.repository.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {
    Optional<Document> findByKey(String key);

    List<Document> searchByTokens(String tokens);

    void save(Document document);

    int count();
}
