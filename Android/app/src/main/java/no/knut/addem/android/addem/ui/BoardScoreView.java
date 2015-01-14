package no.knut.addem.android.addem.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import no.knut.addem.android.addem.core.Board;
import no.knut.addem.android.addem.core.Number;
import no.knut.addem.android.addem.core.Solution;

public class BoardScoreView extends BoardView {

    private final static String LOG_KEY = "BoardView";

    private Solution solution;
    private Paint correctPaint;
    private Paint wrongPaint;


    public BoardScoreView(Context context, Board board, BoardView.Settings settings, Solution solution) {
        super(context, board, settings);
        this.solution = solution;

        correctPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        correctPaint.setColor(Color.rgb(0, 255, 0));
        correctPaint.setStyle(Paint.Style.STROKE);
        correctPaint.setStrokeWidth(50);

        wrongPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wrongPaint.setColor(Color.rgb(255, 0, 0));
        wrongPaint.setStyle(correctPaint.getStyle());
        wrongPaint.setStrokeCap(correctPaint.getStrokeCap());
        wrongPaint.setStrokeJoin(correctPaint.getStrokeJoin());
        wrongPaint.setStrokeWidth(correctPaint.getStrokeWidth());
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawSums(canvas, solution.getCorrectSums(), correctPaint);
        drawSums(canvas, solution.getWrongSums(), wrongPaint);

        for (NumberButton button : numberButtons){
            canvas.drawText(""+button.number.getValue(), button.getRect().centerX(), button.getRect().centerY() + textHeight / 2.0f, textPaint);
        }
    }

    private void drawSums(Canvas canvas, Set<Set<Number>> sums, Paint paint){
        List<NumberButton> sumButtons = new ArrayList<>();
        for (Set<Number> sum: sums){
            for (Number number : sum){
                for(NumberButton numberbutton: numberButtons) {
                    if (numberbutton.number.equals(number)) {
                        sumButtons.add(numberbutton);

                    }
                    else Log.d(" ", number.toString() + "  " + numberbutton.number.toString());

                }
            }
            Log.d("Tags", sumButtons.size() + " SIZE");
            pathOfSum(canvas, sumButtons, correctPaint);
            sumButtons.clear();
        }
    }

    private void pathOfSum(Canvas canvas, List<NumberButton> sum, Paint paint){
        if (sum.isEmpty())
            return;
        List<NumberButton> removeList = new ArrayList<>();
        Stack<NumberButton> S = new Stack<>();
        S.push(sum.remove(0));
        while (!S.isEmpty()){
            NumberButton v = S.pop();
            Rect vRect = v.getRect();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);

            int numConnected = 0;
            for (NumberButton w : sum){
                if (!areNeighbours(v, w))
                    continue;

                numConnected++;

                if (numConnected <= 2){

                    Path rect = new Path();
                    Log.d("", v.getRect().centerX() + " " + v.getRect().centerY() + " " + w.getRect().centerX() + " " + w.getRect().centerY());
                    rect.moveTo(v.getRect().centerX(), v.getRect().centerY());
                    if(numConnected == 1)
                        rect.addCircle(v.getRect().centerX(), v.getRect().centerY(), v.getRect().height() / 2.0f - 25, Path.Direction.CCW);
                    rect.lineTo(w.getRect().centerX(), w.getRect().centerY());
                    rect.addCircle(w.getRect().centerX(), w.getRect().centerY(), w.getRect().height() / 2.0f - 25, Path.Direction.CCW);

                    Log.d("", paint.getStrokeWidth() + " width");
                    canvas.drawPath(rect, paint);
                }

                S.push(w);
                removeList.add(w);
            }

            sum.removeAll(removeList);
            removeList.clear();
        }
    }

    private boolean areNeighbours(NumberButton button1, NumberButton button2){
        if (Math.abs(button1.number.getRow() - button2.number.getRow()) > 1)
            return false;

        if (Math.abs(button1.number.getColumn() - button2.number.getColumn()) > 1)
            return false;

        return true;
    }
}
