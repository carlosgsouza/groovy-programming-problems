package binary

import spock.lang.Specification
import spock.lang.Unroll


public class GreyCode extends Specification {

	@Unroll
	def "the grey representation of #binary should be #grey"() {
		expect:
		toGrey(binary) == grey

		where:
		binary		| grey
		0b00000000	| 0b00000000
		0b00000001	| 0b00000001
		0b00000010	| 0b00000011
		0b00000011	| 0b00000010
		0b00001011	| 0b00001110
	}

	/*
	value	div	remainder	xor(remainder, previousRemainder)	multiplier 	added
    11		5	1 			0 									1 			0
     5		2	1 			1 									2 			2
     2		1 	0 			1 									4 			4
     1		0 	1 			1* 									8 			8

	*/

     private static class PartialResult {
     	int previousRemainder;
     	int value;
     }

     int toGrey(int value) {
     	PartialResult R = new PartialResult();
     	toGrey(R, value, 1);
     	return R.value;
     }

     void toGrey(PartialResult R, int value, int multiplier) {
     	int div = value / 2;
     	int remainder = value % 2;

     	if(div == 0) {
     		// The most significant bit is the same as binary
     		R.value = remainder * multiplier;
     		R.previousRemainder = remainder;

     		return;
     	}

     	toGrey(R, div, multiplier * 2);

     	int bit = remainder ^ R.previousRemainder;

     	R.value += bit * multiplier;
     	R.previousRemainder = remainder;
     }

}

