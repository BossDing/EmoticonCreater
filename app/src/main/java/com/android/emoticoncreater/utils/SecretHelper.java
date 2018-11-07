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

import com.android.emoticoncreater.model.PictureInfo;

import java.io.File;
import java.util.List;

/**
 * 告诉你个秘密工具类
 */

public class SecretHelper {

    private static final int padding = 20;//内边距
    private static final int pictureWidth = 500;//图片宽度
    private static final int pictureHeight = 268;//图片高度
    private static final int textSize = 40;//字体大小
    private static final int backgroundColor = 0xffffffff;
    private static final int textColor = 0xff010101;

    public static File createSecret(Resources resources, final List<PictureInfo> secretList,
                                    final String savePath, final Typeface typeface) {
        final TextPaint textPaint = createTextPaint(typeface);

        final int totalWidth = padding + pictureWidth + padding;
        int totalHeight = 0;
        for (PictureInfo secret : secretList) {
            final String text = secret.getTitle();
            final StaticLayout currentLayout = new StaticLayout(text, textPaint, pictureWidth,
                    Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

            totalHeight += padding;
            totalHeight += pictureHeight;
            totalHeight += padding;
            totalHeight += currentLayout.getHeight();
            totalHeight += padding;
        }

        final Paint backgroundPatnt = createBackgroundPaint();
        final Bitmap picture = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        final Rect background = new Rect(0, 0, totalWidth, totalHeight);
        final Canvas canvas = new Canvas(picture);
        canvas.drawRect(background, backgroundPatnt);

        for (PictureInfo secret : secretList) {
            final String text = secret.getTitle();
            final StaticLayout currentLayout = new StaticLayout(text, textPaint, pictureWidth,
                    Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

            final int resourceId = secret.getResourceId();

            canvas.translate(0, padding);
            drawBitmap(resources, canvas, resourceId);

            canvas.translate(totalWidth / 2, pictureHeight + padding);
            currentLayout.draw(canvas);

            canvas.translate(-totalWidth / 2, currentLayout.getHeight() + padding);
        }

        final String imageName = System.currentTimeMillis() + ".jpg";
        final File newFile = ImageUtils.saveBitmapToJpg(picture, savePath, imageName);
        picture.recycle();

        return newFile;
    }

    private static Paint createBackgroundPaint() {
        final Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.FILL);
        return backgroundPaint;
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

    private static void drawBitmap(Resources resources, Canvas canvas, int resourceId) {
        final Bitmap bitmap = getBitmapByResourcesId(resources, resourceId);
        final Rect pictureRect = new Rect(0, 0, pictureWidth, pictureHeight);
        final RectF dst = new RectF(padding, 0, pictureWidth + padding, pictureHeight);
        canvas.drawBitmap(bitmap, pictureRect, dst, null);
        bitmap.recycle();
    }

    private static Bitmap getBitmapByResourcesId(Resources resources, int resourceId) {
        final Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();

        if (bitmapWidth != pictureWidth || bitmapHeight != pictureHeight) {
            float scaleWidth = ((float) pictureWidth) / bitmapWidth;
            float scaleHeight = ((float) pictureHeight) / bitmapHeight;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

            bitmap.recycle();

            return resizedBitmap;
        }

        return bitmap;
    }
}
