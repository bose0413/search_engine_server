package test.task.search.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import test.task.search.SearchApplication;
import test.task.search.service.DocumentService;
import test.task.search.service.dto.DocumentDTO;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class DocumentResourceTest {

    private static final String TOKENS_TO_SEARCH = "!@#$%^&*()_ factory test";

    private static final String DOCUMENT_1_KEY = "1";
    private static final String DOCUMENT_1_CONTENT = "!@#$%^&*()_ factory test eggs";

    private static final String DOCUMENT_2_KEY = "2";
    private static final String DOCUMENT_2_CONTENT = "factory card arm #DOG !important test !@#$%^&*()_";

    private static final String DOCUMENT_3_KEY = "3";
    private static final String DOCUMENT_3_CONTENT = "phone weather house chair note english";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertThat(mappingJackson2HttpMessageConverter).isNotNull();
    }

    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();

        documentService.saveDocument(new DocumentDTO(DOCUMENT_1_KEY, DOCUMENT_1_CONTENT));
        documentService.saveDocument(new DocumentDTO(DOCUMENT_2_KEY, DOCUMENT_2_CONTENT));
    }


    @Test
    public void createDocument() throws Exception {
        DocumentDTO documentToStore = new DocumentDTO(DOCUMENT_3_KEY, DOCUMENT_3_CONTENT);
        int beforeCount = documentService.getCount();

        mockMvc.perform(post("/api/documents/")
                .contentType(contentType)
                .content(json(documentToStore)))
                .andExpect(status().isCreated());

        assertThat(documentService.getByKey(DOCUMENT_3_KEY)).isPresent().contains(documentToStore);
    }

    @Test
    public void createDocumentWithoutRequiredParams() throws Exception {
        DocumentDTO documentToStore = new DocumentDTO("random", "");
        int beforeCount = documentService.getCount();

        mockMvc.perform(post("/api/documents/")
                .contentType(contentType)
                .content(json(documentToStore)))
                .andExpect(status().isBadRequest());

        documentToStore = new DocumentDTO("", "content");
        mockMvc.perform(post("/api/documents/")
                .contentType(contentType)
                .content(json(documentToStore)))
                .andExpect(status().isBadRequest());

        documentToStore = new DocumentDTO("", "");
        mockMvc.perform(post("/api/documents/")
                .contentType(contentType)
                .content(json(documentToStore)))
                .andExpect(status().isBadRequest());

        assertThat(documentService.getCount()).isEqualTo(beforeCount);
    }

    @Test
    public void getDocument() throws Exception {
        mockMvc.perform(get("/api/documents/{id}", DOCUMENT_1_KEY))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.key").value(DOCUMENT_1_KEY))
                .andExpect(jsonPath("$.content").value(DOCUMENT_1_CONTENT));
    }

    @Test
    public void getNonExistingDocument() throws Exception {
        mockMvc.perform(get("/api/documents/{id}", "NON_EXISTING_KEY"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchDocuments() throws Exception {
        mockMvc.perform(get("/api/documents").param("query", TOKENS_TO_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].key", hasItems(DOCUMENT_1_KEY, DOCUMENT_2_KEY)))
                .andExpect(jsonPath("$[*].content", hasItems(DOCUMENT_1_CONTENT, DOCUMENT_2_CONTENT)));
    }

    @Test
    public void searchNonExistingDocuments() throws Exception {
        mockMvc.perform(get("/api/documents").param("query", "NON_EXISTING_TOKEN"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @SuppressWarnings("unchecked")
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}