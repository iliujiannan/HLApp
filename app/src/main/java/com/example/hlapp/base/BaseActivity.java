package com.example.hlapp.base;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;

/**
 * @author 12390
 * @date 2018/8/10
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    public static SharedPreferences mSharedPreferences;
    public final static String SP_NAME = "memory_master_sp";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ActivityCollector.getInstance().addActivity(this);


        mSharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        //初始化加载logo
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
    }
    protected void showMessage(String msg){
        Looper.prepare();
        Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_SHORT).show();
        Looper.loop();
    }


    protected abstract void initComponent();

    public void showLoading() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }
    public void hideLoading() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void setStatusBar(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 设置状态栏底色颜色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(color);

            // 如果亮色，设置状态栏文字为黑色
            if (isLightColor(color)) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }

    }

    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     * @from https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    private boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }
}
