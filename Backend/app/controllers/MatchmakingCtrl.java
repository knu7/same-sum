package controllers;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.Game;
import models.Player;
import models.response.BoardResponse;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import models.Number;
import services.MatchmakingSocket;
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
    
    public static WebSocket<String> ws() {
        return new WebSocket<String>() {

            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {

                // For each event received on the socket,
                in.onMessage(new Callback<String>() {
                    public void invoke(String event) {

                        // Log events to the console
                        System.out.println(event);

                    }
                });

                // When the socket is closed.
                in.onClose(new Callback0() {
                    public void invoke() {

                        System.out.println("Disconnected");

                    }
                });

                // Send a single 'Hello!' message
                out.write("Hello!");

            }

        };
    }

}
