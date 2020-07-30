package com.github.progressload;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class ProgressLoad extends View {
    private int pointStartColor;
    private int pointEndColor;
    private int pointNum;
    private int pointWidth;
    private int rotationSpeed;
    private int rotationDirection;
    private int radius;
    private int pointDirection;
    private int pointStartAngle;

    private Bitmap bitmap;

    public ProgressLoad(Context context) {
        super(context);
        init(null);
    }

    public ProgressLoad(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressLoad(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressLoad(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressLoad);

        pointStartColor = typedArray.getColor(R.styleable.ProgressLoad_pointStartColor, Color.parseColor("#19000000"));
        pointEndColor = typedArray.getColor(R.styleable.ProgressLoad_pointEndColor, Color.WHITE);

        pointNum = typedArray.getInt(R.styleable.ProgressLoad_pointNum, 12);
        pointWidth = (int) typedArray.getDimension(R.styleable.ProgressLoad_pointWidth, 6);
        rotationSpeed = typedArray.getInt(R.styleable.ProgressLoad_rotationSpeed, 3000);
        rotationDirection = typedArray.getInt(R.styleable.ProgressLoad_rotationDirection, 0);
        radius = (int) typedArray.getDimension(R.styleable.ProgressLoad_radius, 80);
        pointDirection = typedArray.getInt(R.styleable.ProgressLoad_pointDirection, 0);
        pointStartAngle = typedArray.getInt(R.styleable.ProgressLoad_pointStartAngle, 0);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightLayout = getLayoutParams().height;
        int widthLayout = getLayoutParams().width;

        int width = dp2px(100);
        int height = dp2px(100);

        if (widthLayout == ViewGroup.LayoutParams.WRAP_CONTENT && heightLayout == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, height);
        } else if (widthLayout == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec));
        } else if (heightLayout == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
    public void complete(){
        int size = radius * 2 + pointWidth * 2;
        if(bitmap==null||bitmap.getWidth()!=size){
            bitmap=Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);
        }else{
            bitmap.eraseColor(Color.TRANSPARENT);
        }
    }
    private int dp2px(int value) {
        return (int) (getContext().getResources().getDisplayMetrics().density * value);
    }
}
