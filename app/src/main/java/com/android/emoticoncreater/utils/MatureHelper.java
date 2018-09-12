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

import com.android.emoticoncreater.R;

import java.io.File;

public class MatureHelper {

    public static final int HEADER_WIDTH = 184;
    public static final int HEADER_HEIGHT = 184;

    private static final int PICTURE_WIDTH = 700;
    private static final int PICTURE_HEIGHT = 500;
    private static final int PADDING = 20;
    private static final int MARGIN_TOP = 270;
    private static final int MARGIN_LEFT = 114;
    private static final int TEXT_SIZE = 44;//字体大小
    private static final int TEXT_COLOR = 0xff010101;
    private static final int BACKGROUND_COLOR = 0xffffffff;

    public static File addPicture(Resources resources, String childFilePath, final String savePath) {
        final Bitmap background = Bitmap.createBitmap(PICTURE_WIDTH, PICTURE_HEIGHT, Bitmap.Config.ARGB_8888);
        final Rect backgroundRect = new Rect(0, 0, PICTURE_WIDTH, PICTURE_HEIGHT);
        final Canvas canvas = new Canvas(background);
        final Paint backgroundPaint = createBackgroundPaint();
        canvas.drawRect(backgroundRect, backgroundPaint);

        final Bitmap childBitmap = getBitmapByFilePath(childFilePath, HEADER_WIDTH);
        final int childWidth = childBitmap.getWidth();
        final int childHeight = childBitmap.getHeight();
        final Rect childRect = new Rect(0, 0, childWidth, childHeight);
        final RectF childRectF = new RectF(MARGIN_LEFT, MARGIN_TOP,
                MARGIN_LEFT + childWidth, MARGIN_TOP + childHeight);
        canvas.drawBitmap(childBitmap, childRect, childRectF, null);
        childBitmap.recycle();

        final Bitmap parentBitmap = getBitmapByResourcesId(resources, R.raw.img_mature);
        final Rect parentRect = new Rect(0, 0, PICTURE_WIDTH, PICTURE_HEIGHT);
        final RectF parentRectF = new RectF(0, 0, PICTURE_WIDTH, PICTURE_HEIGHT);
        canvas.drawBitmap(parentBitmap, parentRect, parentRectF, null);
        parentBitmap.recycle();

        final String imageName = System.currentTimeMillis() + ".jpg";
        final File newFile = ImageUtils.saveBitmapToJpg(background, savePath, imageName, 100);
        background.recycle();

        return newFile;
    }

    public static File create(String pictureFilePath, String text, final String savePath, final Typeface typeface) {
        final Bitmap picture = getBitmapByFilePath(pictureFilePath, PICTURE_WIDTH);
        final int pictureWidth = picture.getWidth();
        final int pictureHeight = picture.getHeight();

        final TextPaint textPaint = createTextPaint(typeface);
        final StaticLayout currentLayout = new StaticLayout(text, textPaint, pictureWidth - PADDING * 2,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        final int totalHeight = pictureHeight + PADDING + currentLayout.getHeight() + PADDING;

        final Bitmap background = Bitmap.createBitmap(pictureWidth, totalHeight, Bitmap.Config.ARGB_8888);
        final Rect backgroundRect = new Rect(0, 0, pictureWidth, totalHeight);
        final Canvas canvas = new Canvas(background);
        final Paint backgroundPaint = createBackgroundPaint();
        canvas.drawRect(backgroundRect, backgroundPaint);

        final Rect pictureRect = new Rect(0, 0, pictureWidth, pictureHeight);
        final RectF dst = new RectF(0, 0, pictureWidth, pictureHeight);
        canvas.drawBitmap(picture, pictureRect, dst, null);
        picture.recycle();

        canvas.translate(pictureWidth / 2, pictureHeight + PADDING);
        currentLayout.draw(canvas);

        final String imageName = System.currentTimeMillis() + ".jpg";
        final File newFile = ImageUtils.saveBitmapToJpg(background, savePath, imageName, 100);
        background.recycle();

        return newFile;
    }

    private static TextPaint createTextPaint(Typeface typeface) {
        final TextPaint textPaint = new TextPaint();
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTypeface(typeface);
        return textPaint;
    }

    private static Paint createBackgroundPaint() {
        final Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(BACKGROUND_COLOR);
        backgroundPaint.setStyle(Paint.Style.FILL);
        return backgroundPaint;
    }

    private static Bitmap getBitmapByFilePath(String filePath, int width) {
        final Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return setScale(bitmap, width);
    }

    private static Bitmap getBitmapByResourcesId(Resources resources, int resourceId) {
        final Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
        return setScale(bitmap, PICTURE_WIDTH);
    }

    private static Bitmap setScale(Bitmap bitmap, int totalWidth) {
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();

        if (bitmapWidth != totalWidth) {
            float scale = ((float) totalWidth) / bitmapWidth;

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

            bitmap.recycle();

            return resizedBitmap;
        }

        return bitmap;
    }
}
