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

public class AllWickedHelper {

    private static final int PICTURE_WIDTH = 500;

    private static final int FIRST_CLOTHES_TEXT_CENTER = 258;
    private static final int FIRST_CLOTHES_TEXT_TOP = 99;
    private static final int FIRST_PICUTRE = R.raw.img_all_wicked1;
    private static final int FIRST_PICUTRE_HEIGHT = 318;

    private static final int SECOND_CLOTHES_TEXT_CENTER = 116;
    private static final int SECOND_CLOTHES_TEXT_TOP = 130;
    private static final int SECOND_PICUTRE = R.raw.img_all_wicked2;
    private static final int SECOND_PICUTRE_HEIGHT = 446;

    private static final int THIRD_CLOTHES_TEXT_CENTER = 451;
    private static final int THIRD_CLOTHES_TEXT_TOP = 206;
    private static final int THIRD_PICUTRE = R.raw.img_all_wicked3;
    private static final int THIRD_PICUTRE_HEIGHT = 384;

    private static final int FORTH_CLOTHES_TEXT_CENTER = 260;
    private static final int FORTH_CLOTHES_TEXT_TOP = 100;
    private static final int FORTH_PICUTRE = R.raw.img_all_wicked4;
    private static final int FORTH_PICUTRE_HEIGHT = 285;

    private static final int DESCRIPTION_TEXT_SIZE = 40;
    private static final int CLOTHES_BIG_TEXT_SIZE = 36;
    private static final int CLOTHES_BIG_TEXT_WIDTH = 144;
    private static final int CLOTHES_TEXT_WIDTH = 72;
    private static final int CLOTHES_WORD_WIDTH = 18;
    private static final int CLOTHES_TEXT_SIZE = 18;

    private static final int PADDING = 40;
    private static final int TEXT_PADDING = 20;
    private static final int TEXT_COLOR = 0xff010101;
    private static final int CLOTHES_TEXT_COLOR = 0xffffffff;
    private static final int BACKGROUND_COLOR = 0xffffffff;

    private final Resources mResources;
    private final String mDescription;//描述
    private final String mAClothesText;//A的衣服文字
    private final String mBClothesText;//B的衣服文字
    private final String mBClothesWord;//B的衣服文字的第一个或最后一个字
    private final String mAAskText;//A的提问
    private final String mBReplyText;//B的回答
    private final String mSavePath;
    private final Typeface mTypeFace;

    private AllWickedHelper(Builder builder) {
        mResources = builder.resources;
        mDescription = builder.description;
        mAClothesText = builder.aClothesText;
        mBClothesText = builder.bClothesText;
        mBClothesWord = builder.bClothesWord;
        mAAskText = builder.aAskText;
        mBReplyText = builder.bReplyText;
        mSavePath = builder.savePath;
        mTypeFace = builder.typeFace;
    }

    public File createList() {
        final TextPaint textPaint = createTextPaint(mTypeFace, DESCRIPTION_TEXT_SIZE, TEXT_COLOR);
        final int textWidth = PICTURE_WIDTH - TEXT_PADDING * 2;
        final StaticLayout descriptionLayout = new StaticLayout(mDescription, textPaint, textWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
        final StaticLayout aAskLayout = new StaticLayout(mAAskText, textPaint, textWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
        final StaticLayout bReplyLayout = new StaticLayout(mBReplyText, textPaint, textWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        final int totalHeight = PADDING + descriptionLayout.getHeight() + PADDING
                + FIRST_PICUTRE_HEIGHT + PADDING + SECOND_PICUTRE_HEIGHT
                + PADDING + aAskLayout.getHeight()
                + THIRD_PICUTRE_HEIGHT + PADDING
                + bReplyLayout.getHeight()
                + FORTH_PICUTRE_HEIGHT + PADDING;

        final Bitmap background = Bitmap.createBitmap(PICTURE_WIDTH, totalHeight, Bitmap.Config.ARGB_8888);
        final Rect backgroundRect = new Rect(0, 0, PICTURE_WIDTH, totalHeight);
        final Canvas canvas = new Canvas(background);
        final Paint backgroundPaint = createBackgroundPaint();
        canvas.drawRect(backgroundRect, backgroundPaint);

        final TextPaint clothesTextPaint = createTextPaint(mTypeFace, CLOTHES_TEXT_SIZE, CLOTHES_TEXT_COLOR);

        drawFirstPart(canvas, descriptionLayout, clothesTextPaint);
        drawSecondPart(canvas, clothesTextPaint);
        drawThirdPart(canvas, aAskLayout);
        drawForthPart(canvas, bReplyLayout, clothesTextPaint);

        final String imageName = System.currentTimeMillis() + ".jpg";
        final File newFile = ImageUtils.saveBitmapToJpg(background, mSavePath, imageName, 100);
        background.recycle();

        return newFile;
    }

    private void drawFirstPart(Canvas canvas, StaticLayout descriptionLayout, TextPaint clothesTextPaint) {
        canvas.translate(PICTURE_WIDTH / 2, PADDING);
        descriptionLayout.draw(canvas);

        canvas.translate(-PICTURE_WIDTH / 2, descriptionLayout.getHeight() + PADDING);
        drawBitmap(mResources, canvas, FIRST_PICUTRE);

        final StaticLayout clothesLayout = new StaticLayout(mAClothesText, clothesTextPaint, CLOTHES_TEXT_WIDTH,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        canvas.translate(FIRST_CLOTHES_TEXT_CENTER, FIRST_CLOTHES_TEXT_TOP);
        clothesLayout.draw(canvas);
        canvas.translate(-FIRST_CLOTHES_TEXT_CENTER, -FIRST_CLOTHES_TEXT_TOP);
    }

    private void drawSecondPart(Canvas canvas, TextPaint clothesTextPaint) {
        canvas.translate(0, FIRST_PICUTRE_HEIGHT + PADDING);
        drawBitmap(mResources, canvas, SECOND_PICUTRE);

        final StaticLayout clothesWordLayout = new StaticLayout(mBClothesWord, clothesTextPaint, CLOTHES_WORD_WIDTH,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        canvas.translate(SECOND_CLOTHES_TEXT_CENTER, SECOND_CLOTHES_TEXT_TOP);
        clothesWordLayout.draw(canvas);
        canvas.translate(-SECOND_CLOTHES_TEXT_CENTER, -SECOND_CLOTHES_TEXT_TOP);
    }

    private void drawThirdPart(Canvas canvas, StaticLayout aAskLayout) {
        canvas.translate(PICTURE_WIDTH / 2, SECOND_PICUTRE_HEIGHT + PADDING);
        aAskLayout.draw(canvas);

        canvas.translate(-PICTURE_WIDTH / 2, aAskLayout.getHeight());
        drawBitmap(mResources, canvas, THIRD_PICUTRE);

        final TextPaint clothesBigTextPaint = createTextPaint(mTypeFace, CLOTHES_BIG_TEXT_SIZE, CLOTHES_TEXT_COLOR);
        final StaticLayout clothesLayout = new StaticLayout(mAClothesText, clothesBigTextPaint, CLOTHES_BIG_TEXT_WIDTH,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        canvas.translate(THIRD_CLOTHES_TEXT_CENTER, THIRD_CLOTHES_TEXT_TOP);
        clothesLayout.draw(canvas);
        canvas.translate(-THIRD_CLOTHES_TEXT_CENTER, -THIRD_CLOTHES_TEXT_TOP);
    }

    private void drawForthPart(Canvas canvas, StaticLayout bReplyLayout, TextPaint clothesTextPaint) {
        canvas.translate(PICTURE_WIDTH / 2, THIRD_PICUTRE_HEIGHT + PADDING);
        bReplyLayout.draw(canvas);

        canvas.translate(-PICTURE_WIDTH / 2, bReplyLayout.getHeight());
        drawBitmap(mResources, canvas, FORTH_PICUTRE);

        final StaticLayout bClothesLayout = new StaticLayout(mBClothesText, clothesTextPaint, CLOTHES_TEXT_WIDTH,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
        canvas.translate(FORTH_CLOTHES_TEXT_CENTER, FORTH_CLOTHES_TEXT_TOP);
        bClothesLayout.draw(canvas);
    }

    private TextPaint createTextPaint(Typeface typeface, int textSize, int textColor) {
        final TextPaint textPaint = new TextPaint();
        textPaint.setColor(textColor);
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

    private void drawBitmap(Resources resources, Canvas canvas, int resourceId) {
        final Bitmap bitmap = getBitmapByResourcesId(resources, resourceId);
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        final Rect pictureRect = new Rect(0, 0, width, height);
        final RectF dst = new RectF(0, 0, width, height);
        canvas.drawBitmap(bitmap, pictureRect, dst, null);
        bitmap.recycle();
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
        private String description;
        private String aClothesText;
        private String bClothesText;
        private String bClothesWord;
        private String aAskText;
        private String bReplyText;
        private String savePath;
        private Typeface typeFace;

        public Builder(Resources resources) {
            this.resources = resources;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder aClothesText(String aClothesText) {
            this.aClothesText = aClothesText;
            return this;
        }

        public Builder bClothesText(String bClothesText) {
            this.bClothesText = bClothesText;
            return this;
        }

        public Builder bClothesWord(String bClothesWord) {
            this.bClothesWord = bClothesWord;
            return this;
        }

        public Builder aAskText(String aAskText) {
            this.aAskText = aAskText;
            return this;
        }

        public Builder bReplyText(String bReplyText) {
            this.bReplyText = bReplyText;
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

        public AllWickedHelper bulid() {
            return new AllWickedHelper(this);
        }
    }
}
