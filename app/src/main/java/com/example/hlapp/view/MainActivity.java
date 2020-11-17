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


    public static final String KEY_IMAGE_PATH = "imagePath";
    /**
     * 相机预览
     */
    private FrameLayout mPreviewLayout;

    private TextView mResultTv;

    /**
     * 相机类
     */
    private Camera mCamera;

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
                        takePhoto();
                    }
                }, error -> {
                });
    }

    @Override
    protected void initComponent() {
        mPreviewLayout = findViewById(R.id.preview_container);
        mResultTv = findViewById(R.id.result);
        mCamera = Camera.open();
        mCamera.getParameters().setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.enableShutterSound(false);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {

            }
        });
        CameraPreview preview = new CameraPreview(MainActivity.this, mCamera);
        mPreviewLayout.addView(preview);
    }


    private void takePhoto() {
        isTakePhoto = true;
        //调用相机拍照
        mCamera.takePicture(null, null, null, (data, camera1) -> {
            recognizer.recognize(data, jsonObject -> {
                mResultTv.setText(jsonObject.getString("result"));
                isTakePhoto = false;
            });
            mCamera.startPreview();
        });
    }
}
