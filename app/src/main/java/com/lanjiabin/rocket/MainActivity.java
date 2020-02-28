package com.lanjiabin.rocket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private Button mCreateWndBtn, mRmvWndBtn;
    private ImageView mImageView;
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager mWindowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
                Log.v("showLog", "23---");
            } else {
                //TODO 做你需要的事情
                Log.v("showLog", "23---TODO");
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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_btn) {
            mImageView = new ImageView(this);
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
            mLayoutParams.x = 0;
            mLayoutParams.y = 300;
            mImageView.setOnTouchListener(this);
            Log.v("showLog", "23---addView");
            mWindowManager.addView(mImageView, mLayoutParams);
        } else if (v.getId() == R.id.rmv_btn) {
            mWindowManager.removeViewImmediate(mImageView);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                mLayoutParams.x = rawX;
                mLayoutParams.y = rawY - (mImageView.getHeight()) / 2;
                Log.v("showLog","mLayoutParams.x="+mLayoutParams.x+"  mLayoutParams.y="+rawY);
                mWindowManager.updateViewLayout(mImageView, mLayoutParams);
                break;
            }
            default:
                break;
        }
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
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
        Log.v("showLog", "23---onActivityResult");
        if (requestCode == 100) {
            Log.v("showLog", "23---requestCode=100");
            initView();
        }
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    Log.v("showLog", "23---requestCode=1");
                    Toast.makeText(MainActivity.this, "not granted", Toast.LENGTH_SHORT);
                    mWindowManager.addView(mImageView, mLayoutParams);
                }
            }
        }
    }
}
