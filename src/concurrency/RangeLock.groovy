package concurrency

import java.util.Collections.SynchronizedList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.Semaphore
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import spock.lang.Specification
import spock.lang.Unroll

class RangeLock extends Specification {
	
	def "should not block if ranges do not intersect"() {
		given:
		MyRangeLock lock = new MyRangeLock()
		
		expect:
		lock.lock(1, 1)
		lock.lock(2, 2)
		lock.lock(10, 20)
		lock.lock(21, 30)
		
		lock.unlock(1, 1)
		lock.unlock(2, 2)
		lock.unlock(10, 20)
		lock.unlock(21, 30)
		
		and:
		lock.lock(5, 6)
		lock.unlock(5, 6)
		
		lock.lock(6, 7)
		lock.unlock(6, 7)
	}
	
	def "should lock on ranges"() {
		given:
		MyRangeLock lock = new MyRangeLock()
		
		and:
		lock.lock(10, 20)
		
		when:
		boolean t_10_15_completed = false
		Thread t_10_15 = Thread.start { 
			println "t_10_15 started"
			
			lock.lock(10, 15)
			t_10_15_completed = true 
			
			println "t_10_15 completed"
		}
		
		boolean t_16_20_completed = false
		Thread t_16_20 = Thread.start {
			println "t_16_20 started"
			 
			lock.lock(10, 15)
			t_16_20_completed = true 
			
			println "t_16_20 completed" 
		}
		
		then:
		lock.blockedRanges.size() == 2
		
		and:
		t_10_15_completed == false
		t_16_20_completed == false
		
		when:
		lock.unlock(0, 0)
		
		t_10_15.join()
		t_16_20.join()
		
		then:
		t_10_15 == true
		t_16_20 == true
	}
	
	def "should lock on a range"() {
		given:
		MyRangeLock lock = new MyRangeLock()
		
		and:
		ExecutorService pool = Executors.newFixedThreadPool(100)
				
		and:
		List<Future> futures = []
		List<Integer> completed = new SynchronizedList<Integer>([])
		
		when: "each thread, t_i, 0 <= i < 100, lock on range (i, i+1)"
		100.times { i ->
			futures << pool.submit {
				lock.lock(i, i+1)
				sleep(100)
				lock.unlock(i, i+1)
			
				completed << i
			}
			
		}
				
		and: "waits for all thread to finish"
		futures*.get()
		
		then: "all threads complete"
		completed.size() == 100
		
		and: "t_i where i is even finish before t_i+1, since t_i locks on (i, i+1) before t_i+1"
		def i_index = [:]
		completed.eachWithIndex { i, index ->
			i_index[i] = index
		}
		
		i_index.findAll{ i, index -> i%2 == 0}.every{ i, index -> i_index[i] < i_index[i+1] } 
	}
	
	@Unroll
	def "isInTheRange(#number, (#start, #finish)) should return #result" () {
		given:
		MyRangeLock lock = new MyRangeLock()
		
		Range range = new Range(start:start, finish:finish)
		
		expect:
		lock.isInTheRange(number, range) == result
		
		where:
		start	| finish	| number	| result
		1		| 3			| 0			| false
		1		| 3			| 1			| true
		1		| 3			| 2			| true
		1		| 3			| 3			| true
		1		| 3			| 4			| false
		1		| 1			| 1			| true
	}
	
	
	
	@Unroll
	def "given that (10,20) and (30,40) are locked, isLocked(#range) should return #result" () {
		given:
		MyRangeLock lock = new MyRangeLock()
		
		lock.lock(10, 20)
		lock.lock(30, 40)
		
		expect:
		lock.isLocked(range) == result
		
		where:
		range				| result
		new Range(1, 1)		| false
		new Range(1, 9)		| false
		new Range(1, 10)	| true
		new Range(10, 10)	| true
		new Range(10, 19)	| true
		new Range(10, 20)	| true
		new Range(20, 29)	| true
		new Range(21, 29)	| false
		new Range(21, 30)	| true
		new Range(30, 30)	| true
		new Range(30, 40)	| true
		new Range(41, 41)	| false
		new Range(41, 9999)	| false
		new Range(1, 100)	| true
		new Range(10, 40)	| true
		new Range(10, 41)	| true
		new Range(15, 35)	| true
	}
	
	@Unroll
	def "rangeIntersects(#range1, #range2) and rangeIntersects(#range2, #range1) should return #result" () {
		given:
		MyRangeLock lock = new MyRangeLock()
		
		expect:
		lock.rangesIntersect(range1, range2) == result
		lock.rangesIntersect(range2, range1) == result
		
		where:
		range1				| range2				| result				
		new Range(1, 1)		| new Range(1, 1)		| true
		new Range(1, 1)		| new Range(2, 2)		| false
		new Range(1, 2)		| new Range(2, 3)		| true
		new Range(1, 2)		| new Range(3, 4)		| false
		
		new Range(2, 3)		| new Range(1, 2)		| true
		new Range(2, 3)		| new Range(1, 3)		| true
		new Range(2, 3)		| new Range(1, 4)		| true
		new Range(2, 3)		| new Range(3, 4)		| true
	}
	
	class MyRangeLock {
		
		TreeSet<Range> ranges = new TreeSet<Range>()
		
		Map<Range, Semaphore> blockedRanges = new HashMap<Range, Semaphore>()
		
		void lock(int start, int finish) {
			if(finish < start) {
				throw new IllegalArgumentException("start should <= finish")
			}
			
			Range range = new Range(start, finish)
			
			println "Trying to lock range"
			
			while(isLocked(range)) {
				println "Range $range is locked"
				synchronized(blockedRanges) {
					blockedRanges[range] = new Semaphore(1)
					blockedRanges[range].acquire()
					
					println "Range $range is unlocked now"
				}
			}
			
			synchronized (ranges) {
				ranges.add(range)
			}
			
			println "Locked on $range"
		}
		
		void unlock(int start, int finish) {
			Range range = new Range(start, finish)
			
			println "Unlocking $range"
			
			synchronized (ranges) {
				if(ranges.contains(range)) {
					ranges.remove(range)
				}
			}
			
			synchronized (blockedRanges) {
				blockedRanges.each { blockedRange, semaphore ->
					if(!isLocked(blockedRange)) {
						println "Found blocked range $range"
						
						semaphore.release()
						blockedRanges.remove(semaphore)
					}
				}
			}
		}
		
		boolean isLocked(Range newRange) {
			synchronized (ranges) {
				for(Range existingRange : ranges) {
					if(rangesIntersect(newRange, existingRange)) {
						return true
					}
				}
			}
			
			return false
		}
		
		boolean rangesIntersect(Range r1, Range r2) {
			isInTheRange(r1.start, r2)	||
			isInTheRange(r1.finish, r2)	||
			isInTheRange(r2.start, r1)	||
			isInTheRange(r2.finish, r1)
		}
		
		boolean isInTheRange(int number, Range range) {
			range.start <= number && number <= range.finish
		}
		
		
	}	
	
	class Range implements Comparable<Range> {
		int start
		int finish

		public Range(int start, int finish) {
			this.start = start
			this.finish = finish
		}
		
		@Override
		public int compareTo(Range other) {
			return Integer.compare(start, other.start)
		}
		@Override
		public String toString() {
			"($start, $finish)"
		}
		
		
		
	}

}
