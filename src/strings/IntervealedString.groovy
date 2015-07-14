package strings

import java.util.List

import spock.lang.Specification


class IntervealedString extends Specification {
	
	/*
	Given two strings .Print all the interleavings of the two strings. 
	Interleaving means that the if B comes after A .It should also come after A in the interleaved string. 
	ex- 
	AB and CD 
	ABCD 
	ACBD 
	ACDB 
	CABD 
	CADB 
	CDAB
		
	source: http://www.careercup.com/question?id=14360665
	 */
	
	def "should calculate all the intervealed string of 'AB' and 'CD'"() {
		expect:
		intervealedStrings("AB", "CD") == ["ABCD", "ACBD", "ACDB", "CABD", "CADB", "CDAB"]
	}
		
	
	public List<String> intervealedStrings(String str1, String str2) {
		List<String> result = new ArrayList<String>();
		char[] s = new char[str1.length() + str2.length()];	
		intervealedPermutations(result, str1, str2, 0, 0, s, 0);
		
		return result;
	}
			
	
	public void intervealedPermutations(List<String> result, String str1, String str2, int next1, int next2, char[] s, int i) {		
		if(next1 == str1.length() && next2 == str2.length()) {
			result.add(new String(s));
		}
	
		if(next1 < str1.length()) {
			s[i] = str1.charAt(next1);
			intervealedPermutations(result, str1, str2, next1+1, next2, s, i+1);
		}
	
		if(next2 < str2.length()) {
			s[i] = str2.charAt(next2);
			intervealedPermutations(result, str1, str2, next1, next2+1, s, i+1);
		}
	}

		

}
