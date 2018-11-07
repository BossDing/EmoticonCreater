package com.android.emoticoncreater.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能信息
 */
public class FunctionInfo {

    public static final String NAME_TRIPLE_SEND = "表情三连发";
    public static final String NAME_SECRET = "告诉你个秘密";
    public static final String NAME_ONE_EMOTICON = "一个表情";
    public static final String NAME_GIF = "GIF";
    public static final String NAME_MATURE = "你已经很成熟了";
    public static final String NAME_ALL_WICKED = "全员恶人";

    private String resourceId;//功能图标Id
    private String name;//功能名称

    private FunctionInfo(String name) {
        this.name = name;
    }

    public static List<FunctionInfo> createList() {
        final List<FunctionInfo> functionList = new ArrayList<>();
        functionList.add(new FunctionInfo(NAME_TRIPLE_SEND));
        functionList.add(new FunctionInfo(NAME_SECRET));
        functionList.add(new FunctionInfo(NAME_ONE_EMOTICON));
        functionList.add(new FunctionInfo(NAME_GIF));
        functionList.add(new FunctionInfo(NAME_MATURE));
        functionList.add(new FunctionInfo(NAME_ALL_WICKED));
        return functionList;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
