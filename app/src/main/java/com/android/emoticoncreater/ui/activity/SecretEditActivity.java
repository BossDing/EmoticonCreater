package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.model.PictureInfo;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 编辑秘密
 */

public class SecretEditActivity extends BaseActivity {

    private static final String KEY_SECRET = "key_secret";

    private AppCompatImageView ivPicture;
    private AppCompatEditText etTitle;

    private PictureInfo mSecret;

    public static void show(Activity activity, ActivityOptions options, PictureInfo secret) {
        Intent intent = new Intent();
        intent.setClass(activity, SecretEditActivity.class);
        intent.putExtra(KEY_SECRET, secret);
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_secret_edit;
    }

    @Override
    protected void initData() {
        super.initData();
        mSecret = getIntent().getParcelableExtra(KEY_SECRET);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("添加秘密");

        ivPicture = (AppCompatImageView) findViewById(R.id.iv_picture);
        etTitle = (AppCompatEditText) findViewById(R.id.et_title);

        if (mSecret != null) {
            final int resourceId = mSecret.getResourceId();
            final String title = mSecret.getTitle();

            ImageLoaderFactory.getLoader().loadImageFitCenter(this, ivPicture, resourceId, 0, 0);
            ivPicture.setImageResource(resourceId);
            etTitle.setHint(title);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_done == item.getItemId()) {
            doAdd();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void doAdd() {
        final String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showSnackbar("写下你的秘密");
        } else {
            hideKeyboard();
            mSecret.setTitle(title);

            TellTheSecretActivity.showOnNewIntent(this, mSecret);
        }
    }
}
