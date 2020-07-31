package com.github.progressload;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
    private Paint paint;

    private Bitmap bitmap;
    private ArgbEvaluator argbEvaluator;
    private ValueAnimator valueAnimator;
    private float drawAngle;
    public static final int cw=0;
    public static final int ccw=1;
    @IntDef({cw,ccw})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction{}

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
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressLoad);

        pointStartColor = typedArray.getColor(R.styleable.ProgressLoad_pointStartColor,Color.parseColor("#29000000"));
        pointEndColor = typedArray.getColor(R.styleable.ProgressLoad_pointEndColor, Color.WHITE);

        pointNum = typedArray.getInt(R.styleable.ProgressLoad_pointNum, 12);
        pointWidth = (int) typedArray.getDimension(R.styleable.ProgressLoad_pointWidth, 16);
        rotationSpeed = typedArray.getInt(R.styleable.ProgressLoad_rotationSpeed, 1770);
        rotationDirection = typedArray.getInt(R.styleable.ProgressLoad_rotationDirection, 0);
        radius = (int) typedArray.getDimension(R.styleable.ProgressLoad_radius, 55);
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
        super.onSizeChanged(w, h, oldw, oldh);
        complete();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate((rotationDirection==1?-1:1)*drawAngle,getWidth()/2,getHeight()/2);
        canvas.drawBitmap(bitmap,getWidth()/2-bitmap.getWidth()/2,getHeight()/2-bitmap.getHeight()/2,null);
    }
    public void complete(){
        int size = radius * 2 + pointWidth * 2;
        int center=size/2;
        if(bitmap==null||bitmap.getWidth()!=size){
            bitmap=Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);
        }else{
            bitmap.eraseColor(Color.TRANSPARENT);
        }
        Canvas canvas=new Canvas(bitmap);

        canvas.translate(center,center);

        canvas.rotate(pointStartAngle);
        if(argbEvaluator==null){
            argbEvaluator = new ArgbEvaluator();
        }
        for (int i = 0; i < pointNum; i++) {
            int pColor;
            if(i==pointNum-1){
                pColor=pointEndColor;
            }else{
                pColor= (int) argbEvaluator.evaluate(i*1f/pointNum,pointStartColor,pointEndColor);
            }
            paint.setColor(pColor);
            int tempX = radius + pointWidth / 2;
            canvas.drawCircle(tempX,0,pointWidth/2,paint);

            canvas.rotate((pointDirection==1?-1:1)*360f/pointNum);
        }

        startAnim();
    }
    private void startAnim(){
        if (valueAnimator == null) {
            valueAnimator=ValueAnimator.ofFloat(0,360f);
        }else{
            valueAnimator.cancel();
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawAngle = (float) animation.getAnimatedValue();
                if(getVisibility()==VISIBLE){
                    invalidate();
                }else if(valueAnimator!=null){
                    valueAnimator.cancel();
                }
            }
        });
        valueAnimator.setDuration(rotationSpeed);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
        invalidate();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility==VISIBLE){
            startAnim();
        }else{
            if(valueAnimator!=null){
                valueAnimator.cancel();
            }
        }
    }

    public int getPointStartColor() {
        return pointStartColor;
    }

    public void setPointStartColor(int pointStartColor) {
        this.pointStartColor = pointStartColor;
    }

    public int getPointEndColor() {
        return pointEndColor;
    }

    public void setPointEndColor(int pointEndColor) {
        this.pointEndColor = pointEndColor;
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        if(pointNum<=0){
            pointNum=1;
        }else if(pointNum>360){
            pointNum=360;
        }
        this.pointNum = pointNum;
    }

    public int getPointWidth() {
        return pointWidth;
    }

    public void setPointWidth(int pointWidth) {
        this.pointWidth = pointWidth;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        if(rotationSpeed<0){
            rotationSpeed=100;
        }
        this.rotationSpeed = rotationSpeed;
    }

    public int getRotationDirection() {
        return rotationDirection;
    }

    public void setRotationDirection(@Direction int rotationDirection) {
        this.rotationDirection = rotationDirection;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getPointDirection() {
        return pointDirection;
    }

    public void setPointDirection(@Direction int pointDirection) {
        this.pointDirection = pointDirection;
    }

    public int getPointStartAngle() {
        return pointStartAngle;
    }

    public void setPointStartAngle(int pointStartAngle) {
        this.pointStartAngle = pointStartAngle;
    }


    private int dp2px(int value) {
        return (int) (getContext().getResources().getDisplayMetrics().density * value);
    }
}
