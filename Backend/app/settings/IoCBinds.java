package settings;

import services.BoardService;
import services.GameService;
import services.Matchmaking;
import services.interfaces.IBoardService;
import services.interfaces.IGameService;
import services.interfaces.IMatchmaking;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class IoCBinds extends AbstractModule{
	
	@Override
	protected void configure() {
		bind(IMatchmaking.class).to(Matchmaking.class).in(Scopes.SINGLETON);
		bind(IBoardService.class).to(BoardService.class).in(Scopes.SINGLETON);
		bind(IGameService.class).to(GameService.class).in(Scopes.SINGLETON);
	}
}
