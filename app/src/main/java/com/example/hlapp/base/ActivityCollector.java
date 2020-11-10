package com.example.hlapp.base;

import android.app.Activity;
import android.app.Application;

import java.util.List;
import java.util.Vector;

/**
 * Created by dy on 2016/10/18.
 */

/**
 * 管理所有后台activity, 初始化GreenDao
 *
 * @author Administrator
 */
public class ActivityCollector extends Application {
    //运用list来保存们每一个activity是关键
    private List<Activity> mList = new Vector<>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static ActivityCollector instance;



    //构造方法
    private ActivityCollector() {
    }

    //实例化一次
    public synchronized static ActivityCollector getInstance() {
        if (null == instance) {
            instance = new ActivityCollector();
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


}
