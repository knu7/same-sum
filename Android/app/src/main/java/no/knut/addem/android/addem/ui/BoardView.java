package no.knut.addem.android.addem.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import no.knut.addem.android.addem.core.Board;
import no.knut.addem.android.addem.core.Number;

public class BoardView extends View {

    public static class Settings{
        public float paddingPercentage;
        public float percentageSpacingBetweenButtons;
        public float textPaddingPercentage;

        public Settings(){
            paddingPercentage = 0.1f;
            percentageSpacingBetweenButtons = 0.3f;
            textPaddingPercentage = 0.2f;
        }

        public Settings(float paddingPercentage, float percentageSpacingBetweenButtons, float textPaddingPercentage){
            this.paddingPercentage = paddingPercentage;
            this.percentageSpacingBetweenButtons = percentageSpacingBetweenButtons;
            this.textPaddingPercentage = textPaddingPercentage;
        }
    }

    private final static String LOG_KEY = "BoardView";


    protected NumberButton[] numberButtons;
    private int columns;
    private int rows;
    protected Paint basicPaint;
    protected Paint textPaint;
    protected float textHeight;
    private Settings settings;
    protected int buttonWidth;

    public BoardView(Context context, Board board) {
        super(context);
        initialize(board);
        settings = new Settings();
        resize(getWidth(), getHeight());
    }

    public BoardView(Context context, Board board, Settings settings) {
        super(context);
        initialize(board);
        this.settings = settings;
        resize(getWidth(), getHeight());
    }

    private void initialize(Board board){
        columns = board.getColumns();
        rows = board.getRows();
        numberButtons = new NumberButton[rows * columns];
        basicPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basicPaint.setStyle(Paint.Style.FILL);
        basicPaint.setColor(Color.LTGRAY);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Number[] numbers = board.getNumbers();
        for (int i = 0; i < numbers.length; i++){
            Rect rect = new Rect(0, 0, 0, 0);
            numberButtons[i] = new NumberButton(numbers[i], rect, basicPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        resize(w, h);
    }

    private void resize(int w, int h){
        float allocatedWidth = w - w * settings.paddingPercentage;
        int maxWidth = (int)(allocatedWidth / columns);
        float spaceWidth = maxWidth * settings.percentageSpacingBetweenButtons;
        buttonWidth = (int) (maxWidth - spaceWidth);

        int x = (int)(w / 2.0f - allocatedWidth / 2.0f + spaceWidth / 2.0f);

        float textSize = buttonWidth * ( 1 - settings.textPaddingPercentage);
        textPaint.setTextSize( textSize );

        Rect textBounds = new Rect();
        textPaint.getTextBounds("1", 0, 1, textBounds);
        textHeight = textBounds.height();

        for (NumberButton numberButton : numberButtons){
            int left = numberButton.number.getColumn() * ((int)spaceWidth + buttonWidth) + x;
            int top = numberButton.number.getRow() * ((int)spaceWidth + buttonWidth);
            Rect rect = new Rect(left, top, left + buttonWidth, top + buttonWidth);
            numberButton.setRect(rect);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (NumberButton button : numberButtons){
            canvas.drawPath(button.octagon, button.paint);
            canvas.drawText(""+button.number.getValue(), button.getRect().centerX(), button.getRect().centerY() + textHeight / 2.0f, textPaint);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
