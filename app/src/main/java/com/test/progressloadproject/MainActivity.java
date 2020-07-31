package com.test.progressloadproject;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.github.progressload.ProgressLoad;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ProgressLoad pl;
    private AppCompatSeekBar pointNum;
    private AppCompatSeekBar pointWidth;
    private AppCompatSeekBar rotationSpeed;
    private RadioButton rbCw;
    private RadioButton rbCcw;
    private AppCompatSeekBar radius;
    private RadioButton rbPointCw;
    private RadioButton rbPointCcw;
    private AppCompatSeekBar pointStartAngle;
    private Button bt;
    private Button btVisible;
    private Button btInVisible;
    private Button btGone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        pl = findViewById(R.id.pl);
        pointNum = findViewById(R.id.pointNum);
        pointNum.setOnSeekBarChangeListener(this);
        pointNum.setProgress(pl.getPointNum());

        pointWidth = findViewById(R.id.pointWidth);
        pointWidth.setOnSeekBarChangeListener(this);
        pointWidth.setProgress(pl.getPointWidth());

        rotationSpeed = findViewById(R.id.rotationSpeed);
        rotationSpeed.setOnSeekBarChangeListener(this);
        rotationSpeed.setProgress(pl.getRotationSpeed());

        rbCw = findViewById(R.id.rbCw);
        rbCw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pl.setRotationDirection(isChecked?ProgressLoad.cw:ProgressLoad.ccw);
            }
        });
        rbCw.setChecked(pl.getRotationDirection()==ProgressLoad.cw);

        rbCcw = findViewById(R.id.rbCcw);
        rbCcw.setChecked(pl.getRotationDirection()==ProgressLoad.ccw);

        radius = findViewById(R.id.radius);
        radius.setOnSeekBarChangeListener(this);
        radius.setProgress(pl.getRadius());

        rbPointCw = findViewById(R.id.rbPointCw);
        rbPointCw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pl.setPointDirection(isChecked?ProgressLoad.cw:ProgressLoad.ccw);
            }
        });
        rbPointCw.setChecked(pl.getPointDirection()==ProgressLoad.cw);

        rbPointCcw = findViewById(R.id.rbPointCcw);
        rbPointCcw.setChecked(pl.getPointDirection()==ProgressLoad.ccw);

        pointStartAngle = findViewById(R.id.pointStartAngle);
        pointStartAngle.setOnSeekBarChangeListener(this);
        pointStartAngle.setProgress(pl.getPointStartAngle());


        bt = findViewById(R.id.bt);
        bt.setOnClickListener(this);

        btVisible = findViewById(R.id.btVisible);
        btVisible.setOnClickListener(this);

        btInVisible = findViewById(R.id.btInVisible);
        btInVisible.setOnClickListener(this);

        btGone = findViewById(R.id.btGone);
        btGone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                logStr();
                pl.complete();
                break;
            case R.id.btVisible:
                pl.setVisibility(View.VISIBLE);
                break;
            case R.id.btInVisible:
                pl.setVisibility(View.INVISIBLE);
                break;
            case R.id.btGone:
                pl.setVisibility(View.GONE);
                break;
        }
    }

    private void logStr() {
        int pointNum = pl.getPointNum();
        int pointWidth = pl.getPointWidth();
        int rotationSpeed = pl.getRotationSpeed();
        int rotationDirection = pl.getRotationDirection();
        int radius = pl.getRadius();
        int pointDirection = pl.getPointDirection();
        int pointStartAngle = pl.getPointStartAngle();
        Log.i("=====","===================================");
        Log.i("=====","pointNum====="+pointNum);
        Log.i("=====","pointWidth====="+pointWidth);
        Log.i("=====","rotationSpeed====="+rotationSpeed);
        Log.i("=====","rotationDirection====="+rotationDirection);
        Log.i("=====","radius====="+radius);
        Log.i("=====","pointDirection====="+pointDirection);
        Log.i("=====","pointStartAngle====="+pointStartAngle);
        Log.i("=====","===================================");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.pointNum:
                pl.setPointNum(progress);
                break;
            case R.id.pointWidth:
                pl.setPointWidth(progress);
                break;
            case R.id.rotationSpeed:
                pl.setRotationSpeed(progress);
                break;
            case R.id.radius:
                pl.setRadius(progress);
                break;
            case R.id.pointStartAngle:
                pl.setPointStartAngle(progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
