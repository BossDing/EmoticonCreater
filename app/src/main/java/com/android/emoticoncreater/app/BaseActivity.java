package com.android.emoticoncreater.app;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.widget.dialog.ProgressDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;


/**
 * Material Design BaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected CoordinatorLayout mRootView;
    protected AppBarLayout mAppBarLayout;
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;

    private InputMethodManager manager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContentView() != 0) {
            setContentView(getContentView());
        }

        initData();
        initView(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard();
        hideProgress();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initData() {
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void initView(Bundle savedInstanceState) {
        mRootView = findViewById(R.id.rootview);
        mAppBarLayout = findViewById(R.id.appbarlayout);
        mToolbar = findViewById(R.id.toolbar);
        mTabLayout = findViewById(R.id.tablayout);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    protected void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected void setToolbarTitle(@StringRes int stringId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(stringId);
        }
    }

    protected void setToolbarSubTitle(String subTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subTitle);
        }
    }

    protected void setToolbarSubTitle(@StringRes int stringId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(stringId);
        }
    }

    protected void setToolbarBackEnable() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setToolbarScrollFlags(@AppBarLayout.LayoutParams.ScrollFlags int flags) {
        if (mAppBarLayout != null && mAppBarLayout.getChildCount() > 0) {
            AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) mAppBarLayout.getChildAt(0).getLayoutParams();
            mParams.setScrollFlags(flags);
        }
    }

    protected void setTabMode(@TabLayout.Mode int mode) {
        if (mTabLayout != null) {
            mTabLayout.setVisibility(View.VISIBLE);
            mTabLayout.setTabMode(mode);
        }
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        if (mTabLayout != null) {
            mTabLayout.setVisibility(View.VISIBLE);
            mTabLayout.setupWithViewPager(viewPager);
        }
    }

    protected void showProgress(@StringRes int stringId) {
        showProgress(getString(stringId));
    }

    protected void showProgress(String content) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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

    protected void showSnackbar(String content) {
        if (mRootView != null) {
            Snackbar.make(mRootView, content, Snackbar.LENGTH_LONG).show();
        }
    }

    protected void showSnackbar(@StringRes int contentId) {
        showSnackbar(getString(contentId));
    }

    protected void showSnackbar(@StringRes int btnTextId, @StringRes int contentId, View.OnClickListener click) {
        if (mRootView != null) {
            Snackbar.make(mRootView, contentId, Snackbar.LENGTH_LONG)
                    .setAction(btnTextId, click)
                    .show();
        }
    }

    protected void hideKeyboard() {
        final View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            final IBinder windowToken = currentFocus.getWindowToken();
            if (windowToken != null && manager != null) {
                manager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @LayoutRes
    abstract protected int getContentView();

}
