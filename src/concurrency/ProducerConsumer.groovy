package concurrency

import spock.lang.Specification

class ProducerConsumer extends Specification {
	
	def "should produce and consume in parallel withtout exploding or busywaiting"() {
		given:
		int itemsProduced = 1000
		int bufferSize = 10
		
		and:
		def buffer = new Buffer(bufferSize)
		
		and:
		def consumer = new Consumer(buffer:buffer, numbers:itemsProduced)
		def producer = new Producer(buffer:buffer, numbers:itemsProduced)
		
		when:
		producer.start()
		consumer.start()
		
		and:
		producer.join()
		consumer.join()
		
		then:
		consumer.consumed.sort() == 0..<itemsProduced
	}
	
	class Buffer {
		int[] values
		int size
		
		Buffer(int size) {
			this.size = 0
			values = new int[size]
		}
		
		boolean isFull() {
			size == values.length
		}
		
		boolean isEmpty() {
			size == 0
		}
		
		synchronized void add(int value) {
			println "Adding $value"
			values[size++] = value
		}
		
		synchronized int next() {
			int result = values[--size]
			println "Removing $result"
			return result
		}
	}
	
	class Consumer extends Thread {
		Buffer buffer
		List consumed = []
		int numbers
		
		void run() {
			synchronized(buffer) {
				while(consumed.size() < numbers) {
					if(buffer.empty) {
						println "Buffer empty"
						buffer.wait()
					}
					
					while(!buffer.empty) {
						consumed << buffer.next()
					}
					buffer.notify()
				}
			}
		} 
	}
	
	class Producer extends Thread {
		Buffer buffer
		int numbers
		
		void run() {
			int n = 0
			
			synchronized(buffer) {
				while(n < numbers) {
					if(buffer.full) {
						println "Buffer full"
						buffer.wait()
					}
					
					while(!buffer.full && n < numbers) {
						buffer.add(n++)
					}
					buffer.notify()
				}
			}
		}
	}

}
