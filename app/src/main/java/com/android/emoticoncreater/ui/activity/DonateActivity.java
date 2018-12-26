package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.ImageUtils;
import com.android.emoticoncreater.utils.SDCardUtils;
import com.android.emoticoncreater.utils.ThreadPoolUtil;

import java.io.File;

/**
 * 捐赠
 */
public class DonateActivity extends BaseActivity {

    private Button btnAlipay;
    private Button btnWechat;

    private String mSavePath;

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, DonateActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_donate;
    }

    @Override
    protected void initData() {
        super.initData();

        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_DONATE;
        FileUtils.createdirectory(mSavePath);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("捐赠");

        btnAlipay = findViewById(R.id.btn_alipay);
        btnWechat = findViewById(R.id.btn_wechat);

        btnAlipay.setOnClickListener(mClick);
        btnWechat.setOnClickListener(mClick);
    }

    private void doSavePicture(final int pictureResourceId, final String fileName) {
        showProgress("保存中...");

        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), pictureResourceId);
                final File pictureFile = ImageUtils.saveBitmapToJpg(bitmap, mSavePath, fileName, 100);
                bitmap.recycle();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pictureFile != null && pictureFile.exists()) {
                            final String filePath = pictureFile.getAbsolutePath();

                            final Uri uri = Uri.fromFile(pictureFile);
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                            showSnackbar("保存路径：" + filePath);
                        } else {
                            showSnackbar("保存失败，请联系开发者");
                        }
                        hideProgress();
                    }
                });
            }
        });
    }

    private final View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_alipay:
                    doSavePicture(R.raw.img_alipay, Constants.NAME_ALIPAY);
                    break;
                case R.id.btn_wechat:
                    doSavePicture(R.raw.img_wechatpay, Constants.NAME_WECHAT);
                    break;
            }
        }
    };
}
