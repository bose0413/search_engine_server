package test.task.search.mapper.impl;

import org.junit.Test;
import test.task.search.mapper.DocumentMapper;
import test.task.search.repository.entity.Document;
import test.task.search.service.dto.DocumentDTO;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentMapperImplTest {

    private static Document DOCUMENT_1 = new Document("1", "test1");
    private static Document DOCUMENT_2 = new Document("2", "test2");
    private static DocumentDTO DOCUMENT_DTO_1 = new DocumentDTO("1", "test1");
    private static DocumentDTO DOCUMENT_DTO_2 = new DocumentDTO("2", "test2");

    private DocumentMapper documentMapper = new DocumentMapperImpl();

    @Test
    public void toEntity() {
        assertThat(documentMapper.toEntity(DOCUMENT_DTO_1)).isEqualTo(DOCUMENT_1);
        assertThat(documentMapper.toEntity(Arrays.asList(DOCUMENT_DTO_1, DOCUMENT_DTO_2)))
                .hasSize(2)
                .contains(DOCUMENT_1, DOCUMENT_2);
    }

    @Test
    public void toDto() {
        assertThat(documentMapper.toDto(DOCUMENT_1)).isEqualTo(DOCUMENT_DTO_1);
        assertThat(documentMapper.toDto(Arrays.asList(DOCUMENT_1, DOCUMENT_2)))
                .hasSize(2)
                .contains(DOCUMENT_DTO_1, DOCUMENT_DTO_2);
    }
}