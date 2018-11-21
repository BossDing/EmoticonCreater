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

import com.android.emoticoncreater.model.PictureInfo;

import java.io.File;

/**
 * 一个表情图标编辑帮助类
 */
public class OneEmoticonHelper {

    private static final int TEXT_PADDING = 20;//文字内边距
    private static final int PICTURE_PADDING = 20;//图片内边距

    private static final int PADDING = 20;//内边距
    private static final int PICTURE_WIDTH = 300;//图片宽度
    private static final int BACKGROUND_COLOR = 0xffffffff;
    private static final int TEXT_COLOR = 0xff010101;

    private final Resources mResources;
    private final PictureInfo mPicture;
    private final String mSavePath;
    private final Typeface mTypeFace;
    private final boolean mIsOriginal;
    private final int mTextSize;

    private OneEmoticonHelper(Builder builder) {
        mResources = builder.resources;
        mPicture = builder.picture;
        mSavePath = builder.savePath;
        mTypeFace = builder.typeFace;
        mIsOriginal = builder.isOriginal;
        mTextSize = builder.textSize;
    }

    public File create() {
        final int quality = mIsOriginal ? 100 : 5;
        final Bitmap.Config config = mIsOriginal ? Bitmap.Config.ARGB_8888 : Bitmap.Config.ARGB_4444;

        final String text = mPicture.getTitle();

        final int resourceId = mPicture.getResourceId();
        final String filePath = mPicture.getFilePath();
        final Bitmap bitmap;
        if (!TextUtils.isEmpty(filePath)) {
            bitmap = getBitmapByFilePath(filePath);
        } else {
            bitmap = getBitmapByResourcesId(mResources, resourceId);
        }

        final int pictureWidth = bitmap.getWidth();
        final int pictureHeight = bitmap.getHeight();

        final TextPaint textPaint = createTextPaint(mTypeFace, mTextSize);
        final StaticLayout currentLayout = new StaticLayout(text, textPaint, pictureWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        final int textHeight = TextUtils.isEmpty(text) ? 0 : currentLayout.getHeight() + PADDING;
        final int totalWidth = PADDING + pictureWidth + PADDING;
        final int totalHeight = PADDING + pictureHeight + PADDING + textHeight;

        final Bitmap background = Bitmap.createBitmap(totalWidth, totalHeight, config);
        final Rect backgroundRect = new Rect(0, 0, totalWidth, totalHeight);
        final Canvas canvas = new Canvas(background);
        final Paint backgroundPaint = createBackgroundPaint();
        canvas.drawRect(backgroundRect, backgroundPaint);

        final Rect pictureRect = new Rect(0, 0, pictureWidth, pictureHeight);
        final RectF dst = new RectF(PADDING, PADDING, pictureWidth + PADDING, PADDING + pictureHeight);
        canvas.drawBitmap(bitmap, pictureRect, dst, null);
        bitmap.recycle();

        canvas.translate(totalWidth / 2, PADDING + pictureHeight + PADDING);
        currentLayout.draw(canvas);

        final String imageName = System.currentTimeMillis() + ".jpg";
        final File newFile = ImageUtils.saveBitmapToJpg(background, mSavePath, imageName, quality);
        background.recycle();

        return newFile;
    }

    private TextPaint createTextPaint(Typeface typeface, int textSize) {
        final TextPaint textPaint = new TextPaint();
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTypeface(typeface);
        return textPaint;
    }

    private Paint createBackgroundPaint() {
        final Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(BACKGROUND_COLOR);
        backgroundPaint.setStyle(Paint.Style.FILL);
        return backgroundPaint;
    }

    private Bitmap getBitmapByFilePath(String filePath) {
        final Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return setScale(bitmap);
    }

    private Bitmap getBitmapByResourcesId(Resources resources, int resourceId) {
        final Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
        return setScale(bitmap);
    }

    private Bitmap setScale(Bitmap bitmap) {
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();

        if (bitmapWidth != PICTURE_WIDTH) {
            float scale = ((float) PICTURE_WIDTH) / bitmapWidth;

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

            bitmap.recycle();

            return resizedBitmap;
        }

        return bitmap;
    }

    public static class Builder {
        private final Resources resources;
        private PictureInfo picture;
        private String savePath;
        private Typeface typeFace;
        private boolean isOriginal = true;
        private int textSize = 30;

        public Builder(Resources resources) {
            this.resources = resources;
        }

        public Builder pictureInfo(PictureInfo picture) {
            this.picture = picture;
            return this;
        }

        public Builder savePath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        public Builder typeFace(Typeface typeFace) {
            this.typeFace = typeFace;
            return this;
        }

        public Builder isOriginal(boolean isOriginal) {
            this.isOriginal = isOriginal;
            return this;
        }

        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public OneEmoticonHelper bulid() {
            if (resources == null) {
                throw (new NullPointerException("Resources is null"));
            }
            if (picture == null) {
                throw (new NullPointerException("PictureInfo is null"));
            }
            if (TextUtils.isEmpty(savePath)) {
                throw (new NullPointerException("SavePath is empty"));
            }
            if (typeFace == null) {
                throw (new NullPointerException("Typeface is null"));
            }
            return new OneEmoticonHelper(this);
        }
    }
}
