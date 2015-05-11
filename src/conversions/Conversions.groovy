package conversions

import spock.lang.Specification
import spock.lang.Unroll

class Conversions extends Specification {

	@Unroll
	def "int #intValue, when converted to a binary string is #binaryValue"(){
		expect:
		toBinary(intValue) == binaryValue
		
		where:
		intValue	| binaryValue
		0			| '00000000000000000000000000000000'
		1			| '00000000000000000000000000000001'
		2			| '00000000000000000000000000000010'
		13			| '00000000000000000000000000001101'
		123456		| '00000000000000011110001001000000'
		-1			| '11111111111111111111111111111111'
		-3			| '11111111111111111111111111111101'
		-53			| '11111111111111111111111111001011'
		
	}
	
	@Unroll
	def "the string '#stringValue' corresponds to the integer '#intValue'"() {
		expect:
		toInteger(stringValue) == intValue
		
		where:
		stringValue	| intValue
		"0"			| 0
		"00"		| 0
		"1"			| 1
		"01"		| 1
		"11"		| 11
		"123456"	| 123456
		"-0"		| 0
		"-00"		| 0
		"-1"		| 1
		"-01"		| 1
		"-11"		| 11
		"-123456"	| 123456
	}
	
	int toInteger(String s) {
		int result = 0
		
		char[] c = s.toCharArray()
		
		boolean negative = (c[0] == '-')
		int firstDigit = negative ? 1 : 0
		
		int multiplier = 1
		for(int i = c.length - 1; i >= firstDigit; i--) {
			int digit = c[i] - ('0' as char)
			result += digit * multiplier
			
			multiplier *= 10
		}
		
		return result
	}
	
	String toBinary(int n) {
		boolean positive = (n >= 0)
		
		char[] c = new char[32]
		32.times {
			c[it] = positive ? '0' : '1'
		}
		
		if(!positive) {
			n = -n
			n--
		}
		
		int i = 31
		while(n > 0) {
			int digit = positive ? n%2 : (n+1)%2
			
			c[i--] = (digit == 0) ? '0' : '1' 
			n = n / 2
		}
		
		return new String(c)
	}
}
