package controllers;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.Game;
import models.Player;
import models.response.BoardResponse;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import models.Number;
import services.interfaces.IMatchmaking;

import com.google.inject.Inject;

public class MatchmakingCtrl extends Controller {
	
	@Inject
	private IMatchmaking matchMaking;
	
    public Result getMatch(long userId) {
    	
    	Logger logger = LoggerFactory.getLogger("matchmaking");
    	logger.debug("getMatch triggered");
    	Game game = matchMaking.getNewGame(userId);
    	
    	if (game == null){
    		logger.debug("No match");
    		return ok();
    	}
    	
    	logger.debug("Got match");
    	
    	Set<Player> players = game.getPlayers();
    	String[] opponents = new String[players.size()-1];
    	int index = 0;
    	for (Player player : players){
    		if (player.getId() != userId){
    			opponents[index] = player.getName();
    			index++;
    		}
    	}
    	
    	BoardResponse response = new BoardResponse(game.getId(), opponents , game.getRounds()[0].getBoard());
    	
    	logger.debug("returning match " + response.toString());
        return ok(Json.toJson(response));
    }

}
