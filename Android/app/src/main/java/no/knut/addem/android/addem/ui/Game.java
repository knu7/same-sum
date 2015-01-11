package no.knut.addem.android.addem.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import no.knut.addem.android.addem.R;
import no.knut.addem.android.addem.core.Board;
import no.knut.addem.android.addem.core.OptimalSolution;
import no.knut.addem.android.addem.core.Solution;
import no.knut.addem.android.addem.events.OptimalSolutionReadyEvent;

public class Game extends ActionBarActivity implements View.OnClickListener{

    public final static String PLAYER_SOLUTION = "no.knut.addem.android.PLAYER_SOLUTION";
    public final static String OPTIMAL_SOLUTION = "no.knut.addem.android.OPTIMAL_SOLUTION";
    private final static String LOG_KEY = "Game";

    private EventBus eventBus;
    private BoardView boardView;
    private TextView countDownTextView;
    private Context thisContext;
    private OptimalSolutionReadyEvent optimalSolutionEvent;
    private CountDownTimer timer;

    @Override protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_KEY, "Game started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        optimalSolutionEvent = null;
        LinearLayout boardContainer = (LinearLayout) findViewById(R.id.boardContainer);
        Board board = new Board(4, 4, 12);
        boardView = new BoardView(this, board);
        boardContainer.addView(boardView);
        findViewById(R.id.undoButton).setOnClickListener(this);
        findViewById(R.id.clearButton).setOnClickListener(this);
        countDownTextView = (TextView)findViewById(R.id.countDownTextView);
        thisContext = this;
        eventBus = EventBus.getDefault();
        eventBus.register(this);

        new OptimalSolution().execute(board);

        timer = new CountDownTimer(35000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownTextView.setText(millisUntilFinished / 1000 + " sec remaining");
            }

            public void onFinish() {
                boardView.deactivateInput();
                countDownTextView.setText("0 sec remaining");
                Solution playerSolution = boardView.getSolution();
                Intent resultScreen = new Intent(thisContext, ResultScreen.class);
                resultScreen.putExtra(OPTIMAL_SOLUTION, optimalSolutionEvent);
                resultScreen.putExtra(PLAYER_SOLUTION, playerSolution);
                startActivity(resultScreen);

            }
        }.start();

    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        timer.cancel();
        super.onDestroy();
    }

    @Override public void onClick(View v) {

        switch (v.getId()){

            case R.id.undoButton:
                boardView.undoLastSum();
                break;

            case R.id.clearButton:
                boardView.clearAllSums();
                break;
        }
    }

    public void onEventMainThread(OptimalSolutionReadyEvent event){
        optimalSolutionEvent = event;
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addem, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
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
