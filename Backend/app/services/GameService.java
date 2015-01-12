package services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import models.Game;
import models.Number;
import models.Player;
import services.interfaces.IGameService;

public class GameService implements IGameService{
	
	private Map<Long, Game> activeGames;
	private long nextId;
	
	public GameService(){
		activeGames = new HashMap<>();
		nextId = 1;
	}
	
	@Override
	public Number[][] getCurrentBoard(long matchId, long userId) {
		
		Game game = activeGames.get(matchId);
		if (game == null)
			return null;
		
		return game.getCurrentRound().getBoard();
		
	}

	@Override
	public Game addNewGame(Set<Player> players, int numberOfRounds) {
		Game game = new Game(nextId, players, numberOfRounds);
		activeGames.put(nextId, game);
		nextId++;
		return game;
	}

}
