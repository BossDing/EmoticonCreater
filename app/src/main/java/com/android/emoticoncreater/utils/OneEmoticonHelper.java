package com.android.emoticoncreater.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import com.android.emoticoncreater.model.PictureBean;

import java.io.File;

/**
 * 一个表情图标编辑帮助类
 */
public class OneEmoticonHelper {

    private static final int padding = 20;//内边距
    private static final int pictureWidth = 300;//图片宽度
    private static final int textSize = 30;//字体大小
    private static final int backgroundColor = 0xffffffff;
    private static final int textColor = 0xff010101;

    public static File create(Resources resources, final PictureBean emoticon,
                              final String savePath, final Typeface typeface, boolean isOriginal) {
        final int quality = isOriginal ? 100 : 5;
        final Bitmap.Config config = isOriginal ? Bitmap.Config.ARGB_8888 : Bitmap.Config.ARGB_4444;

        final String text = emoticon.getTitle();

        final int resourceId = emoticon.getResourceId();
        final String filePath = emoticon.getFilePath();
        final Bitmap bitmap;
        if (!TextUtils.isEmpty(filePath)) {
            bitmap = getBitmapByFilePath(filePath);
        } else {
            bitmap = getBitmapByResourcesId(resources, resourceId);
        }

        final int pictureWidth = bitmap.getWidth();
        final int pictureHeight = bitmap.getHeight();

        final TextPaint textPaint = createTextPaint(typeface);
        final StaticLayout currentLayout = new StaticLayout(text, textPaint, pictureWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        final int totalWidth = padding + pictureWidth + padding;
        final int totalHeight = padding + pictureHeight + padding + currentLayout.getHeight() + padding;

        final Bitmap background = Bitmap.createBitmap(totalWidth, totalHeight, config);
        final Rect backgroundRect = new Rect(0, 0, totalWidth, totalHeight);
        final Canvas canvas = new Canvas(background);
        final Paint backgroundPaint = createBackgroundPaint();
        canvas.drawRect(backgroundRect, backgroundPaint);

        final Rect pictureRect = new Rect(0, 0, pictureWidth, pictureHeight);
        final RectF dst = new RectF(padding, padding, pictureWidth + padding, padding + pictureHeight);
        canvas.drawBitmap(bitmap, pictureRect, dst, null);
        bitmap.recycle();

        canvas.translate(totalWidth / 2, padding + pictureHeight + padding);
        currentLayout.draw(canvas);

        final String imageName = System.currentTimeMillis() + ".jpg";
        final File newFile = ImageUtils.saveBitmapToJpg(background, savePath, imageName, quality);
        background.recycle();

        return newFile;
    }

    private static TextPaint createTextPaint(Typeface typeface) {
        final TextPaint textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTypeface(typeface);
        return textPaint;
    }

    private static Paint createBackgroundPaint() {
        final Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.FILL);
        return backgroundPaint;
    }

    private static Bitmap getBitmapByFilePath(String filePath) {
        final Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return setScale(bitmap);
    }

    private static Bitmap getBitmapByResourcesId(Resources resources, int resourceId) {
        final Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
        return setScale(bitmap);
    }

    private static Bitmap setScale(Bitmap bitmap) {
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();

        if (bitmapWidth != pictureWidth) {
            float scale = ((float) pictureWidth) / bitmapWidth;

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

            bitmap.recycle();

            return resizedBitmap;
        }

        return bitmap;
    }
}
