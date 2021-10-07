package com.kunfei.basemvplib;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.kunfei.basemvplib.impl.IPresenter;
import com.kunfei.basemvplib.impl.IView;
import com.monke.basemvplib.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.ButterKnife;

public abstract class BaseActivity<T extends IPresenter> extends AppCompatActivity implements IView {
    public final static String START_SHEAR_ELE = "start_with_share_ele";
    public static final int SUCCESS = 1;
    public static final int ERROR = -1;
    protected Bundle savedInstanceState;
    protected T mPresenter;
    protected boolean isRecreate;
    private Boolean startShareAnim = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        if(getIntent()!=null){
            isRecreate = getIntent().getBooleanExtra("isRecreate", false);
            startShareAnim = getIntent().getBooleanExtra(START_SHEAR_ELE, false);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //禁止横屏幕

        AppActivityManager.getInstance().add(this);
        initSDK();
        setContentView(setLayoutId());
        ButterKnife.bind(setmActivity());
        onCreateActivity();
        mPresenter = initInjector();
        attachView();
        initData();
        bindView();
        bindEvent();
        firstRequest();
    }

    protected abstract Activity setmActivity();

    //layout资源
    protected abstract int setLayoutId();

    /**
     * 首次逻辑操作
     */
    protected void firstRequest() {

    }

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
     * P层绑定V层
     */
    private void attachView() {
        if (null != mPresenter) {
            mPresenter.attachView(this);
        }
    }

    /**
     * P层解绑V层
     */
    private void detachView() {
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    /**
     * SDK初始化
     */
    protected void initSDK() {

    }

    /**
     * P层绑定   若无则返回null;
     */
    protected abstract T initInjector();

    /**
     * 布局载入  setContentView()
     */
    protected abstract void onCreateActivity();

    /**
     * 数据初始化
     */
    protected abstract void initData();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachView();
        AppActivityManager.getInstance().remove(this);
    }

    @Override
    public void recreate() {
        getIntent().putExtra("isRecreate", true);
        super.recreate();
    }

    /////////Toast//////////////////

    public void toast(String msg) {
        toast(msg, Toast.LENGTH_SHORT, 0);
    }

    public void toast(String msg, int state) {
        toast(msg, Toast.LENGTH_LONG, state);
    }

    public void toast(int strId) {
        toast(strId, 0);
    }

    public void toast(int strId, int state) {
        toast(getString(strId), Toast.LENGTH_LONG, state);
    }

    public void toast(String msg, int length, int state) {
        Toast toast = Toast.makeText(this, msg, length);
        try {
            if (state == SUCCESS) {
                toast.getView().getBackground().setColorFilter(getResources().getColor(R.color.success), PorterDuff.Mode.SRC_IN);
            } else if (state == ERROR) {
                toast.getView().getBackground().setColorFilter(getResources().getColor(R.color.error), PorterDuff.Mode.SRC_IN);
            }
        } catch (Exception ignored) {
        }
        toast.show();
    }

    public Context getContext(){
        return this;
    }

    public Boolean getStart_share_ele() {
        return startShareAnim;
    }

    ////////////////////////////////启动Activity转场动画/////////////////////////////////////////////

    protected void startActivityForResultByAnim(Intent intent, int requestCode, int animIn, int animExit) {
        startActivityForResult(intent, requestCode);
        overridePendingTransition(animIn, animExit);
    }

    protected void startActivityByAnim(Intent intent, int animIn, int animExit) {
        startActivity(intent);
        overridePendingTransition(animIn, animExit);
    }

    protected void startActivityByAnim(Intent intent, @NonNull View view, @NonNull String transitionName, int animIn, int animExit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.putExtra(START_SHEAR_ELE, true);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, transitionName);
            startActivity(intent, options.toBundle());
        } else {
            startActivityByAnim(intent, animIn, animExit);
        }
    }

    //隐藏软键盘
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    //获取剪切板内容
    public String GetClipboardStr(){
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        ClipData.Item item = data.getItemAt(0);
        String ClipboardContent = item.getText().toString();
        return ClipboardContent;
    }

    //当前APP的版本号
    public String getVersionName()
    {
        try{
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
            String version = packInfo.versionName;
            return version;
        }catch (Exception e){
            return null;
        }

    }

}