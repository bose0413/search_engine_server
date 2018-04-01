package test.task.search.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import test.task.search.SearchApplication;
import test.task.search.mapper.DocumentMapper;
import test.task.search.repository.DocumentRepository;
import test.task.search.repository.entity.Document;
import test.task.search.service.DocumentService;
import test.task.search.service.dto.DocumentDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class DocumentServiceImplTest {

    private static Document DOCUMENT_1 = new Document("1", "test random car !@#$%^&*()");
    private static Document DOCUMENT_2 = new Document("2", "tv !@#$%^&*() weather chair car");

    private static DocumentDTO DOCUMENT_DTO_1 = new DocumentDTO("1", "test random car !@#$%^&*()");
    private static DocumentDTO DOCUMENT_DTO_2 = new DocumentDTO("2", "tv !@#$%^&*() weather chair car");

    private static int COUNT = 3;
    private static String SEARCH_TOKENS = "random car";

    @MockBean
    private DocumentMapper documentMapper;

    @MockBean
    private DocumentRepository documentRepository;

    private DocumentService documentService;

    @Before
    public void setUp() {
        documentService = new DocumentServiceImpl(documentRepository, documentMapper);

        //Repository
        when(documentRepository.count()).thenReturn(COUNT);

        when(documentRepository.searchByTokens(SEARCH_TOKENS)).thenReturn(Arrays.asList(DOCUMENT_1, DOCUMENT_2));
        when(documentRepository.searchByTokens(not(eq(SEARCH_TOKENS)))).thenReturn(Collections.emptyList());

        when(documentRepository.findByKey(DOCUMENT_1.getKey())).thenReturn(Optional.of(DOCUMENT_1));
        when(documentRepository.findByKey(not(eq(DOCUMENT_1.getKey())))).thenReturn(Optional.empty());

        //Mapper
        when(documentMapper.toDto(DOCUMENT_1)).thenReturn(DOCUMENT_DTO_1);
        when(documentMapper.toDto(Arrays.asList(DOCUMENT_1, DOCUMENT_2))).thenReturn(Arrays.asList(DOCUMENT_DTO_1, DOCUMENT_DTO_2));

        when(documentMapper.toEntity(DOCUMENT_DTO_1)).thenReturn(DOCUMENT_1);
    }

    @Test
    public void getByKey() {
        assertThat(documentService.getByKey(DOCUMENT_1.getKey()))
                .isPresent()
                .contains(DOCUMENT_DTO_1);

        assertThat(documentService.getByKey("RANDOM"))
                .isEmpty();
        assertThat(documentService.getByKey(null))
                .isEmpty();
    }

    @Test
    public void searchByTokens() {
        assertThat(documentService.searchByTokens(SEARCH_TOKENS))
                .hasSize(2)
                .contains(DOCUMENT_DTO_1, DOCUMENT_DTO_2);

        assertThat(documentService.searchByTokens("RANDOM"))
                .hasSize(0);
    }

    @Test
    public void saveDocument() {
        documentService.saveDocument(DOCUMENT_DTO_1);
        verify(documentRepository, times(1)).save(DOCUMENT_1);
    }

    @Test
    public void getCount() {
        assertThat(documentService.getCount()).isEqualTo(COUNT);
    }
}