package no.knut.addem.android.addem.ui;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;

import no.knut.addem.android.addem.core.Number;

public class NumberButton {

    public no.knut.addem.android.addem.core.Number number;
    public Path octagon;
    public Paint paint;
    public Paint unknownPaint;
    public Paint successPaint;
    public Paint failurePaint;
    public Paint borderPaint;
    private Rect rect;
    private float halfWidth;
    private float quarterWidth;
    private float halfHeight;
    private float quarterHeight;

    public NumberButton(Number number, Rect rect, Paint paint){
        this.number = number;
        this.rect = rect;
        this.paint = paint;

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setColor(Color.rgb(115,116,109));

        unknownPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unknownPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        unknownPaint.setShader(new RadialGradient(0, 0, 100, Color.argb(160,218,230,5), Color.argb(50, 218,230,5), Shader.TileMode.MIRROR));
        unknownPaint.setStrokeWidth(15);

        successPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        successPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        successPaint.setShader(new RadialGradient(0, 0, 100, Color.argb(160,0,189,4), Color.argb(50, 0,189,4), Shader.TileMode.MIRROR));
        successPaint.setStrokeWidth(15);

        failurePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        failurePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        failurePaint.setShader(new RadialGradient(0, 0, 100, Color.argb(160,207,10,7), Color.argb(50, 207,10,7), Shader.TileMode.MIRROR));
        failurePaint.setColor(Color.rgb(207,10,7));
        failurePaint.setStrokeWidth(15);
        createOctagon(rect);
    }

    public float getCircleRadius(){
        return rect.width() / 2.0f;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
        createOctagon(rect);
    }

    public Rect getRect() {
        return rect;
    }

    public boolean contains(float x, float y){
        if (!rect.contains((int)x, (int)y))
            return false;

        if (isPointInTriangle(x, y, rect.left, rect.top, rect.left + quarterWidth, rect.top, rect.left, rect.top + quarterHeight))
            return false;

        if (isPointInTriangle(x, y, rect.right, rect.top, rect.right - quarterWidth, rect.top, rect.right, rect.top + quarterHeight))
            return false;

        if (isPointInTriangle(x, y, rect.left, rect.bottom, rect.left + quarterWidth, rect.bottom, rect.left, rect.bottom - quarterHeight))
            return false;

        if (isPointInTriangle(x, y, rect.right, rect.bottom, rect.right - quarterWidth, rect.bottom, rect.right, rect.bottom - quarterHeight))
            return false;

        return true;
    }

    private void createOctagon(Rect rect){
        halfWidth = rect.width() / 2.0f;
        quarterWidth = halfWidth / 2.0f;
        halfHeight = rect.height() / 2.0f;
        quarterHeight = halfHeight / 2.0f;

        octagon = new Path();
        octagon.moveTo(rect.left + quarterWidth, rect.top);
        octagon.rLineTo(halfWidth, 0);
        octagon.rLineTo(quarterWidth, quarterHeight);
        octagon.rLineTo(0, halfHeight);
        octagon.rLineTo(-quarterWidth, quarterHeight);
        octagon.rLineTo(-halfWidth, 0);
        octagon.rLineTo(-quarterWidth, -quarterHeight);
        octagon.rLineTo(0, -halfHeight);
        octagon.rLineTo(quarterWidth, -quarterHeight);
        octagon.close();
    }

    private static float sign (float p1x, float p1y, float p2x, float p2y, float p3x, float p3y)
    {
        return (p1x - p3x) * (p2y - p3y) - (p2x - p3x) * (p1y - p3y);
    }

    private static boolean isPointInTriangle (float px, float py, float v1x, float v1y, float v2x, float v2y, float v3x, float v3y)
    {
        boolean b1, b2, b3;

        b1 = sign(px, py, v1x, v1y, v2x, v2y) < 0.0f;
        b2 = sign(px, py, v2x, v2y, v3x, v3y) < 0.0f;
        b3 = sign(px, py, v3x, v3y, v1x, v1y) < 0.0f;

        return ((b1 == b2) && (b2 == b3));
    }



}
