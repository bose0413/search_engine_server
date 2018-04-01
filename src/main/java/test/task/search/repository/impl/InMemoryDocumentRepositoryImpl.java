package test.task.search.repository.impl;

import org.springframework.stereotype.Repository;
import test.task.search.repository.DocumentRepository;
import test.task.search.repository.entity.Document;
import test.task.search.utils.Trie;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryDocumentRepositoryImpl implements DocumentRepository {

    Map<String, Trie> data = new HashMap<>();

    @Override
    public Optional<Document> findByKey(String key) {
        return Optional.ofNullable(data.get(key))
                .map(Trie::getContent)
                .map(content -> new Document(key, content));
    }

    @Override
    public List<Document> searchByTokens(String tokens) {
        Predicate<Map.Entry<String, Trie>> areTokensIncluded = entry -> Arrays.stream(tokens.split(" "))
                .allMatch(word->entry.getValue().find(word));

        return data.entrySet().stream()
                .filter(areTokensIncluded)
                .map(entry -> new Document(entry.getKey(), entry.getValue().getContent()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Document document) {
        data.put(document.getKey(), new Trie(document.getContent()));
    }

    @Override
    public int count() {
        return data.size();
    }
}
