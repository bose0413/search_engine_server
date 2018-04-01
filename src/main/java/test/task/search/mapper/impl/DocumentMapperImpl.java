package test.task.search.mapper.impl;

import org.springframework.stereotype.Service;
import test.task.search.mapper.DocumentMapper;
import test.task.search.repository.entity.Document;
import test.task.search.service.dto.DocumentDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity Document and its DTO.
 */
@Service
public class DocumentMapperImpl implements DocumentMapper {


    @Override
    public Document toEntity(DocumentDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Document(dto.getKey(), dto.getContent());
    }

    @Override
    public DocumentDTO toDto(Document entity) {
        if (entity == null) {
            return null;
        }
        return new DocumentDTO(entity.getKey(), entity.getContent());
    }

    @Override
    public List<Document> toEntity(List<DocumentDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentDTO> toDto(List<Document> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}