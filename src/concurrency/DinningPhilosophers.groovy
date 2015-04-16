package concurrency

import spock.lang.Specification

class DinningPhilosophers extends Specification {
	
	int seats = 4
	def forks = 0..<seats
	boolean dinnerIsOver = false
	
	def "all philosophers should eat about the same amount"() {
		given: "the philosophers"
		List<Philosopher> philosophers = []
		seats.times{ 
			philosophers << new Philosopher(it, it, (it+1)%seats)
		}
		
		when: "you tell them to start eating"
		philosophers.each { 
			it.start()
		}
		
		and: "wait a little bit and tell them dinner is over"
		sleep(3000)
		dinnerIsOver = true

		def totalEaten = 0
		philosophers.each {
			println it;
			totalEaten += it.bites
		}
				
		then: "they will have eaten"
		totalEaten > 0
		
		and: "none of them ate too much or too little"
		int minimumEaten = (totalEaten / seats) * 0.5
		int maximumEaten = (totalEaten / seats) * 2
		
		philosophers*.bites.every{ it > minimumEaten }
		philosophers*.bites.every{ it < maximumEaten }
		
	}
	
	class Food {
		int remainingBites
	}
	
	class Philosopher extends Thread {
		int leftFork
		int rightFork
		int number
		int bites
				
		Philosopher(int number, int forkA, int forkB) {
			this.number = number
			this.leftFork = (number%2==0) ? forkA : forkB
			this.rightFork = (number%2==1) ? forkA : forkB
		}
		
		void run() {
			while(dinnerIsOver == false) {
				synchronized(forks[leftFork]) {
					println "Philosopher $number grabbed fork $leftFork"
					synchronized(forks[rightFork]) {
						println "Philosopher $number grabbed fork $rightFork"
						println "Philosopher $number ate"
						bites++
					}
				}
			}
		}
		
		String toString() {
			"Philosopher $number ate $bites bites"
		}
		
	}

}
