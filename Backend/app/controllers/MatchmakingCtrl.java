package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class MatchmakingCtrl extends Controller {

    public static Result getMatch(long userId) {
        return ok(index.render("Your new application is ready."));
    }

}