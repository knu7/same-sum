package controllers;

import java.util.Set;

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
    	
    	Game game = matchMaking.getNewGame(userId);
    	
    	if (game == null){
    		return ok();
    	}
    	
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
    	
    	
        return ok(Json.toJson(response));
    }

}
