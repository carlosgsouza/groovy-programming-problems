package logic

import spock.lang.Specification

class NumberOfTimesEachDigitOccurs extends Specification {

	/*

In a book with N pages, pages are numbered from 1 to N. Find out how many times 
each digit occurs in that book. 


You are expected to complete the function getDigitslnBook, which takes an integer as input and 
prints how many times each digits occur, one in a line. 

The Nth line in the output denotes how many times the integer N-1 occurs in page numbers. 

http://www.careercup.com/question?id=6702347652694016
	 */
	
	
	def run() {
		expect:
		getNumberOfOcurrencesOfDigits(N) == result
		
		where:
		N	| result
		1	| [0:0, 1:1, 2:0, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0]
		4	| [0:0, 1:1, 2:1, 3:1, 4:1, 5:0, 6:0, 7:0, 8:0, 9:0]
		14	| [0:1, 1:3, 2:2, 3:2, 4:2, 5:1, 6:1, 7:1, 8:1, 9:1]
		23	| [0:2, 1:4, 2:4, 3:3, 4:2, 5:2, 6:2, 7:2, 8:2, 9:2]
		123	| [0:13, 1:16, 2:15, 3:14, 4:13, 5:13, 6:13, 7:13, 8:13, 9:13]
	}
	
	
	def getNumberOfOcurrencesOfDigits(int N) {
		def result = [0:0, 1:0, 2:0, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0]
		
		def addToAll = getNumberOfOcurrencesOfDigits(N, result)
		
		(0..9).each {
			result[it] += addToAll
		}
		
		return result
	}
	
	def getNumberOfOcurrencesOfDigits(int N, result) {
		int Nby10 = N / 10
		int Nmod10 = N % 10
		
		(1..Nmod10).each {
			result[it]++ 
		}
		
		if(Nby10 > 0) {
			return Nby10 + getNumberOfOcurrencesOfDigits(Nby10, result)			
		} else {
			return 0
		}
	}
}
