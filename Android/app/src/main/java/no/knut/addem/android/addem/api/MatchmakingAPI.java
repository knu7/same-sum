package no.knut.addem.android.addem.api;


import no.knut.addem.android.addem.transport.BoardResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface MatchmakingAPI {

    @GET("/matchmaking/{userId}")
    void getNewMatch(@Path("userId") long userId, Callback<BoardResponse> response);

}
