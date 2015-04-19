package strings

import spock.lang.Specification

class GeneratePermutations extends Specification {

	def "should calculate all the permutations of a string"() {
		expect:
		permutations("123") == ["123", "132", "213", "231", "312", "321"]
		
		and:
		permutations("1234").size() == 4*3*2*1
		
		and:
		permutations("12345").size() == 5*4*3*2*1
	}
	
	List permutations(String s) {
		def result = []
		
		char[] chars = s.toCharArray()
		char[] current = new char[chars.length]
		
		boolean[] used = new boolean[chars.size()]
		used.length.times {
			used[it] = false
		}
		
		permutations(result, chars, used, current, 0)
		
		return result
	}
	
	void permutations(List result, char[] chars, boolean[] used, char[] current, int length) {
		if(length == chars.length) {
			result << String.valueOf(current)
			return
		}
		
		chars.length.times { i ->
			if(used[i]) {
				return
			}
			
			used[i] = true
			current[length] = chars[i]
			
			permutations(result, chars, used, current, length+1)
			
			used[i] = false
		}
	}
}
