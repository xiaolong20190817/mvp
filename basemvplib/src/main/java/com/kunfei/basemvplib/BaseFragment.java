package com.kunfei.basemvplib;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.kunfei.basemvplib.impl.IPresenter;
import com.kunfei.basemvplib.impl.IView;

import java.util.Objects;

import butterknife.ButterKnife;

import static com.kunfei.basemvplib.BaseActivity.START_SHEAR_ELE;

public abstract class BaseFragment<T extends IPresenter> extends Fragment implements IView {
    protected View view;
    protected Bundle savedInstanceState;
    protected AppCompatActivity mActivity;
    protected LayoutInflater mInflater;
    public BaseFragment() {
        //Fragment必须有一个无参public的构造函数，否则在数据恢复的时候，会报crash
        // doesn't do anything special
    }
    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        initSDK();
        view = createView(inflater, container);
        mActivity = (AppCompatActivity) getActivity();
        setContentView(setLayoutId());
        ButterKnife.bind(setmActivity());
        initData();
        bindView();
        bindEvent();
        firstRequest();
        return view;
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        initSDK();
        mInflater = inflater;
        view = inflater.inflate(initlayoutid(), container, false);
        mActivity = (AppCompatActivity) getActivity();
        ButterKnife.bind(this,view);  //这里一定要在SetContentView后添加
        initData();
        bindView();
        bindEvent();
        firstRequest();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        created();
    }

    protected abstract void created();
    protected abstract int initlayoutid();

    /**
     * 事件触发绑定
     */
    protected void bindEvent() {

    }

    /**
     * 控件绑定
     */
    protected void bindView() {

    }

    /**
     * 数据初始化
     */
    protected void initData() {

    }

    /**
     * 首次逻辑操作
     */
    protected void firstRequest() {

    }

    /**
     * 加载布局
     */
    protected abstract View createView(LayoutInflater inflater, ViewGroup container);

    /**
     * 第三方SDK初始化
     */
    protected void initSDK() {

    }

    protected void startActivityByAnim(Intent intent, int animIn, int animExit) {
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(animIn, animExit);
    }

    protected void startActivityByAnim(Intent intent, @NonNull View view, @NonNull String transitionName, int animIn, int animExit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.putExtra(START_SHEAR_ELE, true);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, transitionName);
            startActivity(intent, options.toBundle());
        } else {
            startActivityByAnim(intent, animIn, animExit);
        }
    }
}
