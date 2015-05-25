package strings

import spock.lang.Specification

class Trie extends Specification {

	def "should add elements to the trie and check if an element is in the trie"() {
		given:
		MyTrie trie = new MyTrie()
		
		when:
		trie.add("cartoon")
		trie.add("car")
		trie.add("a")
		trie.add("toon")
		trie.add("network")
		trie.add("net")
		trie.add("work")
		trie.add("work")
		trie.add("work")
		
		then:
		trie.contains("cartoon") == true
		trie.contains("car")     == true
		trie.contains("a")       == true
		trie.contains("network") == true
		trie.contains("net")     == true
		trie.contains("work")    == true
		
		and:
		trie.contains("bob") == false
		trie.contains("ca") == false
		trie.contains("workaholic") == false
	}
	
	class MyTrie {
		
		Node root = new Node()
		
		public boolean contains(String word) {
			Node currentNode = root
			
			for(int i = 0; i < word.length(); i++) {
				char c = word.charAt(i)
				
				if(currentNode.next[c] == null) {
					return false
				}
				
				currentNode = currentNode.next[c]
			}
			
			return (currentNode.isFinal) 
		}
		
		public boolean add(String word) {
			Node currentNode = root
			
			for(int i = 0; i < word.length(); i++) {
				char c = word.charAt(i)
				
				if(currentNode.next[c] == null) {
					currentNode.next[c] = new Node()
				}
				
				currentNode = currentNode.next[c]
			}	
			
			currentNode.isFinal = true
		}
	}
	
	class Node {
		boolean isFinal = false
		Map<Character, Node> next = [:]
	}
}
