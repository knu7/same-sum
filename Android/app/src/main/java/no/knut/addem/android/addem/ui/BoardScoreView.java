package no.knut.addem.android.addem.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
        correctPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        wrongPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wrongPaint.setColor(Color.rgb(255, 0, 0));
        wrongPaint.setStyle(correctPaint.getStyle());
        wrongPaint.setStrokeCap(correctPaint.getStrokeCap());
        wrongPaint.setStrokeJoin(correctPaint.getStrokeJoin());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        correctPaint.setStrokeWidth(buttonWidth * 0.25f);
        wrongPaint.setStrokeWidth(correctPaint.getStrokeWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("", "wrongs: "+solution.getWrongSums().size());
        drawSums(canvas, solution.getCorrectSums(), correctPaint);
        drawSums(canvas, solution.getWrongSums(), wrongPaint);

        for (NumberButton button : numberButtons){
            canvas.drawText(""+button.number.getValue(), button.getRect().centerX(), button.getRect().centerY() + textHeight / 2.0f, textPaint);
        }
    }

    private void drawSums(Canvas canvas, Set<Set<Number>> sums, Paint paint){

        if (sums.isEmpty())
            return;

        List<NumberButton> sumButtons = new ArrayList<>();
        for (Set<Number> sum: sums){
            for (Number number : sum){
                for(NumberButton numberbutton: numberButtons) {
                    if (numberbutton.number.equals(number)) {
                        sumButtons.add(numberbutton);


                    }
                }
            }
            pathOfSum(canvas, sumButtons, paint);
            sumButtons.clear();
        }
    }

    private void drawPath(Canvas canvas, NumberButton button1, NumberButton button2, Paint paint){
        Path path = new Path();
        path.moveTo(button1.getRect().centerX(), button1.getRect().centerY());
        path.addCircle(button1.getRect().centerX(), button1.getRect().centerY(), button1.getRect().height() / 2.0f - 25, Path.Direction.CCW);
        path.moveTo(button1.getRect().centerX(), button1.getRect().centerY());
        path.lineTo(button2.getRect().centerX(), button2.getRect().centerY());
        path.addCircle(button2.getRect().centerX(), button2.getRect().centerY(), button2.getRect().height() / 2.0f - 25, Path.Direction.CCW);
        canvas.drawPath(path, paint);
    }

    private void pathOfSum(Canvas canvas, List<NumberButton> sum, Paint paint){
        if (sum.isEmpty())
            return;

        if (sum.size() == 1){
            NumberButton singleNumber = sum.get(0);
            Path circle = new Path();
            circle.addCircle(singleNumber.getRect().centerX(), singleNumber.getRect().centerY(), buttonWidth / 2.0f - correctPaint.getStrokeWidth() * 0.40f, Path.Direction.CW);
            canvas.drawPath(circle, paint);
        }

        List<NumberButton> removeList = new ArrayList<>();
        Stack<NumberButton> S = new Stack<>();
        S.push(sum.remove(0));
        while (!S.isEmpty()){
            NumberButton v = S.pop();
            Path circle = new Path();
            circle.addCircle(v.getRect().centerX(), v.getRect().centerY(), buttonWidth / 2.0f - correctPaint.getStrokeWidth() * 0.40f, Path.Direction.CCW);
            canvas.drawPath(circle, paint);
            int numConnected = 0;
            for (NumberButton w : sum){
                if (!areNeighbours(v, w))
                    continue;

                if (numConnected <= 2){

                    Path rect = new Path();
                    Log.d("", v.getRect().centerX() + " " + v.getRect().centerY() + " " + w.getRect().centerX() + " " + w.getRect().centerY());
                    rect.moveTo(v.getRect().centerX(), v.getRect().centerY());
                    rect.lineTo(w.getRect().centerX(), w.getRect().centerY());
                    rect.addCircle(w.getRect().centerX(), w.getRect().centerY(), buttonWidth / 2.0f - correctPaint.getStrokeWidth() * 0.40f, Path.Direction.CCW);

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
        int rowDifference = Math.abs(button1.number.getRow() - button2.number.getRow());
        int columnDifference = Math.abs(button1.number.getColumn() - button2.number.getColumn());
        if (rowDifference > 1)
            return false;

        if (columnDifference > 1)
            return false;

        if (rowDifference == 0 && columnDifference == 0)
            return false;

        return true;
    }
}
