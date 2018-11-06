package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.model.PictureInfo;
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
    private TextView tvTextSize;
    private SeekBar sbTextSize;

    private PictureInfo mPicture;
    private String mSavePath;

    public static void show(Activity activity, PictureInfo picture) {
        Intent intent = new Intent();
        intent.setClass(activity, OneEmoticonEditActivity.class);
        intent.putExtra(KEY_ONE_EMOTICON, picture);
        activity.startActivity(intent);
    }

    public static void show(Activity activity, ActivityOptions options, PictureInfo picture) {
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
        tvTextSize = findViewById(R.id.tv_text_size);
        sbTextSize = findViewById(R.id.sb_text_size);

        swQuality.setOnCheckedChangeListener(mCheckChange);
        swQuality.setChecked(true);

        sbTextSize.setOnSeekBarChangeListener(mSeekBarChange);

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

        ivPicture.post(new Runnable() {
            @Override
            public void run() {
                setTextSize(0);
            }
        });
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

    private void setTextSize(int progress) {
        final int picutreWidth = ivPicture.getWidth();
        final float scale = picutreWidth / 300f;

        final int textSizePx = progress + 30;
        final String textSize = "字体:" + textSizePx;
        tvTextSize.setText(textSize);
        etTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx * scale);
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
                final int textSizePx = sbTextSize.getProgress() * 2 + 30;
                final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");

                final OneEmoticonHelper helper = new OneEmoticonHelper.Builder(getResources())
                        .pictureInfo(mPicture)
                        .savePath(mSavePath)
                        .typeFace(typeface)
                        .isOriginal(isOriginal)
                        .textSize(textSizePx)
                        .bulid();

                final File imageFile = helper.create();

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

    private CompoundButton.OnCheckedChangeListener mCheckChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            final int id = buttonView.getId();
            if (R.id.sw_quality == id) {
                tvQuality.setText(isChecked ? "HD画质" : "AV画质");
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener mSeekBarChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progress *= 2;
            setTextSize(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
