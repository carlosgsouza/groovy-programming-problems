package iterators

import spock.lang.Specification

class NegativeIterator extends Specification {
	
	def "should iterate only over negative numbers of a list"() {
		given:
		MyNegativeIterator iterator = new MyNegativeIterator(list)
		
		when:
		def foundNegativeElements = []
		while(iterator.hasNext()) {
			foundNegativeElements << iterator.next()
		}
		
		then:
		foundNegativeElements == negativeElements
		
		where:
		list				| negativeElements
		[]					| []
		[1, 2]				| []
		[1, -1, 2, -2]		| [-1, -2]
		[0, -1, 0, -2]		| [-1, -2]
		[-1, -1, -2, -2]	| [-1, -1, -2, -2]
	}
	
	def "should support consecutive calls to hasNext without changing the next element"() {
		given:
		def list = [-1, -2, -3]
		
		and:
		MyNegativeIterator iterator = new MyNegativeIterator(list)
		
		expect:
		iterator.hasNext() == true
		iterator.hasNext() == true
		iterator.hasNext() == true
		
		and:
		iterator.next() == -1
		
		and:
		iterator.hasNext() == true
		iterator.hasNext() == true
		iterator.hasNext() == true
		
		and:
		iterator.next() == -2
		iterator.next() == -3
		
		and:
		iterator.hasNext() == false
		iterator.hasNext() == false
		iterator.hasNext() == false
	}

	public class MyNegativeIterator implements Iterator<Integer> {
		
			private Iterator<Integer> originalIterator;
		
			private Integer cachedNext = null;
		
			public MyNegativeIterator(Iterable<Integer> iterable) {
				if(iterable == null) {
					throw new IllegalArgumentException("iterable is null");
				}
		
				originalIterator = iterable.iterator();
			}
		
			@Override
			public boolean hasNext() {
				if(cachedNext != null) {
					return true;
				}
		
				while(originalIterator.hasNext()) {
					Integer next = originalIterator.next();
		
					if(next < 0) {
						cachedNext = next;
						return true;
					}
				}
		
				return false;
			}
		
			@Override
			public Integer next() {
				if(hasNext() == false) {
					throw new IllegalStateException();
				}
		
				Integer result = cachedNext;
				cachedNext = null;
				return result;
			}
		}
}
