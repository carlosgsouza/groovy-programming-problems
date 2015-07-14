package strings

import spock.lang.Specification
import spock.lang.Unroll

class IsAnagramSubstring extends Specification {

	@Unroll
	def "isAnagramSubstring(#str1, #str2) == #result"() {
		expect:
		isAnagramSubstring(str1, str2) == result

		where:
		str1			| str2		| result
		"abc"			| "abc"		| true
		"abc"			| "cab"		| true
		"abc"			| "def"		| false
		"abcdefabc"		| "fed"		| true
		"abcdefabc"		| "ghi"		| false
		"abcdeefabc"	| "fed"		| false
		"abcdeefabc"	| "fee"		| true
		"abc"			| ""		| true
		""				| ""		| true
		""				| "def"		| false
	}

	/*
	 abcdadefdabc	| ddef
	 5--8
	 count | expected
	 d: 2	   | 2
	 e: 1     | 1
	 f: 1     | 1
	 charactersCountMatching=2
	 */

	public boolean isAnagramSubstring(String str1, String str2) {
		if(str1.length() < str2.length()) {
			return false;
		}

		Map<Character, Integer> expectedCount = countCharacters(str2);

		Map<Character, Integer> windowCount = new HashMap<Character, Integer>();
		for(Character c : expectedCount.keySet()) {
			windowCount.put(c, 0);
		}

		int start = 0;
		int end = str2.length() - 1;

		for(int i=0; i <= end; i++) {
			char c = str1.charAt(i);
			if(windowCount.get(c) != null) {
				windowCount.put(c, windowCount.get(c)+1);
			}
		}

		int charactersCountMatching = 0;
		for(Character c : windowCount.keySet()) {
			if(windowCount.get(c) == expectedCount.get(c)) {
				charactersCountMatching++;
			}
		}

		if(charactersCountMatching == expectedCount.size()) {
			return true;
		}

		while(end+1 < str1.length()) {
			start++;
			end++;

			char removedChar = str1.charAt(start-1);
			if(windowCount.get(removedChar) != null) {
				if(windowCount.get(removedChar) == expectedCount.get(removedChar)) {
					charactersCountMatching--;
				}

				windowCount.put(removedChar, windowCount.get(removedChar)-1);
				if(windowCount.get(removedChar) == expectedCount.get(removedChar)) {
					charactersCountMatching++;
				}
			}

			char addedChar = str1.charAt(end); //d
			if(windowCount.get(addedChar) != null) {
				if(windowCount.get(addedChar) == expectedCount.get(addedChar)) {
					charactersCountMatching--;
				}

				windowCount.put(addedChar, windowCount.get(addedChar)+1);
				if(windowCount.get(addedChar) == expectedCount.get(addedChar)) {
					charactersCountMatching++;
				}
			}

			if(charactersCountMatching == windowCount.size()) {
				return true;
			}

		}

		return false;
	}

	private Map<Character, Integer> countCharacters(String str) {
		Map<Character, Integer> result = new HashMap<Character, Integer>();

		for(int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if(result.get(c) == null) {
				result.put(c, 0);
			}

			result.put(c, result.get(c)+1);
		}

		return result;
	}

}
