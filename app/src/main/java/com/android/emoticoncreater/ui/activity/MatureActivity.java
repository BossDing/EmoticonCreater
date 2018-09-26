package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.utils.DataCleanManager;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.ImageUtils;
import com.android.emoticoncreater.utils.MatureHelper;
import com.android.emoticoncreater.utils.SDCardUtils;
import com.android.emoticoncreater.utils.ThreadPoolUtil;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;

import java.io.File;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * “你已经很成熟了”功能主界面
 */
public class MatureActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_PICTURE = 100;
    private static final int REQUEST_CODE_CUTE_PICTURE = 101;

    private AppCompatImageView ivPicture;
    private AppCompatEditText etTitle;
    private Button btnSelectSon;

    private File mCutePhotoFile;
    private File mPictureFile;
    private String mTempPath;
    private String mSavePath;

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, MatureActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_mature;
    }

    @Override
    protected void initData() {
        super.initData();

        mTempPath = SDCardUtils.getExternalCacheDir(this);
        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_MATURE;

        FileUtils.createdirectory(mSavePath);
        FileUtils.createdirectory(mTempPath);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("你已经很成熟了");

        ivPicture = findViewById(R.id.iv_picture);
        etTitle = findViewById(R.id.et_title);
        btnSelectSon = findViewById(R.id.btn_select_child);

        btnSelectSon.setOnClickListener(mClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final File cacheDir = new File(mTempPath);
        if (cacheDir.exists()) {
            DataCleanManager.deleteAllFiles(cacheDir);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (REQUEST_CODE_SELECT_PICTURE == requestCode) {
                if (data != null) {
                    final Uri uri = data.getData();
                    if (uri != null) {
                        doCutPicture(uri);
                        return;
                    }
                }
                showSnackbar("图片获取失败");
            } else if (REQUEST_CODE_CUTE_PICTURE == requestCode) {
                if (mCutePhotoFile != null && mCutePhotoFile.exists()) {
                    final String filePath = mCutePhotoFile.getAbsolutePath();
                    doAddPicture(filePath);
                } else {
                    showSnackbar("图片裁剪失败");
                }
            }
        }
    }

    private void doSelectPicture() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        if (pickIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickIntent, REQUEST_CODE_SELECT_PICTURE);
        } else {
            showSnackbar("该系统没有选图工具");
        }
    }

    private void doCutPicture(Uri inputUri) {
        if (inputUri.toString().contains("file://")) {
            final String path = inputUri.getPath();
            final File inputFile = new File(path);
            inputUri = ImageUtils.getImageContentUri(this, inputFile);
        }

        mCutePhotoFile = new File(mTempPath, System.currentTimeMillis() + ".jpg");
        final Uri outputUri = Uri.fromFile(mCutePhotoFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", MatureHelper.HEADER_WIDTH);
        intent.putExtra("outputY", MatureHelper.HEADER_HEIGHT);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);

        if (intent.resolveActivity(getPackageManager()) != null) {  //存在
            startActivityForResult(intent, REQUEST_CODE_CUTE_PICTURE);
        } else {
            showSnackbar("没有系统裁剪图片工具，本功能无法使用");
        }
    }

    private void doAddPicture(final String filePath) {
        showProgress("图片处理中...");

        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {
                mPictureFile = MatureHelper.addPicture(getResources(), filePath, mTempPath);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPictureFile.exists()) {
                            final String filePath = mPictureFile.getAbsolutePath();

                            ImageLoaderFactory.getLoader().loadImage(MatureActivity.this, ivPicture, filePath);
                        } else {
                            showSnackbar("生成失败，图片不存在");
                        }
                        hideProgress();
                    }
                });
            }
        });
    }

    private void doCreate() {
        final String text = etTitle.getText().toString();
        if (mPictureFile == null || !mPictureFile.exists()) {
            showSnackbar("图片不存在，请重新选择儿童头像");
        } else if (TextUtils.isEmpty(text)) {
            showSnackbar("请填写对儿童教导");
        } else {
            showProgress("图片处理中...");

            ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
                @Override
                public void run() {
                    final String pictureFilePath = mPictureFile.getAbsolutePath();
                    final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
                    final File imageFile = MatureHelper.create(pictureFilePath, text, mSavePath, typeface);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (imageFile.exists()) {
                                final String filePath = imageFile.getAbsolutePath();

                                final Uri uri = Uri.fromFile(imageFile);
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                                ShowPictureActivity.show(MatureActivity.this, filePath);
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

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_select_child:
                    doSelectPicture();
                    break;
            }
        }
    };
}
