package test.task.search.repository.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import test.task.search.SearchApplication;
import test.task.search.repository.entity.Document;
import test.task.search.utils.Trie;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class InMemoryDocumentRepositoryImplTest {

    private static Document DOCUMENT_NEW_1 = new Document("NEW_1", "test1");
    private static Document DOCUMENT_NEW_2 = new Document("NEW_2", "test2");
    private static Trie TRIE_1 = new Trie("test random car !@#$%^&*()");
    private static Trie TRIE_2 = new Trie("tv !@#$%^&*() weather chair car");
    private static Trie TRIE_3 = new Trie("auto test spring");
    private static Document DOCUMENT_1 = new Document("1", TRIE_1.getContent());
    private static Document DOCUMENT_2 = new Document("2", TRIE_2.getContent());
    private static Document DOCUMENT_3 = new Document("3", TRIE_3.getContent());

    @Autowired
    private InMemoryDocumentRepositoryImpl documentRepository;

    @Before
    public void setup() {
        documentRepository.data = new HashMap<>();
        documentRepository.data.put(DOCUMENT_1.getKey(), TRIE_1);
        documentRepository.data.put(DOCUMENT_2.getKey(), TRIE_2);
        documentRepository.data.put(DOCUMENT_3.getKey(), TRIE_3);
    }

    @Test
    public void findByKey() {
        assertThat(documentRepository.findByKey(DOCUMENT_1.getKey()))
                .isPresent().contains(DOCUMENT_1);
        assertThat(documentRepository.findByKey(DOCUMENT_2.getKey()))
                .isPresent().contains(DOCUMENT_2);

        assertThat(documentRepository.findByKey(DOCUMENT_NEW_1.getKey()))
                .isEmpty();
        assertThat(documentRepository.findByKey(null))
                .isEmpty();
    }

    @Test
    public void searchByTokens() {
        assertThat(documentRepository.searchByTokens("!@#$%^&*() car"))
                .hasSize(2)
                .contains(DOCUMENT_1, DOCUMENT_2);

        assertThat(documentRepository.searchByTokens("ar"))
                .hasSize(0);
    }

    @Test
    public void save() {
        documentRepository.save(DOCUMENT_NEW_1);
        documentRepository.save(DOCUMENT_NEW_2);

        assertThat(documentRepository.data.size()).isEqualTo(5);

        assertThat(documentRepository.data.get(DOCUMENT_NEW_1.getKey()).getContent())
                .isEqualTo(DOCUMENT_NEW_1.getContent());
        assertThat(documentRepository.data.get(DOCUMENT_NEW_2.getKey()).getContent())
                .isEqualTo(DOCUMENT_NEW_2.getContent());
    }

    @Test
    public void count() {
        assertThat(documentRepository.count()).isEqualTo(documentRepository.data.size());
    }
}