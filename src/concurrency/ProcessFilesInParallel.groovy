package concurrency

import spock.lang.Specification

/*
 How can we divide a large file between multi threads to process it? If we are running a multi threaded application and input 
 is a large file and we have to provide each thread a part of the file to make it fast. How we can achieve it in java?
 
 http://www.careercup.com/question?id=5653584985194496
 */
class ProcessFilesInParallel extends Specification {
	
	static Integer lines = 3000000
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
