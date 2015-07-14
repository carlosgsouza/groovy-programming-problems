package logic

import spock.lang.Specification
import spock.lang.Unroll

class InPlaceSwap extends Specification {

	@Unroll
	def "(#a, #b) shoulbd become (#b, #a)"(int a, int b) {
		given:
		int a0 = a
		int b0 = b
		
		def swap = {
			a = a+b
			b = a-b //(a+b)-b=a
			a = a-b //(a+b)-((a+b)-b) = a+b -a-b+b = b
		}
		
		when:
		swap()
		
		then:
		a == b0
		b == a0
		
		where:
		a	| b
		1	| 2
		2	| 1
		-1	| 0
		0	| 1
		-3	| -4
		100	| -100
		0	| 0
	}
}
