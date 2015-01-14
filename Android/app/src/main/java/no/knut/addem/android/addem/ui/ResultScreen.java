package no.knut.addem.android.addem.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import no.knut.addem.android.addem.R;
import no.knut.addem.android.addem.core.Board;
import no.knut.addem.android.addem.core.Solution;
import no.knut.addem.android.addem.events.OptimalSolutionReadyEvent;


public class ResultScreen extends ActionBarActivity {

    private final static String LOG_KEY = "ResultScreen";
    private TextView optimalSolutionTextView;
    private EventBus eventBus;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        Bundle extras = getIntent().getExtras();

        eventBus = EventBus.getDefault();
        eventBus.register(this);

        Object optimalSolutionOrNull = extras.getSerializable(Game.OPTIMAL_SOLUTION);


        Solution playerSolution = (Solution) extras.getSerializable(Game.PLAYER_SOLUTION);

        Board board = (Board)extras.getSerializable(Game.BOARD);
        addContentView(new BoardScoreView(this, board, new BoardView.Settings(), playerSolution),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        TextView playerScoreTextView = (TextView) findViewById(R.id.yourScoreTextView);
        optimalSolutionTextView = (TextView) findViewById(R.id.optimalSolutionTextView);

        playerScoreTextView.setText(""+playerSolution.getScore());

        if(optimalSolutionOrNull != null){
            OptimalSolutionReadyEvent optimalSolution = (OptimalSolutionReadyEvent)optimalSolutionOrNull;
            displayOptimalSolution(optimalSolution);
        }
        else{
            progressDialog = ProgressDialog.show(this, "WAIT!", "Finding optimal solution");
        }
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    private void displayOptimalSolution(OptimalSolutionReadyEvent solutionEvent){
        Solution solution = solutionEvent.getSolution();
        optimalSolutionTextView.setText("The optimal score of " + solution.getScore()
                + " was found by your smartphone in " + solution.getSecondsSpent() + " seconds"
                + " after checking " + solutionEvent.getSumsChecked() + " sums."
                );
    }

    public void onEventMainThread(OptimalSolutionReadyEvent event){
        displayOptimalSolution(event);
        progressDialog.dismiss();
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
