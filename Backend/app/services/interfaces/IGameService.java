package services.interfaces;

import java.util.Set;

import models.Game;
import models.Number;
import models.Player;


public interface IGameService {

	
	Number[][] getCurrentBoard(long matchId, long userId);
	
	Game addNewGame(Set<Player> players, int numberOfRounds);
	
}
