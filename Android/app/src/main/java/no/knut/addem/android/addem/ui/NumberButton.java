package no.knut.addem.android.addem.ui;

import android.graphics.Paint;
import android.graphics.Rect;

import no.knut.addem.android.addem.core.Number;

public class NumberButton {

    public no.knut.addem.android.addem.core.Number number;
    public Rect rect;
    public Paint paint;

    public NumberButton(Number number, Rect rect, Paint paint){
        this.number = number;
        this.rect = rect;
        this.paint = paint;
    }

}
