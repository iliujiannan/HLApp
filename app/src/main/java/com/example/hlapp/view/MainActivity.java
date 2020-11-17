package com.example.hlapp.view;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.hlapp.R;
import com.example.hlapp.base.BaseActivity;
import com.example.hlapp.util.ImageRecognizer;
import com.example.hlapp.util.PermissionUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {
    /**
     * 相机预览
     */
    private CameraPreview cameraPreview;

    private TextView mResultTv;

    private boolean isTakePhoto = false;

    private ImageRecognizer recognizer = new ImageRecognizer();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.applicationPermissions(this, new PermissionUtils.PermissionListener() {
            @Override
            public void onSuccess(Context context) {
                initComponent();
                startClock();
            }

            @Override
            public void onFailed(Context context) {
                if (AndPermission.hasAlwaysDeniedPermission(context, Permission.Group.CAMERA)
                        && AndPermission.hasAlwaysDeniedPermission(context, Permission.Group.STORAGE)) {
                    AndPermission.with(context).runtime().setting().start();
                }
                Toast.makeText(context, "申请权限失败", Toast.LENGTH_SHORT).show();
            }
        }, Permission.Group.STORAGE, Permission.Group.CAMERA);
    }

    private void startClock() {
        Disposable disposable = Observable.interval(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (!isTakePhoto) {
                        isTakePhoto = true;
                        cameraPreview.takePhoto(img -> {
                            recognizer.recognize(img, result->{
                                mResultTv.setText("识别结果：\n" + result.getString("result"));
                                isTakePhoto = false;
                            });
                        });
                    }
                }, error -> {
                });
    }

    @Override
    protected void initComponent() {
        FrameLayout layout = findViewById(R.id.preview_container);
        cameraPreview = new CameraPreview(MainActivity.this);
        layout.addView(cameraPreview);
        mResultTv = findViewById(R.id.result);
    }
}
