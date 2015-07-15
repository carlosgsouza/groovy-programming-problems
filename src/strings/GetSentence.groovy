package strings

import spock.lang.Specification
import spock.lang.Unroll


public class GetSentence extends Specification {
	
	@Unroll
	def "getSentence(#text, #dictionary) should return '#sentence'"() {
		given: "a set built with the words in dictionary"
		Set<String> dictionarySet = new HashSet<String>()
		dictionary?.each {
			dictionarySet.add(it)
		}

		expect:
		getSentence(text, dictionarySet) == sentence

		where:
		text					| dictionary									| sentence
		"cartoonnetwork"		| ["cartoon", "network"]						| "cartoon network"
		"cartoonnetwork"		| ["network", "cartoon"]						| "cartoon network"
		"cartoonnetwork"		| ["cartoon", "car", "a", "ball", "network"]	| "cartoon network"
		"potatopotato"			| ["potato", "tomato"]							| "potato potato"
		"a"						| ["a", "b"]									| "a"
		"cartoonnetwork"		| ["no", "solution"]							| null
		""						| []											| null
		""						| ["words"]										| null
		null					| ["potato", "tomato"]							| null
		"potatopotato"			| null											| null
		"carcarusotro"			| ["carcar", "car", "us", "otro"]				| "carcar us otro"
	}
	
	public String getSentence(String text, Set<String> dictionary) {
		if(text == null || dictionary == null || text.isEmpty() || dictionary.isEmpty()) {
			return null
		}
		
		TrieNode trie = buildTrie(dictionary);
		
		List<String> S = new ArrayList<String>();
		
		S.add('')
		for(int i = 1; i <= text.length(); i++) {
			S.add(null);
		}
		
		for(int i = 1; i <= text.length(); i++) {
			
			TrieNode currentNode = trie
			for(int j = i-1; j >= 0 && currentNode != null; j--) {
				char c = text.charAt(j)
				
				currentNode = currentNode.next.get(c);
				
				if(currentNode != null && currentNode.word != null && S[j] != null) {
					S.set(i, currentNode.word);
				}
			}
		}
		
		List<String> words = new ArrayList<String>();
		int k = text.length();
		
		while(k >= 1) {
			if(S[k] == null) {
				return null;
			}
			
			words.add(S[k]);
			k = k - S[k].length();
		}
		
		StringBuilder sb = new StringBuilder()
		sb.append(words[words.size() - 1]);
		
		for(int i = words.size() - 2; i>= 0; i--) {
			sb.append(" ").append(words[i]);
		}
		
		String result = sb.toString()
		println "Found solution: $result"
		
		return result;
	}

	private TrieNode buildTrie(Set<String> dictionary) {
		TrieNode trie = new TrieNode();

		for(String word : dictionary) {
			insertWord(trie, word);
		}
		
		println "The following tree was built for the words in $dictionary"
		trie.print()

		return trie;
	}

	private void insertWord(TrieNode trie, String word) {
		TrieNode currentNode = trie;

		for(int i = word.length() - 1; i >= 0; i--) {
			char c = word.charAt(i);
			
			if(currentNode.next.get(c) == null) {
				currentNode.next.put(c, new TrieNode());
			}

			currentNode = currentNode.next.get(c);
			currentNode.character = c
		}

		currentNode.word = word;
	}

	public static class TrieNode {
		public Map<Character, TrieNode> next = new HashMap<Character, TrieNode>();
		public String word = null;
		public char character;
		
		public String toString() {
			return "$character" + ((word != null) ? " ($word)" : "")
		}
		
		public void print(){
			print(0, this)
		}
		
		public void print(int depth, TrieNode node) {
			def s = ""
			
			depth.times {
				s += " "
			}
			
			s += node.toString()
			
			println s
			
			node.next.each { k, v ->
				print(depth+1, v)
			}
		}
	}
}
