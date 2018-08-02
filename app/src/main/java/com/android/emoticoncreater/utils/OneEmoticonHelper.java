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

        final Paint paint = new Paint();
        initTextPaint(paint, typeface);

        final Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        final int textWidth = textRect.right;
        final int textHeight = TextUtils.isEmpty(text) ? 0
                : (textWidth <= pictureWidth ? textSize : 2 * textSize + padding / 2);

        initBackgroundPaint(paint);

        final int totalWidth = padding + bitmap.getWidth() + padding;
        final int totalHeight = padding + bitmap.getHeight() + padding + textHeight + padding;

        final Bitmap background = Bitmap.createBitmap(totalWidth, totalHeight, config);
        final Rect backgroundRect = new Rect(0, 0, totalWidth, totalHeight);
        final Canvas canvas = new Canvas(background);
        canvas.drawRect(backgroundRect, paint);

        final Rect pictureRect = new Rect(0, 0, pictureWidth, pictureHeight);
        final RectF dst = new RectF(padding, padding, pictureWidth + padding, padding + pictureHeight);
        canvas.drawBitmap(bitmap, pictureRect, dst, null);
        bitmap.recycle();

        if (textHeight > 0) {
            if (textWidth <= pictureWidth) {
                drawText(canvas, paint, text, padding + pictureHeight + padding, typeface);
            } else {
                final float line = textWidth / (float) pictureWidth;
                final int count = (int) (text.length() / line);
                final String text1 = text.substring(0, count);
                final String text2 = text.substring(count, text.length());

                drawText(canvas, paint, text1, padding + pictureHeight + padding, typeface);
                drawText(canvas, paint, text2,
                        padding + pictureHeight + padding + textSize + padding / 2, typeface);
            }
        }

        final String imageName = System.currentTimeMillis() + ".jpg";
        final File newFile = ImageUtils.saveBitmapToJpg(background, savePath, imageName, quality);
        background.recycle();

        return newFile;
    }

    private static void initBackgroundPaint(final Paint paint) {
        paint.reset();
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
    }

    private static void initTextPaint(final Paint paint, final Typeface typeface) {
        paint.reset();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(typeface);
    }

    private static void drawText(Canvas canvas, Paint paint, String text, int top, Typeface typeface) {
        initTextPaint(paint, typeface);

        final Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        final int textWidth = textRect.right;

        final float textTop = top - textRect.top;
        final float textLeft = (pictureWidth - textWidth) / 2f + padding;
        canvas.drawText(text, textLeft, textTop, paint);
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
