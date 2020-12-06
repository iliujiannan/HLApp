package com.example.hlapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ImageUtil {
    private final static String API_KEY = "6THGWMzEjwbDycfGCMx60DU8";
    private final static String APP_ID = "23000386";
    private final static String SECRET_KEY = "Tti0AtptsfjwPMMlGYvlnc3NHA5uDbXu";

    public static Disposable recognize(byte[] imageData, Consumer<JSONObject> consumer) {
        return Single.create((SingleOnSubscribe<JSONObject>) emitter -> {
            AipImageClassify classify = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
            classify.setConnectionTimeoutInMillis(2000);
            classify.setSocketTimeoutInMillis(60000);
            JSONObject object = classify.advancedGeneral(imageData, new HashMap<>());
            System.out.println(object.toString());
            emitter.onSuccess(object);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, Throwable::printStackTrace);
    }

    public static byte[] bitmap2ByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap byteArray2Bitmap(byte[] img) {
        return adjustPhotoRotation(BitmapFactory.decodeByteArray(img, 0, img.length), 270);
    }

    public static Bitmap cutImage(Bitmap bitmap, int x, int y, int w, int h) {
        return Bitmap.createBitmap(bitmap, x, y, w, h);
    }

    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

        } catch (OutOfMemoryError ex) {
        }
        return bm;
    }

    public static String saveImage(Context context, Bitmap bitmap) {
        String path = ResUtilKt.getCacheRootPath(context) + "/test.jpg";
        File file = new File(path);
        if (file.exists()) {
            boolean result = file.delete();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static void loadLocalImage(SimpleDraweeView view, String filePath) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.autoRotate())
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(view.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                        if (imageInfo == null) {
                            return;
                        }
                        view.requestLayout();
                    }
                })
                .build();
        view.setController(controller);
    }
}
