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
public class ChartView extends View {

    public int XPoint = 50;    //原点的X坐标
    public int YPoint = 330;     //原点的Y坐标
    public int XScale = 40;     //X的刻度长度
    public int YScale = 20;     //Y的刻度长度
    public int XLength = 420;        //X轴的长度
    public int YLength = 280;        //Y轴的长度
    public int XueYaData_count;

    public String[] XLabel = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};    //X的刻度
    public String[] YLabel = {"50", "60", "70", "80", "90", "100", "110", "120", "130", "140", "150", "160", "170", "180"};    //Y的刻度
    public String[] HData = new String[XueYaData_count];      //高压数据
    public String[] LData = new String[XueYaData_count];      //低压数据
    private static final String TAG = "ChartView";


    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChartViewHeart);
        XueYaData_count = typedArray.getInt(R.styleable.ChartViewHeart_hcount, 0);
        if (XueYaData_count < 0)
            XueYaData_count = 0;
        typedArray.recycle();
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        Paint Hpaint = new Paint();
        Hpaint.setStyle(Paint.Style.STROKE);
        Hpaint.setAntiAlias(true);//去锯齿
        Hpaint.setColor(Color.RED);//颜色
        Hpaint.setTextSize(15);
        drawHDatas(canvas, Hpaint);

        Paint Lpaint = new Paint();
        Lpaint.setStyle(Paint.Style.STROKE);
        Lpaint.setAntiAlias(true);//去锯齿
        Lpaint.setColor(Color.rgb(0, 139, 139));//颜色
        Lpaint.setTextSize(15);
        drawLDatas(canvas, Lpaint);

        drawTitle(canvas, Hpaint, Lpaint);


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
        if (XueYaData_count == 1) {
            canvas.drawCircle(XPoint, YCoord(HData[0]), 2, p);
            canvas.drawText(HData[0], XPoint + 5, YCoord(HData[0]), p);
        } else {
            for (int i = 1; i * XScale < XLength && i < XueYaData_count; i++) {
                //数据值
                if (i > 0 && i < 10) {
                    if (YCoord(HData[i - 1]) != -999 && YCoord(HData[i]) != -999) { //保证有效数据
                        canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(HData[i - 1]), XPoint + i * XScale, YCoord(HData[i]), p);
                        canvas.drawCircle(XPoint + i * XScale, YCoord(HData[i]), 2, p);
                        canvas.drawText(HData[i - 1], XPoint + (i - 1) * XScale - 3, YCoord(HData[i - 1]) - 8, p);
                        if (i == XueYaData_count - 1) {
                            canvas.drawText(HData[i], XPoint + i * XScale - 3, YCoord(HData[i]) - 8, p);
                        }
                    }

                }
            }
        }
    }

    private void drawLDatas(Canvas canvas, Paint p) {
        if (XueYaData_count == 1) {
            canvas.drawCircle(XPoint, YCoord(LData[0]), 2, p);
            canvas.drawText(LData[0], XPoint + 5, YCoord(LData[0]), p);
        } else {
            for (int i = 1; i * XScale < XLength && i < XueYaData_count; i++) {
                //数据值
                if (i > 0 && i < 10) {
                    if (YCoord(LData[i - 1]) != -999 && YCoord(LData[i]) != -999) { //保证有效数据
                        canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(LData[i - 1]), XPoint + i * XScale, YCoord(LData[i]), p);
                        canvas.drawCircle(XPoint + i * XScale, YCoord(LData[i]), 2, p);
                        canvas.drawText(LData[i - 1], XPoint + (i - 1) * XScale - 3, YCoord(LData[i - 1]) - 8, p);
                        if (i == XueYaData_count - 1) {
                            canvas.drawText(LData[i], XPoint + i * XScale - 3, YCoord(LData[i]) - 8, p);
                        }
                    }
                }
            }
        }
    }

    private void drawTitle(Canvas canvas, Paint paint1, Paint paint2) {
        canvas.drawCircle(XPoint + 20, YPoint - YLength + 40, 2, paint1);
        canvas.drawLine(XPoint + 20, YPoint - YLength + 40, XPoint + 60, YPoint - YLength + 40, paint1);
        canvas.drawCircle(XPoint + 60, YPoint - YLength + 40, 2, paint1);
        canvas.drawText("高血压曲线", XPoint + 80, YPoint - YLength + 40, paint1);

        canvas.drawCircle(XPoint + 20, YPoint - YLength + 60, 2, paint2);
        canvas.drawLine(XPoint + 20, YPoint - YLength + 60, XPoint + 60, YPoint - YLength + 60, paint2);
        canvas.drawCircle(XPoint + 60, YPoint - YLength + 60, 2, paint2);
        canvas.drawText("低血压曲线", XPoint + 80, YPoint - YLength + 60, paint2);
    }

    public void SetInfo(String[] HDatas, String[] LDatas) {

        HData = HDatas;
        LData = LDatas;
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
