package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.utils.AllWickedHelper;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.SDCardUtils;
import com.android.emoticoncreater.utils.ThreadPoolUtil;

import java.io.File;

public class AllWickedActivity extends BaseActivity {

    private EditText etDescritption;
    private EditText etAClothesText;
    private EditText etBClothesWord;
    private EditText etAAsk;
    private EditText etBReply;
    private EditText etBClothesText;

    private String mSavePath;

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AllWickedActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_all_wicked;
    }

    @Override
    protected void initData() {
        super.initData();

        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_ALL_WICKED;
        FileUtils.createdirectory(mSavePath);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("全员恶人");

        etDescritption = findViewById(R.id.et_descritption);
        etAClothesText = findViewById(R.id.et_a_clothes_text);
        etBClothesWord = findViewById(R.id.et_b_clothes_word);
        etAAsk = findViewById(R.id.et_a_ask);
        etBReply = findViewById(R.id.et_b_reply);
        etBClothesText = findViewById(R.id.et_b_clothes_text);
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
        hideKeyboard();
        showProgress("图片处理中...");

        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {
                String description = etDescritption.getText().toString();
                String aClothesText = etAClothesText.getText().toString();
                String bClothesWord = etBClothesWord.getText().toString();
                String aAsk = etAAsk.getText().toString();
                String bReply = etBReply.getText().toString();
                String bClothesText = etBClothesText.getText().toString();

                if (TextUtils.isEmpty(description)) {
                    description = etDescritption.getHint().toString();
                }
                if (TextUtils.isEmpty(aClothesText)) {
                    aClothesText = etAClothesText.getHint().toString();
                }
                if (TextUtils.isEmpty(bClothesWord)) {
                    bClothesWord = etBClothesWord.getHint().toString();
                }
                if (TextUtils.isEmpty(aAsk)) {
                    aAsk = etAAsk.getHint().toString();
                }
                if (TextUtils.isEmpty(bReply)) {
                    bReply = etBReply.getHint().toString();
                }
                if (TextUtils.isEmpty(bClothesText)) {
                    bClothesText = etBClothesText.getHint().toString();
                }

                final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
                final AllWickedHelper helper = new AllWickedHelper.Builder(getResources())
                        .description(description)
                        .aClothesText(aClothesText)
                        .bClothesWord(bClothesWord)
                        .aAskText(aAsk)
                        .bReplyText(bReply)
                        .bClothesText(bClothesText)
                        .savePath(mSavePath)
                        .typeFace(typeface)
                        .bulid();

                final File imageFile = helper.createList();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (imageFile.exists()) {
                            final String filePath = imageFile.getAbsolutePath();

                            final Uri uri = Uri.fromFile(imageFile);
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                            ShowPictureActivity.show(AllWickedActivity.this, filePath);
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
