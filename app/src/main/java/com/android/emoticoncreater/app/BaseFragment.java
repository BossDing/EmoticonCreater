package com.android.emoticoncreater.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.android.emoticoncreater.ui.dialog.ProgressDialog;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

/**
 * 基类
 */

public abstract class BaseFragment extends Fragment {

    protected View mFragmentView;

    private ProgressDialog mProgressDialog;
    private InputMethodManager manager;

    protected Activity mActivity;

    protected void initData(@Nullable Bundle savedInstanceState) {
        manager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(getLayoutId(), container, false);

        initView(savedInstanceState);

        return mFragmentView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
    }

    protected void showProgress(@StringRes int stringId) {
        showProgress(getString(stringId));
    }

    protected void showProgress(String content) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.setMessage(content);
        mProgressDialog.show();
    }

    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void hideKeyboard() {
        final View currentFocus = mActivity.getCurrentFocus();
        if (currentFocus != null) {
            final IBinder windowToken = currentFocus.getWindowToken();
            if (windowToken != null && manager != null) {
                manager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @LayoutRes
    abstract protected int getLayoutId();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(), "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e(this.getClass().getSimpleName(), "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e(this.getClass().getSimpleName(), "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e(this.getClass().getSimpleName(), "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(this.getClass().getSimpleName(), "onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.e(this.getClass().getSimpleName(), "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean getUserVisibleHint() {
        Log.e(this.getClass().getSimpleName(), "getUserVisibleHint");
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(this.getClass().getSimpleName(), "setUserVisibleHint:" + isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(this.getClass().getSimpleName(), "onHiddenChanged:" + hidden);
    }
}
