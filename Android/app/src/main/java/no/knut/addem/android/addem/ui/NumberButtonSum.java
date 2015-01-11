package no.knut.addem.android.addem.ui;

import android.graphics.Paint;

import java.util.HashSet;

public class NumberButtonSum extends HashSet<NumberButton> {

    private Paint paint;

    public NumberButtonSum(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }
}
