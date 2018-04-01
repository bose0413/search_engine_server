package test.task.search.service.impl;

import org.springframework.stereotype.Service;
import test.task.search.mapper.DocumentMapper;
import test.task.search.repository.DocumentRepository;
import test.task.search.service.DocumentService;
import test.task.search.service.dto.DocumentDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    private DocumentRepository documentRepository;

    private DocumentMapper documentMapper;

    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    @Override
    public Optional<DocumentDTO> getByKey(String key) {
        return documentRepository.findByKey(key)
                .map(documentMapper::toDto);
    }

    @Override
    public List<DocumentDTO> searchByTokens(String tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return Collections.emptyList();
        }

        return documentMapper.toDto(documentRepository.searchByTokens(tokens));
    }

    @Override
    public void saveDocument(DocumentDTO document) {
        documentRepository.save(documentMapper.toEntity(document));
    }

    @Override
    public int getCount() {
        return documentRepository.count();
    }
}
