package models;

import java.util.Map;

public class Round {
	
	private int playerCount;
	private Number[][] board;
	private Map<Long, Solution> solutions;
	
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
		
		
		return new RoundResult();
		
		
	}
	
}
