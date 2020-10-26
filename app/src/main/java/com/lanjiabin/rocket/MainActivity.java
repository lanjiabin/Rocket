package com.lanjiabin.rocket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * jiabin.lan
 * */
public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private Button mCreateWndBtn, mRmvWndBtn;
    private ImageView mImageView;
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager mWindowManager;

    int mDownX = 0;
    int mDownY = 0;

    int mWidthPixel = 0;
    int mHeightPixel = 0;

    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                initView();
            }
        }
    }

    public void initView() {
        mCreateWndBtn = findViewById(R.id.add_btn);
        mRmvWndBtn = findViewById(R.id.rmv_btn);
        mCreateWndBtn.setOnClickListener(this);
        mRmvWndBtn.setOnClickListener(this);
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        mWidthPixel = outMetrics.widthPixels;
        mHeightPixel = outMetrics.heightPixels;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_btn) {
            mImageView = new ImageView(this);
            mImageView.setId(R.id.imageView_view_1);
            mImageView.setBackgroundResource(R.mipmap.ic_launcher);

            mLayoutParams =
                    new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            2099,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                            PixelFormat.TRANSPARENT);
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            mLayoutParams.x = mWidthPixel;
            mLayoutParams.y = mHeightPixel / 2;
            mImageView.setOnTouchListener(this);
            mWindowManager.addView(mImageView, mLayoutParams);
        } else if (v.getId() == R.id.rmv_btn) {
            mWindowManager.removeViewImmediate(mImageView);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.button_view_1: {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mDownX = (int) (event.getRawX() + 0.5f);
                        mDownY = (int) (event.getRawY() + 0.5f);
                    }
                    case MotionEvent.ACTION_MOVE: {
                        mLayoutParams.x = (int) event.getRawX();
                        mLayoutParams.y = (int) event.getRawY();
                        if (mButton!=null){
                            mWindowManager.updateViewLayout(mButton, mLayoutParams);
                        }
                        break;
                    }
                    default:
                        break;
                }

            }
            break;

            case R.id.imageView_view_1: {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mDownX = (int) (event.getRawX() + 0.5f);
                        mDownY = (int) (event.getRawY() + 0.5f);
                    }
                    case MotionEvent.ACTION_MOVE: {
                        mLayoutParams.x = (int) event.getRawX();
                        mLayoutParams.y = (int) event.getRawY();
                        mWindowManager.updateViewLayout(mImageView, mLayoutParams);
                        break;
                    }
                    default:
                        break;
                }
                if (mLayoutParams.x >= mWidthPixel >> 1 && mDownX - mLayoutParams.x > (mWidthPixel >> 1) * 0.65) {
                    Toast.makeText(MainActivity.this, "向左滑动了 " + (mDownX - mLayoutParams.x) + "距离", Toast.LENGTH_LONG).show();

                    mLayoutParams.x = mWidthPixel;
                    mLayoutParams.y = mHeightPixel / 3;
                    if (mButton==null){
                        mButton=new Button(this);
                        mButton.setText("我是被拖拉出来的");
                        mButton.setId(R.id.button_view_1);
                        mButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        mButton.setWidth(500);
                        mButton.setHeight(mHeightPixel);
                        mButton.setOnTouchListener(this);
                        mWindowManager.addView(mButton,mLayoutParams);
                    }
                }
            }
            break;
            default:
                break;
        }

        return false;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            initView();
        }
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(MainActivity.this, "not granted", Toast.LENGTH_SHORT);
                    mWindowManager.addView(mImageView, mLayoutParams);
                }
            }
        }
    }
}
