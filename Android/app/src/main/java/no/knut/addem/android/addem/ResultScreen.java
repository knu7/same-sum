package no.knut.addem.android.addem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ResultScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        Bundle extras = getIntent().getExtras();
        Solution optimalSolution = (Solution) extras.getSerializable(Game.OPTIMAL_SOLUTION);
        Solution playerSolution = (Solution) extras.getSerializable(Game.PLAYER_SOLUTION);

        TextView playerScoreTextView = (TextView) findViewById(R.id.yourScoreTextView);
        TextView optimalSolutionTextView = (TextView) findViewById(R.id.optimalSolutionTextView);

        playerScoreTextView.setText(""+playerSolution.getScore());
        optimalSolutionTextView.setText("The optimal score of " + optimalSolution.getScore()
                + " was found by your smartphone in " + optimalSolution.getSecondsSpent() + " seconds");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_screen, menu);
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
}
