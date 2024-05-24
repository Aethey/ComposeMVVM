package com.example.gitsimpledemo.util

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description:
 */

class TrieNode {
    val children: MutableMap<Char, TrieNode> = mutableMapOf()
    var isEndOfWord: Boolean = false
}

class Trie {
    private val root = TrieNode()

    fun insert(word: String) {
        var node = root
        for (char in word) {
            node = node.children.computeIfAbsent(char) { TrieNode() }
        }
        node.isEndOfWord = true
    }

    fun search(prefix: String): List<String> {
        val results = mutableListOf<String>()
        var node = root
        for (char in prefix) {
            node = node.children[char] ?: return results
        }
        collectWords(node, prefix, results)
        return results
    }

    private fun collectWords(node: TrieNode, prefix: String, results: MutableList<String>) {
        if (node.isEndOfWord) {
            results.add(prefix)
        }
        for ((char, childNode) in node.children) {
            collectWords(childNode, prefix + char, results)
        }
    }
}
