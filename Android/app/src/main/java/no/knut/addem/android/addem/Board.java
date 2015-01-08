package no.knut.addem.android.addem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class Board extends View implements View.OnTouchListener {

    private final float PADDING_PERCENTAGE = 0.1f;
    private final float SPACING_BETWEEN_BUTTONS_PERCENTAGE = 0.3f;
    private final float TEXT_PADDING_PERCENTAGE = 0.2f;
    private final int[] Colors = new int[]{Color.BLUE, Color.RED, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.YELLOW};

    private Random random = new Random();
    private Stack<Stack<NumberButton>> sums = new Stack<>();
    private Stack<NumberButton> currentSum;
    public int rows, columns, maxNumber;
    private Context context;
    private NumberButton[] numberButtons;
    private Paint basicPaint;
    private Paint textPaint;
    private Paint[] backgroundPaints;
    private int currentPaintIndex;
    private float textHeight;


    public Board(Context context, int rows, int columns, int maxNumber) {
        super(context);
        this.rows = rows;
        this.columns = columns;
        this.maxNumber = maxNumber;
        this.context = context;
        numberButtons = new NumberButton[rows*columns];
        basicPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basicPaint.setStyle(Paint.Style.FILL);
        basicPaint.setColor(Color.LTGRAY);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        setOnTouchListener(this);

        backgroundPaints = new Paint[Colors.length];

        for (int i = 0; i < Colors.length; i++){
            Paint paint = backgroundPaints[i];
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Colors[i]);
            backgroundPaints[i] = paint;
        }
        currentPaintIndex = 0;
    }

    public void undoLastSum(){
        if(sums.isEmpty())
            return;

        Stack<NumberButton> sum = sums.pop();
        for (NumberButton button : sum){
            button.paint = basicPaint;
        }
        invalidate();
    }

    public void clearAllSums(){

        if(sums.isEmpty())
            return;

        for (Stack<NumberButton> sum : sums){
            for (NumberButton button : sum){
                button.paint = basicPaint;
            }
        }
        sums.clear();
        invalidate();
    }

    public Number[] getBoard(){

        Number[] board = new Number[numberButtons.length];

        for (int i = 0; i < numberButtons.length; i++){
            board[i] = numberButtons[i].number;
        }
        return board;
    }

    public Solution getSolution(){
        Set<Set<Number>> sumsAsSet = new HashSet<>();

        for (Stack<NumberButton> sum : sums){

            Set<Number> sumAsSet = new HashSet<Number>();
            for( NumberButton button : sum ){
                sumAsSet.add(button.number);
            }

            sumsAsSet.add(sumAsSet);
        }

        return new Solution(sumsAsSet, 30);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float allocatedWidth = w - w * PADDING_PERCENTAGE;
        int maxWidth = (int)(allocatedWidth / rows);
        float spaceWidth = maxWidth * SPACING_BETWEEN_BUTTONS_PERCENTAGE;
        int buttonWidth = (int) (maxWidth - spaceWidth);

        int x = (int)(w / 2.0f - allocatedWidth / 2.0f + spaceWidth / 2.0f);

        float textSize = buttonWidth * ( 1 - TEXT_PADDING_PERCENTAGE);
        textPaint.setTextSize( textSize );

        Rect textBounds = new Rect();
        textPaint.getTextBounds("1", 0, 1, textBounds);
        textHeight = textBounds.height();

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int left = i * ((int)spaceWidth + buttonWidth) + x;
                int top = j * ((int)spaceWidth + buttonWidth);
                Rect rect = new Rect(left, top, left + buttonWidth, top + buttonWidth);
                Number number = new Number(i, j, random.nextInt(maxNumber) + 1);
                numberButtons[index] = new NumberButton(number, rect, basicPaint);
                index++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (NumberButton button : numberButtons){
            Paint p = button.paint;
            canvas.drawRect(button.rect, button.paint);
            canvas.drawText(""+button.number.number, button.rect.centerX(), button.rect.centerY() + textHeight / 2.0f, textPaint);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            currentSum = new Stack<>();
            currentPaintIndex++;
            if(currentPaintIndex >= backgroundPaints.length)
                currentPaintIndex = 0;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (!currentSum.isEmpty())
                sums.push(currentSum);
        }

        NumberButton buttonTouched = null;

        for (NumberButton button : numberButtons){
            if(button.rect.contains((int)event.getX(), (int)event.getY()))
                buttonTouched = button;
        }

        if (buttonTouched == null)
            return true;

        if(isSelected(buttonTouched))
            return true;

        if (currentSum.isEmpty()) {
            currentSum.push(buttonTouched);
            buttonTouched.paint = backgroundPaints[currentPaintIndex];
            invalidate();
            return true;
        }

        NumberButton prevButton = currentSum.peek();

        if (buttonTouched == prevButton)
            return true;

        if (Math.abs(buttonTouched.number.row - prevButton.number.row) > 1)
            return true;

        if (Math.abs(buttonTouched.number.column - prevButton.number.column) > 1)
            return true;

        currentSum.push(buttonTouched);
        buttonTouched.paint = backgroundPaints[currentPaintIndex];
        this.invalidate();
        return true;
    }

    private boolean isSelected(NumberButton button)
    {
        for (List sum : sums)
        {
            if(sum.contains(button))
            {
                return true;
            }
        }

        return false;
    }
}