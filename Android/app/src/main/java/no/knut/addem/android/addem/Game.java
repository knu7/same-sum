package no.knut.addem.android.addem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Game extends FragmentActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Solution> {

    public final static String PLAYER_SOLUTION = "no.knut.addem.android.PLAYER_SOLUTION";
    public final static String OPTIMAL_SOLUTION = "no.knut.addem.android.OPTIMAL_SOLUTION";

    private Board board;
    private TextView countDownTextView;
    private Context thisContext;
    private LoaderManager.LoaderCallbacks thisLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        LinearLayout boardContainer = (LinearLayout) findViewById(R.id.boardContainer);
        board = new Board(this, 4, 4, 12);
        boardContainer.addView(board);
        findViewById(R.id.undoButton).setOnClickListener(this);
        findViewById(R.id.clearButton).setOnClickListener(this);
        countDownTextView = (TextView)findViewById(R.id.countDownTextView);
        thisContext = this;
        getSupportLoaderManager().initLoader(0, null, this);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownTextView.setText(millisUntilFinished / 1000 + " sec remaining");
            }

            public void onFinish() {
                countDownTextView.setText("0 sec remaining");

            }
        }.start();
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

            case R.id.undoButton:
                board.undoLastSum();
                break;

            case R.id.clearButton:
                board.clearAllSums();
                break;
        }
    }




    @Override
    public Loader<Solution> onCreateLoader(int id, Bundle args) {
        return new OptimalSolution(this, board.getBoard(), board.maxNumber);
    }

    @Override
    public void onLoadFinished(Loader<Solution> loader, Solution optimalSolution) {
        Solution playerSolution = board.getSolution();
        Intent resultScreen = new Intent(thisContext, ResultScreen.class);
        resultScreen.putExtra(OPTIMAL_SOLUTION, optimalSolution);
        resultScreen.putExtra(PLAYER_SOLUTION, playerSolution);
        startActivity(resultScreen);
    }

    @Override
    public void onLoaderReset(Loader<Solution> loader) {

    }
}
