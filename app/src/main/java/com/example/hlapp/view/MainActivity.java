package com.example.hlapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.hlapp.R;
import com.example.hlapp.base.BaseActivity;
import com.example.hlapp.util.ImageUtil;
import com.example.hlapp.util.PermissionUtils;
import com.example.hlapp.util.ResUtilKt;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {
    /**
     * 相机预览
     */
    private CameraPreview cameraPreview;

    private TextView mResultTv;

    private int times = 0;

    private CompositeDisposable compositeDisposable;

    private boolean isRecognizing = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compositeDisposable = new CompositeDisposable();
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
        compositeDisposable.add(Observable.interval(3, 5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (!isRecognizing) {
                        isRecognizing = true;
                        times++;
                        cameraPreview.takePhoto(img -> {
                            Bitmap originBitmap = ImageUtil.byteArray2Bitmap(img);
                            Bitmap bitmap = ImageUtil.cutImage(originBitmap, 0,
                                    (int) ResUtilKt.dp2Px(this, 220),
                                    originBitmap.getWidth(),
                                    ResUtilKt.getScreenHeight(this) - (int) ResUtilKt.dp2Px(this, 220 + 220));
                            byte[] image2 = ImageUtil.bitmap2ByteArray(bitmap);
                            ImageUtil.recognize(image2,
                                    result -> {
                                        isRecognizing = false;
                                        String strResult = result.optString("result", "识别错误");
                                        mResultTv.setText(getResources().getString(
                                                R.string.hlapp_result, times, strResult));
                                        if (strResult.contains("舌")) {
                                            String path = ImageUtil.saveImage(this, bitmap);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("path", path);
                                            SecondActivity.Companion.start(this, bundle);
                                            finish();
                                        }
                                    });
                        });
                    }
                }, error -> {
                }));

    }

    protected void initComponent() {
        FrameLayout layout = findViewById(R.id.preview_container);
        cameraPreview = new CameraPreview(MainActivity.this);
        layout.addView(cameraPreview);
        mResultTv = findViewById(R.id.result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
