package com.android.emoticoncreater.config;

import java.io.File;

/**
 * 常量
 */

public class Constants {

    public static final String BUGLY_APP_ID = "06b4b6de0f";//TODO 更改Bugly App Id

    public static final String PATH_BASE = File.separator + "表情包生成器" + File.separator;
    public static final String PATH_TRIPLE_SEND = PATH_BASE + "表情三连发" + File.separator;
    public static final String PATH_SECRET = PATH_BASE + "告诉你个秘密" + File.separator;
    public static final String PATH_ONE_EMOTICON = PATH_BASE + "一个表情" + File.separator;
    public static final String PATH_GIF = PATH_BASE + "GIF" + File.separator;
    public static final String PATH_MATURE = PATH_BASE + "你已经很成熟了" + File.separator;

    public static final String KEY_RETURN_DATA = "key_return_data";//ActivityResult

}
