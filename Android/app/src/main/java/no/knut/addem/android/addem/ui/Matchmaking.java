package no.knut.addem.android.addem.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import no.knut.addem.android.addem.R;
import no.knut.addem.android.addem.api.MatchmakingAPI;
import no.knut.addem.android.addem.transport.BoardResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Matchmaking extends ActionBarActivity {

    public final static long USER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchmaking);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.0.15:9000")
                .build();

        MatchmakingAPI matchmakingAPI = restAdapter.create(MatchmakingAPI.class);

        matchmakingAPI.getNewMatch(USER_ID, new Callback<BoardResponse>() {
            @Override
            public void success(BoardResponse boardResponse, Response response) {
                Log.d("HOJA", boardResponse.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("ERROOROROS", error.toString());
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matchmaking, menu);
        return true;
    }
    //"http://192.168.10.100:9000/matchmaking/"



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
