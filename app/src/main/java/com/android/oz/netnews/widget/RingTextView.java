package com.android.oz.netnews.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.oz.netnews.Interface.IRingTextView;

/**
 * Created by jonesleborn on 16/8/12.
 */
public class RingTextView extends View {

    private String content = "跳过";
    // 画文字
    private TextPaint textPaint;
    private float padding = 10;
    private float stroke = 10;
    // 控件的宽度
    private float all;
    // 内切圆的半径
    private float radius;
    // 内切圆的Paint
    private Paint innerCirclePaint;
    private RectF rectF;
    private Paint ringPaint;
    private IRingTextView iRingTextView;

    public void setiRingTextViewClick(IRingTextView iRingTextView) {
        this.iRingTextView = iRingTextView;
    }

    // 当前进度的角度
    private int nowDegree;

    public RingTextView(Context context) {
        super(context);
    }

    public RingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
        textPaint.setColor(Color.WHITE);

        float textWidth = textPaint.measureText(content);

        all = textWidth + 2 * stroke + 2 * padding;

        // 内接元的半径
        // 文字的宽度+
        radius = (textWidth + padding * 3) / 2;

        // 内切圆的画笔
        innerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerCirclePaint.setColor(Color.GRAY);

        // 最外边的区域
        rectF = new RectF(stroke / 2, stroke / 2, all - stroke / 2, all - stroke / 2);

        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setColor(Color.RED);
        ringPaint.setStrokeWidth(5);
        // 记得要镂空
        ringPaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) all, (int) all);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(all / 2, all / 2, radius, innerCirclePaint);

        canvas.save();
        canvas.rotate(-90, all / 2, all / 2);
        canvas.drawArc(rectF, 0, nowDegree, false, ringPaint);
        canvas.restore();
        int y = (int) (all / 2) - (int) (textPaint.descent() + textPaint.ascent()) / 2;
        // 画文字
        canvas.drawText(content, stroke + padding, y, textPaint);
    }

    /**
     * @param total 总次数
     * @param now   当前的进度
     */
    public void setProgress(int total, int now) {
        int space = 360 / total;
        this.nowDegree = now * space;
        // 重新调用onDraw方法
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.3f);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1.0f);
                if (iRingTextView != null) {
                    iRingTextView.setOnclick(this);
                }
                break;
        }
        return true;
    }
}
