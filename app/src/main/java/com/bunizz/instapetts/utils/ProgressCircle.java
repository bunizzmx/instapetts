package com.bunizz.instapetts.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ProgressCircle extends View {
    private int animValue;
    private int strokeWidth = 5;


    public ProgressCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        animValue = 0;
    }

    public ProgressCircle(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(Color.parseColor("#FFFFFF"));
        RectF rectF = new RectF();
        rectF.set(strokeWidth,strokeWidth,getWidth() - strokeWidth  ,getWidth() - strokeWidth);
        canvas.drawArc(rectF,0,360,false,paint);
        paint.setColor(Color.parseColor("#E5E6E6"));
        canvas.drawArc(rectF,animValue,120,false,paint);
    }
    public void setValue(int animatedValue) {
        animValue = animatedValue;
        invalidate();
    }
}