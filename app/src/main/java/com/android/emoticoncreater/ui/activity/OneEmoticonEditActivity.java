package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.model.PictureBean;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.OneEmoticonHelper;
import com.android.emoticoncreater.utils.SDCardUtils;
import com.android.emoticoncreater.utils.ThreadPoolUtil;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;

import java.io.File;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;

public class OneEmoticonEditActivity extends BaseActivity {

    private static final String KEY_ONE_EMOTICON = "key_one_emoticon";

    private ImageView ivPicture;
    private AppCompatEditText etTitle;
    private TextView tvQuality;
    private SwitchCompat swQuality;

    private PictureBean mPicture;
    private String mSavePath;

    public static void show(Activity activity, PictureBean picture) {
        Intent intent = new Intent();
        intent.setClass(activity, OneEmoticonEditActivity.class);
        intent.putExtra(KEY_ONE_EMOTICON, picture);
        activity.startActivity(intent);
    }

    public static void show(Activity activity, ActivityOptions options, PictureBean picture) {
        Intent intent = new Intent();
        intent.setClass(activity, OneEmoticonEditActivity.class);
        intent.putExtra(KEY_ONE_EMOTICON, picture);
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_one_emoticon_edit;
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
    }

    @Override
    protected void initData() {
        super.initData();

        mPicture = getIntent().getParcelableExtra(KEY_ONE_EMOTICON);

        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_ONE_EMOTICON;
        FileUtils.createdirectory(mSavePath);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("编辑表情");
        setToolbarSubTitle("编写表情的文字");

        ivPicture = findViewById(R.id.iv_picture);
        etTitle = findViewById(R.id.et_title);
        tvQuality = findViewById(R.id.tv_quality);
        swQuality = findViewById(R.id.sw_quality);

//        etTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                return event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
//            }
//        });

        swQuality.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tvQuality.setText(isChecked ? "HD画质" : "AV画质");
            }
        });
        swQuality.setChecked(true);

        if (mPicture != null) {
            final String filePath = mPicture.getFilePath();
            final int resourceId = mPicture.getResourceId();

            if (!TextUtils.isEmpty(filePath)) {
                ImageLoaderFactory.getLoader().loadImageFitCenter(this, ivPicture, filePath, 0, 0);
            } else {
                ImageLoaderFactory.getLoader().loadImageFitCenter(this, ivPicture, resourceId, 0, 0);
            }

            ivPicture.setImageResource(resourceId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_done == item.getItemId()) {
            doCreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void doCreate() {
        if (mPicture == null) {
            showSnackbar("图片异常，请返回重新选图");
            return;
        }

        hideKeyboard();
        showProgress("图片处理中...");

        final String text = etTitle.getText().toString();
        mPicture.setTitle(text);

        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {
                final boolean isOriginal = swQuality.isChecked();
                final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
                final File imageFile = OneEmoticonHelper.create(getResources(), mPicture, mSavePath, typeface, isOriginal);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (imageFile.exists()) {
                            final String filePath = imageFile.getAbsolutePath();

                            final Uri uri = Uri.fromFile(imageFile);
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                            ShowPictureActivity.show(OneEmoticonEditActivity.this, filePath);
                        } else {
                            showSnackbar("生成失败，图片不存在");
                        }
                        hideProgress();
                    }
                });
            }
        });
    }
}
