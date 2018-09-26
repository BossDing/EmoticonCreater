package com.android.emoticoncreater.ui.adapter;

import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * adapter 点击回调接口
 */

public interface IOnListClickListener {

    @IntDef({ITEM_TAG0, ITEM_TAG1, ITEM_TAG2})
    @Retention(RetentionPolicy.SOURCE)
    @interface ItemView {
    }

    int ITEM_TAG0 = 0;
    int ITEM_TAG1 = 1;
    int ITEM_TAG2 = 2;


    //item点击事件
    void onItemClick(int position);

    //item点击事件，返回model
    void onItemClick(Object object);

    void onItemClick(View view, int position);

    void onItemClick(View view, Object object);

    //可根据tag来区分点击的是item内部哪个控件
    void onTagClick(@ItemView int tag, int position);

}
