package strings

import spock.lang.Specification

class LongestPalindromeSubsequence extends Specification {

	/*
Imagine we have a large string like this "ABCBAHELLOHOWRACECARAREYOUIAMAIDOINGGOOD" which contains multiple palindromes within it, 
like ABCBA, RACECAR, ARA, IAMAI etc... Now write a method which will accept this large string and return the largest palindrome from this string. 

If there are two palindromes which are of same size, it would be sufficient to just return any one of them.

(http://www.careercup.com/question?id=4981417205301248)
	 */
	
	
	def run() {
		expect:
		getLongestPalindromeSubsequence(string) == result
		
		where:
		string		| result
		"a010c"		| "010"
		"abc"		| "a"
		"a001100"	| "001100"
		""			| ""
		"0aa1bb2"	| "aa"
	}
	
	
	def getLongestPalindromeSubsequence(String string) {
		if(string.length() == 0) {
			return ""
		}
		
		char[] s = string.toCharArray()
		
		int first = 0
		int last = 0
		
		// Look for palindromes with odd length starting from the center
		for(int i = 1; i < string.length() - 1; i++) {
			int i_l = i-1, i_r = i+1
			
			while(i_l >= 0 && i_r < string.length() && s[i_l] == s[i_r]) {
				i_l--
				i_r++
			}
			
			i_r--
			i_l++
			
			if(i_r-i_l > last-first) {
				last = i_r
				first = i_l
			}
		}
		
		// Look for palindromes with even length starting from the center (between two chars)
		// i = the first character to the left of the center
		for(int i = 1; i < string.length() - 1; i++) {
			int i_l = i, i_r = i+1
			
			while(i_l >= 0 && i_r < string.length() && s[i_l] == s[i_r]) {
				i_l--
				i_r++
			}
			
			i_r--
			i_l++
			
			if(i_r-i_l > last-first) {
				last = i_r
				first = i_l
			}
		}
		
		return string.substring(first, last+1)
	}
}
