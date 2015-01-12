package services.interfaces;

import models.Game;

public interface IMatchmaking {

	Game getNewGame(long userId);
}
