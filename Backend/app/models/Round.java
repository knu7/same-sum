package models;

import java.util.Map;
import models.Number;

public class Round {
	
	private int playerCount;
	private Number[][] board;
	private Map<Long, Solution> solutions;
	
	public Round(Number[][] board, int playerCount) {
		this.playerCount = playerCount;
		this.board = board;
	}
	public Number[][] getBoard() {
		return board;
	}
	public Map<Long, Solution> getSolutions() {
		return solutions;
	}
	
	public RoundResult submitSolution(long userId, Solution solution){
		
		solutions.put(userId, solution);
		
		if (solutions.size() < playerCount)
			return null;
		
		long winnerId = 0;
		return new RoundResult(winnerId);
		
		
	}
	
}
