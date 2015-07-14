package concurrency

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import spock.lang.Specification


class BasicConcurrency extends Specification {
	
	def "using a fixed thread pool"() {
		given:
		Integer counter = 0;
		int threads = 10;
		int cycles = 10000;
		
		ExecutorService tp = Executors.newFixedThreadPool(threads);
						
		10.times { t ->
			tp.submit(new Thread() {
				@Override
				public void run() {
					cycles.times {
						// Unsing class as monitor so all instances of this Thread will synchronize
						// Can't use counter as monitor. Since every time you increment an Integer, you get a new object, threads would not synchronize
						synchronized(BasicConcurrency.class) {
							counter++;
						}
					}
					println "Finished $t"
				}
			});
		}
		
		tp.shutdown()
		tp.awaitTermination(60, TimeUnit.SECONDS)
		tp.shutdownNow()
		
		expect:
		counter == threads * cycles;
	}
}
