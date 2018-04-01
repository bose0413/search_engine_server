package test.task.search.service;

import test.task.search.service.dto.DocumentDTO;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    Optional<DocumentDTO> getByKey(String key);

    List<DocumentDTO> searchByTokens(String tokens);

    void saveDocument(DocumentDTO document);

    int getCount();
}
