package com.example.hlapp.util;

import com.baidu.aip.imageclassify.AipImageClassify;

import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ImageRecognizer {
    private final static String API_KEY = "6THGWMzEjwbDycfGCMx60DU8";
    private final static String APP_ID = "23000386";
    private final static String SECRET_KEY = "Tti0AtptsfjwPMMlGYvlnc3NHA5uDbXu";

    public static void recognize(byte[] imageData, Consumer<JSONObject> consumer) {
        Single.create((SingleOnSubscribe<JSONObject>) emitter -> {
            AipImageClassify classify = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
            classify.setConnectionTimeoutInMillis(2000);
            classify.setSocketTimeoutInMillis(60000);
            JSONObject object = classify.advancedGeneral(imageData, new HashMap());
            System.out.println(object.toString());
            emitter.onSuccess(object);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    consumer.accept(res);
                }, e -> {

                });
    }
}
