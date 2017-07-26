package com.zerotoone.n17r.zhetisoz.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Random;

/**
 * Created by асус on 21.07.2017.
 */
public class RandomCircle extends View
{
    int red = (int) (Math.random() * 255);
    int green = (int) (Math.random() * 255);
    int blue = (int) (Math.random() * 255);
    public RandomCircle(Context con)
    {
        super(con);
    }
    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        int color = Color.argb(90,red, green, blue);

        int w = getWidth();
        int h = getHeight();
        Random random = new Random();

        int randR = random.nextInt(6) + 5;
        int randX = random.nextInt(w - 20) + 10;
        int randY = random.nextInt(h - 20) + 10;

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(100);
        p.setColor(color);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(randX, randY, randR, p);

        clear(c);

    }
    public void clear(Canvas c)
    {
        c.drawColor(Color.TRANSPARENT);
    }

}