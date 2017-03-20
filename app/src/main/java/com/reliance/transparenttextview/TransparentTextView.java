package com.reliance.transparenttextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sunzhishuai on 17/3/20.
 * E-mail itzhishuaisun@sina.com
 */

public class TransparentTextView extends View {
    private String drawText = "我是默认文字";
    private float textSize = 160;
    private Paint mTextPaint;
    private BitmapShader bitmapShader;
    private int textHeight;
    private int textWidth;
    private float mRoundSize = 20;
    private Paint mBgPaint;
    private int bgColor = Color.WHITE;
    private int parentWidth;
    private int parentHeight;

    public TransparentTextView(Context context) {
        this(context, null);
    }

    public TransparentTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransparentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs, defStyleAttr);
        init();
    }

    private void getAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TransparentTextView, defStyleAttr, 0);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TransparentTextView_trans_bg_round_size:
                    this.mRoundSize = a.getDimension(attr, 20);
                    break;
                case R.styleable.TransparentTextView_trans_text_content:
                    this.drawText = a.getString(attr);
                    break;
                case R.styleable.TransparentTextView_trans_view_bg_color:
                    bgColor = a.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.TransparentTextView_trans_text_size:
                    textSize = a.getDimension(attr,80);
                    break;
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = textWidth;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = textHeight;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    private void init() {
        mTextPaint = new Paint();


        mBgPaint = new Paint();
        mBgPaint.setColor(bgColor);


        Rect rect = new Rect();
        mTextPaint.setTextSize(textSize);
        mTextPaint.getTextBounds(drawText, 0, drawText.length(), rect);
        textHeight = rect.height();
        textWidth = rect.width();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getChildrenShader();
        amendSize();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mRoundSize, mRoundSize, mBgPaint);
        }
        canvas.drawText(drawText, getMeasuredWidth() / 2 - textWidth / 2, getMeasuredHeight() / 2 + textHeight / 2, mTextPaint);
    }

    private void amendSize() {
        if (textHeight > getMeasuredHeight()) {
            textHeight = getMeasuredHeight();
        }
        if (textWidth > getMeasuredWidth()) {
            textWidth = getMeasuredWidth();
        }

        if (textHeight > getMeasuredHeight() || textWidth > getMeasuredWidth()) {
            int min = Math.min(getMeasuredWidth() / drawText.length(), getMeasuredHeight());
            mTextPaint.setTextSize(min);
        }


    }

    public void getChildrenShader() {
        if (parentWidth == 0 || parentHeight == 0) {
            BitmapDrawable bitmapDrawable = null;
            if (getParent() instanceof ViewGroup) {
                bitmapDrawable = (BitmapDrawable) ((ViewGroup) getParent()).getBackground();
            } else {
                bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.mipmap.aa);
            }
            ViewGroup parent = (ViewGroup) getParent();
            parentWidth = parent.getMeasuredWidth();
            parentHeight = parent.getMeasuredHeight();
            Bitmap bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), parentWidth, parentHeight, false);
            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
            mTextPaint.setShader(bitmapShader);
            mTextPaint.setTextSize(textSize);
        }
    }
}
