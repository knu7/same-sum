package no.knut.addem.android.addem;

import android.graphics.Paint;
import android.graphics.Rect;

public class NumberButton {

    public Number number;
    public Rect rect;
    public Paint paint;

    public NumberButton(Number number, Rect rect, Paint paint){
        this.number = number;
        this.rect = rect;
        this.paint = paint;
    }

}
