package com.wmct.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wmct.health.R;


/**
 * Created by shan on 2016/1/12.
 */
public class ChartViewHeart extends View {

    public int XPoint = 50;    //原点的X坐标
    public int YPoint = 330;     //原点的Y坐标
    public int XScale = 40;     //X的刻度长度
    public int YScale = 35;    //Y的刻度长度
    public int XLength = 420;        //X轴的长度
    public int YLength = 280;        //Y轴的长度
    public int HeartData_count;

    public String[] XLabel = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};    //X的刻度
    public String[] YLabel = {"50", "60", "70", "80", "90", "100", "110", "120"};    //Y的刻度
    public String[] HeartData = new String[HeartData_count];      //高压数据
    private static final String TAG = "ChartView";


    public ChartViewHeart(Context context) {
        super(context);

    }

    public ChartViewHeart(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChartViewHeart);
        HeartData_count = typedArray.getInt(R.styleable.ChartViewHeart_hcount, 0);
        if (HeartData_count < 0)
            HeartData_count = 0;
        typedArray.recycle();
    }

    public ChartViewHeart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);//去锯齿
        paint.setColor(Color.BLACK);//颜色


        drawXLine(canvas, paint);
        drawYLine(canvas, paint);

        Paint Heartpaint = new Paint();
        Heartpaint.setStyle(Paint.Style.STROKE);
        Heartpaint.setAntiAlias(true);//去锯齿
        Heartpaint.setColor(Color.rgb(255, 0, 255));//颜色
        Heartpaint.setTextSize(15);
        drawHDatas(canvas, Heartpaint);


        drawTitle(canvas, Heartpaint);


    }

    // 画横纵轴
    private void drawXLine(Canvas canvas, Paint p) {
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, p);
        for (int i = 0; i * XScale < XLength; i++) {
            canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale, YPoint - 5, p);
            canvas.drawText(XLabel[i], XPoint + i * XScale - 5, YPoint + 12, p);

        }

        canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint + 3, p);    //箭头
        canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint - 3, p);
    }

    private void drawYLine(Canvas canvas, Paint p) {
        canvas.drawLine(XPoint, YPoint, XPoint, YPoint - YLength, p);
        for (int i = 1; i * YScale < YLength; i++) {
            canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i * YScale, p);
            canvas.drawText(YLabel[i - 1], XPoint - 24, YPoint - i * YScale + 5, p);
        }

        canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength + 6, p);
        canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength + 6, p);
    }

    private void drawHDatas(Canvas canvas, Paint p) {
        if (HeartData_count == 1) {
            canvas.drawCircle(XPoint, YCoord(HeartData[0]), 2, p);
            canvas.drawText(HeartData[0], XPoint + 5, YCoord(HeartData[0]), p);
        } else {
            for (int i = 1; i * XScale < XLength && i < HeartData_count; i++) {
                //数据值
                if (i < 10) {
                    if (YCoord(HeartData[i - 1]) != -999 && YCoord(HeartData[i]) != -999) { //保证有效数据
                        canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(HeartData[i - 1]), XPoint + i * XScale, YCoord(HeartData[i]), p);
                        canvas.drawCircle(XPoint + i * XScale, YCoord(HeartData[i]), 2, p);
                        canvas.drawText(HeartData[i - 1], XPoint + (i - 1) * XScale - 3, YCoord(HeartData[i - 1]) - 8, p);
                        if (i == HeartData_count - 1) {
                            canvas.drawText(HeartData[i], XPoint + i * XScale - 3, YCoord(HeartData[i]) - 8, p);
                        }
                    }

                }
            }
        }
    }


    private void drawTitle(Canvas canvas, Paint paint) {
        canvas.drawText("心率曲线", XPoint + 20, YPoint - YLength + 30, paint);
    }

    public void SetInfo(String[] HeartDatas) {

        HeartData = HeartDatas;
    }

    private int YCoord(String y0)  //计算绘制时的Y坐标，无数据时返回-999
    {
        int y;
        try {
            y = Integer.parseInt(y0);
        } catch (Exception e) {
            return -999;    //出错则返回-999
        }

        return YPoint - YScale - (y - 50) * YScale / 10;
    }


}
