package com.android.emoticoncreater.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.utils.FastClick;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.PermissionsHelper;
import com.android.emoticoncreater.utils.SDCardUtils;

public class MainActivity extends BaseActivity {

    private Button btnTripleSend;
    private Button btnSecret;
    private Button btnOneEmoticon;
    private Button btnGif;
    private Button btnMature;

    private PermissionsHelper mPermissionsHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();

        mPermissionsHelper = new PermissionsHelper.Builder()
                .readExternalStorage()
                .bulid();
        mPermissionsHelper.setPermissionsResult(mPermissionsResult);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarTitle(R.string.app_name);

        btnTripleSend = findViewById(R.id.btn_triple_send);
        btnSecret = findViewById(R.id.btn_secret);
        btnOneEmoticon = findViewById(R.id.btn_one_emoticon);
        btnGif = findViewById(R.id.btn_gif);
        btnMature = findViewById(R.id.btn_mature);

        mPermissionsHelper.requestPermissions(this);
    }

    private void setData() {
        btnTripleSend.setOnClickListener(mClick);
        btnSecret.setOnClickListener(mClick);
        btnOneEmoticon.setOnClickListener(mClick);
        btnGif.setOnClickListener(mClick);
        btnMature.setOnClickListener(mClick);

        final String basePath = SDCardUtils.getSDCardDir() + Constants.PATH_BASE;
        if (!FileUtils.createdirectory(basePath)) {
            showSnackbar("创建存储目录失败");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsHelper.requestPermissionsResult(this, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionsHelper.activityResult(this, requestCode);
    }

    @Override
    public void onBackPressed() {
        if (!FastClick.isExitClick()) {
            showSnackbar("再次点击退出程序");
        } else {
            super.onBackPressed();
        }
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_triple_send:
                    TripleSendActivity.show(MainActivity.this);
                    break;
                case R.id.btn_secret:
                    TellTheSecretActivity.show(MainActivity.this);
                    break;
                case R.id.btn_one_emoticon:
                    OneEmoticonActivity.show(MainActivity.this);
                    break;
                case R.id.btn_gif:
                    GifThemeListActivity.show(MainActivity.this);
                    break;
                case R.id.btn_mature:
                    MatureActivity.show(MainActivity.this);
                    break;
            }
        }
    };

    private PermissionsHelper.OnPermissionsResult mPermissionsResult = new PermissionsHelper.OnPermissionsResult() {
        @Override
        public void allPermissionGranted() {
            setData();
        }

        @Override
        public void cancelToSettings() {
            finish();
        }
    };
}
