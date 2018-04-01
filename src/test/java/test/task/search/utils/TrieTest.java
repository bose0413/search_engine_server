package test.task.search.utils;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TrieTest {

    private Trie testTrie;

    @Before
    public void initTestRoot() {
        testTrie = new Trie("");
        // test ted
        Trie.TrieNode testRoot = new Trie.TrieNode();

        Trie.TrieNode next = testRoot.getChildren().computeIfAbsent('t', c -> new Trie.TrieNode());
        next = next.getChildren().computeIfAbsent('e', c -> new Trie.TrieNode());
        next.getChildren().computeIfAbsent('d', c -> new Trie.TrieNode()).setWord(true);
        next = next.getChildren().computeIfAbsent('s', c -> new Trie.TrieNode());
        next.getChildren().computeIfAbsent('t', c -> new Trie.TrieNode()).setWord(true);

        testTrie.root = testRoot;
        testTrie.content = "test ted";
    }

    @Test
    public void insert() {
        Trie trie = new Trie("");
        trie.append("test ted");
        assertThat(trie.root).isEqualTo(testTrie.root);

        trie.append("test");
        assertThat(trie.root).isEqualTo(testTrie.root);
    }

    @Test
    public void find() {
        assertThat(testTrie.find("test")).isTrue();
        assertThat(testTrie.find("ted")).isTrue();

        assertThat(testTrie.find("random")).isFalse();
        assertThat(testTrie.find("est")).isFalse();
        assertThat(testTrie.find(null)).isFalse();
    }
}