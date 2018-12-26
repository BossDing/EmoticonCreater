package com.android.emoticoncreater.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.model.FunctionInfo;
import com.android.emoticoncreater.ui.adapter.FunctionListAdapter;
import com.android.emoticoncreater.ui.adapter.IOnListClickListener;
import com.android.emoticoncreater.ui.adapter.OnListClickListener;
import com.android.emoticoncreater.utils.FastClick;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.PermissionsHelper;
import com.android.emoticoncreater.utils.SDCardUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity {

    private RecyclerView rvFunctionList;

    private List<FunctionInfo> mFunctionList;
    private FunctionListAdapter mFunctionAdapter;

    private PermissionsHelper mPermissionsHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();

        mFunctionList = FunctionInfo.createList();
        mFunctionAdapter = new FunctionListAdapter(this, mFunctionList);

        mPermissionsHelper = new PermissionsHelper.Builder()
                .writeExternalStorage()
                .readExternalStorage()
                .setPermissionsResult(mPermissionsResult)
                .bulid();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarTitle(R.string.app_name);

        rvFunctionList = findViewById(R.id.rv_function_list);
        rvFunctionList.setLayoutManager(new LinearLayoutManager(this));
        rvFunctionList.setAdapter(mFunctionAdapter);

        mPermissionsHelper.requestPermissions(this);
    }

    private void setData() {
        mFunctionAdapter.setListClick(mListClick);
        final String basePath = SDCardUtils.getSDCardDir() + Constants.PATH_BASE;
        if (!FileUtils.createdirectory(basePath)) {
            showSnackbar("创建存储目录失败");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hideKeyboard();
        int id = item.getItemId();
        if (id == R.id.action_donate) {
            DonateActivity.show(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private IOnListClickListener mListClick = new OnListClickListener() {
        @Override
        public void onItemClick(int position) {
            final FunctionInfo function = mFunctionList.get(position);
            final String name = function.getName();
            switch (name) {
                case FunctionInfo.NAME_TRIPLE_SEND:
                    TripleSendActivity.show(MainActivity.this);
                    break;
                case FunctionInfo.NAME_SECRET:
                    TellTheSecretActivity.show(MainActivity.this);
                    break;
                case FunctionInfo.NAME_ONE_EMOTICON:
                    OneEmoticonActivity.show(MainActivity.this);
                    break;
                case FunctionInfo.NAME_GIF:
                    GifThemeListActivity.show(MainActivity.this);
                    break;
                case FunctionInfo.NAME_MATURE:
                    MatureActivity.show(MainActivity.this);
                    break;
                case FunctionInfo.NAME_ALL_WICKED:
                    AllWickedActivity.show(MainActivity.this);
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
