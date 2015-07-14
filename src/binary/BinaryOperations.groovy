package binary

import spock.lang.Specification
import spock.lang.Unroll


class BinaryOperations extends Specification {
	
	@Unroll
	def "representing #intValue as binary"(byte intValue, byte binValue) {
		expect:
		intValue == binValue
		
		where:
		intValue	| binValue
		0			| 0b0
		1			| 0b1
		2			| 0b10
		3			| 0b00000011
		32			| 0b100000
	}
	
	def "shift operation"() {
		expect:
		0b00000001 << 3 == 0b00001000
		0b00001000 >> 3 == 0b00000001
		
		and: "logical shift to move the sign bit as well"
		-1 >>> 1 == Integer.MAX_VALUE
		
		and: "shift operator to work as a power of 2 multiplier"
		1 << 0 == 1
		1 << 1 == 2
		1 << 3 == 8
		2 >> 1 == 1
		
		and: "producing strings with 1"
		~0 == -1
		~(~0 << 3) == 0b00000111 
	}
	
	def "xor operation"() {
		expect:
		(0b11110000 ^ 0b00000000) == 0b11110000
		(0b11110000 ^ 0b11111111) == 0b00001111
		(0b11110000 ^ 0b11110000) == 0b00000000
		(0b11110000 ^ 0b00001111) == 0b11111111
	}
	
	@Unroll
	def "getBit(#value, #i) == #result"() {
		given:
		def getBit = { byte value, int i ->
			(value & (1 << i)) == 0 ? 0 : 1 
		}
		
		expect:
		getBit((byte)value, i) == result
		
		where:
		value		| i	| result
		0b00000000	| 0	| 0
		0b00000000	| 1	| 0
		0b00000000	| 2	| 0
		0b11111111	| 0	| 1
		0b11111111	| 1	| 1
		0b11111111	| 2	| 1
		0b01010101	| 0	| 1
		0b01010101	| 1	| 0
		0b01010101	| 2	| 1
		0b01010101	| 3	| 0
	}
	
	@Unroll
	def "setBit(#value, #i) == #result"() {
		given:
		def setBit = { byte value, int i ->
			(byte)(value | (1 << i)) 
		}
		
		expect:
		setBit((byte)value, i) == result
		
		where:
		value		| i	| result
		0b00000000	| 0	| 0b00000001
		0b00000000	| 1	| 0b00000010
		0b00000000	| 2	| 0b00000100
		
		0b01111111	| 0	| 0b01111111
		0b01111111	| 1	| 0b01111111
		0b01111111	| 2	| 0b01111111
	}
	
	@Unroll
	def "clearBit(#value, #i) == #result"() {
		given:
		def clearBit = { byte value, int i ->
			(byte)(value & ~(1 << i))
		}
		
		expect:
		clearBit((byte)value, i) == result
		
		where:
		value		| i	| result
		0b00000000	| 0	| 0b00000000
		0b00000000	| 1	| 0b00000000
		0b00000000	| 2	| 0b00000000
		
		0b01111111	| 0	| 0b01111110
		0b01111111	| 1	| 0b01111101
		0b01111111	| 2	| 0b01111011
	}
	
	@Unroll
	def "#intValue is the complement of -(#intValue) plus one"(byte intValue, byte binValue) {
		expect:
		intValue == binValue
		
		where:
		intValue	| binValue
		-1			| 0b11111111
		-30			| 0b11100010
	}
	
	@Unroll
	def "(#A & #B) == #result"(byte A, byte B, byte result) {
		expect:
		(A & B) == result
		
		where:
		A			| B				| result
		0b11111111	| 0b11111111	| 0b11111111
		0b11111111	| 0b00000000	| 0b00000000
		0b11111111	| 0b00001111	| 0b00001111
	}
	
	def "negative numbers can be added to positive numbers seamlessly"() {
		given:
		byte positive6 = 0b00000110
		byte negative3 = 0b11111101
		//               ----------
		byte positive3 = 0b00000011
		
		expect:
		positive6 + negative3 == positive3
	}
	
	def "zeroing N digits of a binary number"() {
		expect:
		(0b11111111 & (~0 << 4)) == 0b11110000
	}
	
	def "using a BitSet"() {
		when:
		BitSet bitSet = new BitSet()
		
		then:
		bitSet.length() == 0
		
		when:
		bitSet.set(0, true)
		bitSet.set(1, true)
		bitSet.set(2, true)
		
		then:
		bitSet.get(0) == true
		bitSet.get(1) == true
		bitSet.get(2) == true
		
		and:
		bitSet.length() == 3
		
		when:
		bitSet.set(3, false)
		bitSet.set(4, false)
		bitSet.set(5, false)
		
		then:
		bitSet.get(3) == false
		bitSet.get(4) == false
		bitSet.get(5) == false
		
		and:
		bitSet.length() == 3
		
		when:
		bitSet.set(1000, true)
		
		then:
		bitSet.get(1000) == true
		
		and:
		bitSet.length() == 1001
		
		and:
		bitSet.get(2345) == false
	}
	
}
