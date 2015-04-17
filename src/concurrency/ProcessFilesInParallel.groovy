package concurrency

import spock.lang.Specification

class ProcessFilesInParallel extends Specification {
	
	def "should process a big file in parallel"() {
		given:
		Integer lines = 3000
				
		and:
		File file = new File("the_file.txt")
		if(file.exists()) {
			file.delete()
		}
		lines.times {
			file << it + "\n"
		}
		
		and: "three readers, each reading a third of the file"
		Reader r1 = new Reader(file:file, startLine:0, 	  count:1000)
		Reader r2 = new Reader(file:file, startLine:1000, count:1000)
		Reader r3 = new Reader(file:file, startLine:2000, count:1000)
		
		when: "the threads are started"
		r1.start()
		r2.start()
		r3.start()
		
		and: "we wait for completion"
		r1.join()
		r2.join()
		r3.join()
		
		then:
		r1.lines ==    0.. 999
		r2.lines == 1000..1999
		r3.lines == 2000..2999
		
		cleanup:
		file?.delete()
	}
	
	class Reader extends Thread {
		List<Integer> lines = []
		File file
		int startLine
		int count
				
		public void run() {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
			moveToStart(bufferedReader)
			process(bufferedReader)
		}

		private process(BufferedReader bufferedReader) {
			int i = 0
			while(i++ < count) {
				String line = bufferedReader.readLine()
				if(line == null) {
					return
				}
				lines << line.toInteger()
			}
		}

		private moveToStart(BufferedReader bufferedReader) {
			int i = 0
			while(i++ < startLine) {
				String line = bufferedReader.readLine()
				if(line == null) {
					return
				}
			}
			
		}
		
		
	}

}
