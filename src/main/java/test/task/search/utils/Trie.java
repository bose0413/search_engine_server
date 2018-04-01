package test.task.search.utils;

import lombok.*;

import java.util.Arrays;
import java.util.HashMap;

@ToString
@EqualsAndHashCode
public class Trie {

    String content = "";

    TrieNode root = new TrieNode();

    public Trie(String content) {
        this.append(content);
    }

    /**
     * split input on words (by space) and add them to the tree. Also, add it to the tail of the context
     * @param str string to be added to the tree
     */
    public void append(String str) {
        if (str == null || str.isEmpty()) {
            return;
        }

        if (!this.content.isEmpty()) {
            this.content += " ";
        }
        this.content += str;

        Arrays.asList(str.split(" ")).forEach(this::insert);
    }

    /**
     * Checks if the tree contains the word
     * @param word word to be searched for
     * @return true if tree contains the exact word. Otherwise, false.
     */
    public boolean find(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isWord();
    }

    public String getContent() {
        return content;
    }

    private void insert(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            current = current.getChildren()
                    .computeIfAbsent(word.charAt(i), c -> new TrieNode());
        }
        current.setWord(true);
    }

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    @EqualsAndHashCode
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    static class TrieNode {
        private HashMap<Character, TrieNode> children = new HashMap<>();

        private boolean isWord = false;
    }
}
