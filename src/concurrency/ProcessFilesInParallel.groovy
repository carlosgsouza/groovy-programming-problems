package concurrency

import spock.lang.Specification

class ProcessFilesInParallel extends Specification {
	
	static Integer lines = 30000
	static File file
	
	def setupSpec() {
		file = new File("ProcessFilesInParallel.txt")
		if(file.exists()) {
			file.delete()
		}
		
		def writer = new BufferedWriter(new FileWriter(file.absoluteFile))
		lines.times {
			writer.write(it.toString())
			writer.write("\n")
		}
		writer.close()
	}
	
	def cleanupSpec() {
		file?.delete()
	}
	
	def "should process a big file in parallel"() {
		given:
		Integer threads = 3
		int batchSize = lines / threads 
		
		and: "three readers, each reading a third of the file"
		List<Reader> readers = []
		threads.times { i ->
			readers << new Reader(file:file, startLine:i*batchSize, count:batchSize)
		} 
		
		when: "the threads are started"
		readers.each {
			it.start()
		}
		
		and: "we wait for completion"
		readers.each {
			it.join()
		}
		
		then:
		readers.every {
			it.lines == it.startLine..<(it.startLine + it.count)
		}
		
		and:
		readers.inject(0) { count, reader -> count + reader.lines.size() } == lines
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
		
		public String toString() {
			return "Reader(${startLine}...${startLine+count})"
		}
		
		
	}

}
