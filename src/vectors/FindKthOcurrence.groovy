package vectors

import spock.lang.Specification
import spock.lang.Unroll

class FindKthOcurrence extends Specification {
	
	/*
		Given a sorted array, find a way to find the # of occurrence of a number 
		for eg: [1, 1, 2, 3, 3, 3, 4, 5] 
		find # of occurrence of 3 in time better than O(n)

		source: http://www.careercup.com/question?id=5647476964982784
	 */
	
	@Unroll
	def "the #k th occurence of #number in #numbers is #result"() {
		expect:
		findKthOcurrence(numbers, number, k) == result
	
		where:
		numbers			| number	| k	| result
		[1, 2, 3]		| 1			| 1	| 0
		[1, 2, 3]		| 2			| 1	| 1
	                	        	
		[1, 2, 3]		| 4			| 1	| -1
		[1, 2, 3]		| 1			| 2	| -1
	                	        	
		[1, 1, 2, 2, 3]	| 1			| 1	| 0
		[1, 1, 2, 2, 3]	| 1			| 2	| 1
		[1, 1, 2, 2, 3]	| 2			| 1	| 2
		[1, 1, 2, 2, 3]	| 2			| 2	| 3
		[1, 1, 2, 2, 3]	| 3			| 1	| 4
	}
	
	int findKthOcurrence(List<Integer> numbers, int number, int k) {
		int firstOcurrence = findFirstOcurrence(numbers, number, 0, numbers.size() - 1);
	
		if(firstOcurrence == -1) {
			return -1;
		}
	
		int indexOfK = firstOcurrence + k - 1;
	
		if(numbers.get(indexOfK) == number) {
			return indexOfK;
		} else {
			return -1;
		}
	}
	
	int findFirstOcurrence(List<Integer> numbers, int number, int left, int right) {
		if(left > right) {
			return -1;
		}
	
		int mid = left + (right - left)/2;
	
		if(numbers.get(mid) == number) {
			if(mid == 0 || numbers.get(mid-1) != number) {
				return mid;
			} else {
				return findFirstOcurrence(numbers, number, left, mid-1);
			}
		} else if(number < numbers.get(mid)){
			return findFirstOcurrence(numbers, number, left, mid-1);
		} else {
			return findFirstOcurrence(numbers, number, mid+1, right);
		}
	}
}
