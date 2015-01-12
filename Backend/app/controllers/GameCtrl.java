package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import models.Number;
import services.interfaces.IGameService;

import com.google.inject.Inject;

public class GameCtrl extends Controller{

	@Inject
	IGameService gameService;
	
	public Result getCurrentRound(long matchId, long userId){
		
		Number[][] board = gameService.getCurrentBoard(matchId, userId);
		return ok(Json.toJson(board));
	}
}
