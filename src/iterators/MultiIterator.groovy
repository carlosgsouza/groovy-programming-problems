package iterators

import spock.lang.Specification
import spock.lang.Unroll


/*
Question from CareerCup
 
Write a method that combines an array of iterators into a new one, such that, e.g. for input [A B C] where: 
A.next() returns a1, a2, respectively; 
B.next() returns b1; 
C.next() returns c1, c2, c3, respectively; 

The new iterator will return elements in this order: a1 b1 c1 a2 c2 c3.
 
http://www.careercup.com/question?id=5646591618711552
*/
class MultiIterator extends Specification {
	
	@Unroll
	def "given the collections #collections, MultiIterator should iterate over the following elements: #elements"(List<Collection> collections, List elements) {
		given:
		List<Iterator> iterators = collections.collect{ it.iterator() }
		
		and:
		MultiIteratorImpl multiIterator = new MultiIteratorImpl(iterators)
		
		when:
		List iteratedElements = []
		while(multiIterator.hasNext()) {
			iteratedElements << multiIterator.next()
		}
		
		then:
		iteratedElements == elements
		
		where:
		collections				| elements
		[]						| []
		[[1], [2]]				| [1, 2]
		[[1, 2], [3, 4]]		| [1, 2, 3, 4]
		[[1, 2], [3, 4], [5,6]]	| [1, 2, 3, 4, 5, 6]
		[[], [], []]			| []
		[[1, 2, 3], []]			| [1, 2, 3]
		[[], [1, 2, 3], []]		| [1, 2, 3]
		[[null], [1, 2, 3]]		| [null, 1, 2, 3]
	}
	
	def "should not iterate twice if the same iterator is used twice"() {
		given:
		List list = [1, 2]
		
		and:
		Iterator iterator = list.iterator()
		
		and:
		MultiIteratorImpl multiIterator = new MultiIteratorImpl([iterator, iterator])
		
		expect:
		multiIterator.next() == 1
		multiIterator.next() == 2
		
		multiIterator.hasNext() == false
	}
	
	@Unroll
	def "should not create a multi iterator with the invalid iterators: #iterators"() {
		when:
		new MultiIteratorImpl(iterators)
		
		then:
		thrown IllegalArgumentException
		
		where:
		iterators << [
			null,
			[null],
			[[1,2,3].iterator(), null, [4,5,6].iterator()]	
		]
	}
	
	def "should remove elements of collections"() {
		given:
		List listA = [1,2,3]
		List listB = []
		List listC = [4,5,6]
		
		and:
		List<Iterator> iterators = [listA.iterator(), listB.iterator(), listC.iterator()]
		
		and:
		MultiIteratorImpl multiIterator = new MultiIteratorImpl(iterators)
		
		expect:
		multiIterator.hasNext() == true
		multiIterator.next() == 1
		multiIterator.next() == 2
		multiIterator.remove()
		multiIterator.next() == 3
		multiIterator.next() == 4
		multiIterator.remove()
		multiIterator.next() == 5
		multiIterator.hasNext() == true
		
		and:
		listA == [1,3]
		listB == []
		listC == [5,6]
	}
	
	public class MultiIteratorImpl<T> implements Iterator<T> {
		
		private List<Iterator<T>> iterators;
		private int current = 0;
	
		public MultiIteratorImpl(List<Iterator<T>> iterators) {
			if(iterators == null) {
				throw new IllegalArgumentException("Iterators should not be null");
			}
			for(Iterator<T> i : iterators) {
				if(i == null) {
					throw new IllegalArgumentException("Iterators should not be null");
				}
			}
	
			this.iterators = iterators;
		}
		
		public boolean hasNext() {
			moveToNextIteratorWithElements();
	
			return (current < iterators.size());
		}
	
		public T next() {
			if(hasNext() == false) {
				// What is the right exception to be thrown here?
				throw new IllegalStateException("There are no more elements");
			}
	
			return iterators[current].next();
		}
	
		public void remove() {
			if(hasNext() == false) {
				// What is the right exception to be thrown here?
				throw new IllegalStateException("There are no more elements");
			}
	
			iterators[current].remove();
		}
	
		private void moveToNextIteratorWithElements() {
			while(current < iterators.size() && iterators[current].hasNext() == false) {
				current++;
			}
		}
	}
}
