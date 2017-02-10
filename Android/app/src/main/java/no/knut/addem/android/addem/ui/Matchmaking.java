package no.knut.addem.android.addem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import no.knut.addem.android.addem.R;
import no.knut.addem.android.addem.api.MatchmakingAPI;
import no.knut.addem.android.addem.core.Board;
import no.knut.addem.android.addem.transport.BoardResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Matchmaking extends ActionBarActivity implements View.OnClickListener{

    public final static long USER_ID = 1;

    private TextView responseText;
    private Button playButton;
    private Board firstBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchmaking);
        firstBoard = null;
        responseText = (TextView)findViewById(R.id.responseText);
        playButton = (Button)findViewById(R.id.playButton);
        playButton.setVisibility(View.INVISIBLE);
        playButton.setOnClickListener(this);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://107.6.175.212:9000")
                .build();

        MatchmakingAPI matchmakingAPI = restAdapter.create(MatchmakingAPI.class);

        matchmakingAPI.getNewMatch(USER_ID, new Callback<BoardResponse>() {
            @Override
            public void success(BoardResponse boardResponse, Response response) {
                if (boardResponse == null){
                    responseText.setText("Waiting for an opponent");
                }
                else{
                    responseText.setText("Got a match. Press play to start.");
                    playButton.setVisibility(View.VISIBLE);
                    firstBoard = new Board(boardResponse.getBoard());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                responseText.setText(error.toString());
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.playButton:
                if (firstBoard != null){
                    Intent gameIntent = new Intent(this, Game.class);
                    gameIntent.putExtra(Game.BOARD, firstBoard);
                    startActivity(gameIntent);
                }
                else Log.e("Matchmaking", "board was null when playButton was active");
                break;
        }

    }
}
