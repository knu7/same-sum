package no.knut.addem.android.addem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import no.knut.addem.android.addem.R;
import no.knut.addem.android.addem.core.Board;


public class Addem extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        findViewById(R.id.singlePlayerButton).setOnClickListener(this);
        findViewById(R.id.matchMakingButton).setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addem, menu);
        return true;
    }

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

            case R.id.singlePlayerButton:
                Intent gameIntent = new Intent(this, Game.class);
                gameIntent.putExtra(Game.BOARD, new Board(4,4, 12));
                startActivity(gameIntent);
                break;
            case R.id.matchMakingButton:
                startActivity(new Intent(this, Matchmaking.class));
                break;
        }
    }
}
