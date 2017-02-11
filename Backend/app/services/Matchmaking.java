package services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import models.Game;
import models.Player;
import models.Round;
import models.Number;
import services.interfaces.IBoardService;
import services.interfaces.IGameService;
import services.interfaces.IMatchmaking;

import com.google.inject.Inject;

public class Matchmaking implements IMatchmaking{

	@Inject
	IBoardService boardService;
	@Inject
	IGameService gameService;
	private Queue<Player> playerQueue;
	private Map<Integer, Game> activeGames;
	
	
	public Matchmaking(){
		playerQueue = new ConcurrentLinkedQueue<Player>();
		activeGames = new HashMap<Integer, Game>();
	}
	
	@Override
	public Game getNewGame(long userId) {
		
		if (isInQueue(userId)){
			return null;
		}
		
		Player player = new Player(userId, "Guest " + userId);
		if (playerQueue.isEmpty()){
			playerQueue.add(player);
			return null;
		}
		else{
			Player opponent = playerQueue.poll();
			Set<Player> players = new HashSet<>(2);
			players.add(player);
			players.add(opponent);
			Game game = gameService.addNewGame(players, 1);
			Round firstRound = new Round(boardService.getNewBoard(4, 4, 12), 2);
			game.addRound(firstRound);
			return game;
		}
		
		
	}
	
	private boolean isInQueue(long userId){
		
		for(Player player : playerQueue){
			if(player.getId() == userId)
				return true;
		}
		
		return false;		
	}

}
