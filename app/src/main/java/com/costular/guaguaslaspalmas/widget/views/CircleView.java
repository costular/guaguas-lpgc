package com.costular.guaguaslaspalmas.widget.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.utils.Utils;

/**
 * Created by Diego on 20/11/2014.
 */
public class CircleView extends AbstractBaseView
{
    private static final int YELLOW = Color.parseColor("#FFDD00");

    private String number = "0";
    private int circleRadius = 20;
    private int fillColor = 0XFFFFAB00;

    public CircleView(Context context) {
        super(context);

        init();
    }

    public CircleView(Context context, String number, String color) {
        super(context);

        this.number = number;
        this.fillColor = Color.parseColor(color);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        init();
    }


    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray aTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);

        fillColor = aTypedArray.getColor(R.styleable.CircleView_fillColor, fillColor);
        circleRadius = aTypedArray.getDimensionPixelSize(R.styleable.CircleView_circleRadius, circleRadius);

        aTypedArray.recycle();

        init();
    }


    public CircleView(Context context, int strokeColor, int strokeWidth, int fillColor, int circleRadius, int circleGap) {
        super(context);
        this.fillColor = fillColor;
        this.circleRadius = circleRadius;

        init();
    }

    private void init() {
        this.setMinimumHeight(circleRadius * 2);
        this.setMinimumWidth(circleRadius * 2);
        this.setSaveEnabled(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = this.getWidth();
        int h = this.getHeight();

        int ox = w/2;
        int oy = h/2;

        Paint p = getTextPaint();

        Rect bounds = new Rect();
        p.getTextBounds(number, 0, number.length(), bounds);

        canvas.drawCircle(ox, oy, circleRadius, getFill());
        canvas.drawText(number, ox, oy + (bounds.height() / 2), p);

        invalidate();
    }

    private Paint getStroke()
    {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.STROKE);
        return p;
    }

    private Paint getFill()
    {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(fillColor);
        p.setStyle(Paint.Style.FILL);
        return p;
    }

    private Paint getTextPaint() {

        int color;

        if(fillColor == YELLOW) {
            color = Color.BLACK;
        } else {
            color = Color.WHITE;
        }

        // Tamaño de la letra
        int size = 18;

        float pixels = Utils.getPixelsByDP(size, getContext().getResources());

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(pixels);
        p.setColor(color);
        p.setTextAlign(Paint.Align.CENTER);

        return p;
    }

    @Override
    protected int hGetMaximumHeight() {
        return circleRadius * 2;
    }

    @Override
    protected int hGetMaximumWidth() {
        return circleRadius * 2;
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getFillColor() {
        return fillColor;
    }

    public String getText() {
        return number;
    }

    public void setText(String str) {
        number = str;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }


}
