package com.example.hlapp.base;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by 12390 on 2018/8/14.
 */
public abstract class BaseFragment extends Fragment implements IBaseView{
    public abstract int getContentViewId();
    protected abstract void initAllMembersView(View rootView);
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected View mRootView;
    public static SharedPreferences mSharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        this.mContext = getActivity();
        this.mInflater = inflater;
        initAllMembersView(mRootView);
        mSharedPreferences = getActivity().getSharedPreferences(BaseActivity.SP_NAME, getActivity().MODE_PRIVATE);
        return mRootView;
    }
    @Override
    public void showLoading() {
        checkActivityAttached();
        ((BaseActivity) mContext).showLoading();
    }
    @Override
    public void hideLoading() {
        checkActivityAttached();
        ((BaseActivity) mContext).hideLoading();
    }

    @Override
    public void showErr() {
        checkActivityAttached();
        ((BaseActivity) mContext).showErr();
    }
    protected boolean isAttachedContext(){
        return getActivity() != null;
    }
    /**
     * 检查activity连接情况
     */
    public void checkActivityAttached() {
        if (getActivity() == null) {
            throw new ActivityNotAttachedException();
        }
    }
    public static class ActivityNotAttachedException extends RuntimeException {
        public ActivityNotAttachedException() {
            super("Fragment has disconnected from Activity ! - -.");
        }
    }

    @Override
    public void onActionSucc(BaseModel result) {

    }

    @Override
    public void onActionFailed(String msg) {
        showMessage(msg);
    }

    protected void showMessage(String msg){
        Looper.prepare();
        Toast.makeText(getActivity(), msg,
                Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    protected void showMessageWithoutLooper(String msg){
        Toast.makeText(getActivity(), msg,
                Toast.LENGTH_SHORT).show();
    }
}
