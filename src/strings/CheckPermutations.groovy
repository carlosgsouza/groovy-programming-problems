package strings

import spock.lang.Specification
import spock.lang.Unroll;

class CheckPermutations extends Specification {

	@Unroll
	def "isPermutation should return #result for strings '#string1' and '#string2'"() {
		expect:
		isPermutation(string1, string2) == result

		where:
		string1	| string2	| result
		"a"		| "a"		| true
		"abc"	| "abc"		| true
		"aabbc"	| "abc"		| false
		"cba"	| "abc"		| true
		"aab"	| "aba"		| true
		"az"	| "za"		| true
	}
	
	boolean isPermutation(String string1, String string2) {
		if(string1.length() != string2.length()) {
			return false
		}

		int[] charCount1 = countChars(string1);
		int[] charCount2 = countChars(string2);

		for(int i = 0; i < charCount1.length; i++) {
			if(charCount1[i] != charCount2[i]) {
				return false;
			}
		}

		return true;
	}

	int[] countChars(String string) {
		int[] result = new int[26];
		for(int i = 0; i < 26; i++) {
			result[i] = 0;
		}

		for(int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			int index = c - (char)'a';
			
			if(index < 0 || index >= 26) {
				throw new IllegalArgumentException("Only strings with a-z are supported");
			}

			result[index]++;
		}

		return result;
	}
}
