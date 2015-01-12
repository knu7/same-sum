package models;

import java.util.Set;

public class Game {
	
	private long id;
	private Set<Player> players;
	private Round[] rounds;
	private int numberOfRounds;
	private int roundsFinished;
	
	public Game(long id, Set<Player> players, int numberOfRounds) {
		this.players = players;
		this.numberOfRounds = numberOfRounds;
		this.rounds = new Round[numberOfRounds];
	}
	
	public void addRound(Round round){
		rounds[roundsFinished] = round;
	}
	
	public long getId(){
		return id;
	}
	
	public Round getCurrentRound(){
		
		if(rounds.length == 0 || roundsFinished >= rounds.length)
			return null;
		
		if (roundsFinished == 0)
			return rounds[0];
		
		return rounds[roundsFinished - 1];
	}
	
	public Set<Player> getPlayers() {
		return players;
	}
	public Round[] getRounds() {
		return rounds;
	}
	public int getRoundsFinished() {
		return roundsFinished;
	}
	
	
}
