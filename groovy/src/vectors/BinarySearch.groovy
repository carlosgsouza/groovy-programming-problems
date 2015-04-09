package vectors

import spock.lang.Specification

class BinarySearch extends Specification {

	def run() {
		expect:
		binarySearch(a, e) == i
		
		where:
		a					| e		| i
		[0, 2, 4]			| 2		| 1
		[0, 2, 4]			| 3		| null
		1..100				| 38	| 37
		1..100				| 1		| 0
		1..100				| 99	| 98 
	}
	
	
	Integer binarySearch(List<Integer> a, Integer e) {
		int left = 0;
		int right = a.size() -1;
		
		while(left < right) {
			int mid = left + (right - left)/2;
			
			if(a[mid] == e) {
				return mid;
			} else if(a[mid] > e) {
				right = mid - 1;
			} else { // a[mid] < e 
				left = mid + 1;
			}
		}
		
		return null;
	}
}
