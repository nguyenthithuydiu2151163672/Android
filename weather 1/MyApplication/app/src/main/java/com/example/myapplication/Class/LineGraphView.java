package com.example.myapplication.Class;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.myapplication.Model.Weather;

import java.util.List;

public class LineGraphView extends View {
    private Paint linePaint;
    private Paint pointPaint;
    private Paint textPaint;
    private List<Weather> weatherList;

    public LineGraphView(Context context) {
        super(context);
        init();
    }

    public LineGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(5f);
        linePaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30f);
        textPaint.setAntiAlias(true);
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (weatherList == null || weatherList.isEmpty()) {
            return;
        }

        float graphHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        float graphWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float maxTemp = Float.MIN_VALUE;
        float minTemp = Float.MAX_VALUE;

        for (Weather weather : weatherList) {
            maxTemp = Math.max(maxTemp, (float) weather.getTemp());
            minTemp = Math.min(minTemp, (float) weather.getTemp());
        }

        float tempRange = maxTemp - minTemp;
        float prevX = getPaddingLeft();
        float prevY = getPaddingTop() + (graphHeight - ((float) weatherList.get(0).getTemp() - minTemp) / tempRange * graphHeight);

        for (int i = 1; i < weatherList.size(); i++) {
            float x = getPaddingLeft() + i * graphWidth / (weatherList.size() - 1);
            float y = getPaddingTop() + (graphHeight - ((float) weatherList.get(i).getTemp() - minTemp) / tempRange * graphHeight);
            canvas.drawLine(prevX, prevY, x, y, linePaint);
            prevX = x;
            prevY = y;
        }

        for (int i = 0; i < weatherList.size(); i++) {
            float x = getPaddingLeft() + i * graphWidth / (weatherList.size() - 1);
            float y = getPaddingTop() + (graphHeight - ((float) weatherList.get(i).getTemp() - minTemp) / tempRange * graphHeight);
            canvas.drawCircle(x, y, 10f, pointPaint);
            canvas.drawText(String.format("%.1f°C", weatherList.get(i).getTemp()), x - 30f, y - 20f, textPaint);
        }
    }
}
