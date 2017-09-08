package com.apmato.evolveme;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by hyuntae on 05/09/16.
 */
public class PictureUtils extends Drawable{
    private final Bitmap mBitmap;
    private final Paint mPaint;
    private final RectF mRectF;
    private final int mBitmapWidth;
    private final int mBitmapHeight;

    public PictureUtils(){
        mBitmapHeight = 0;
        mBitmapWidth = 0;
        mBitmap = null;
        mRectF = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    public PictureUtils(Bitmap bitmap){
        mBitmap = bitmap;
        mRectF = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);

        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawOval(mRectF, mPaint);
    }

    @Override
    protected void onBoundsChange(Rect bounds){
        super.onBoundsChange(bounds);
        mRectF.set(bounds);
    }

    @Override
    public void setAlpha(int alpha){
        if(mPaint.getAlpha()!=alpha){
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf){
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity(){
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth(){
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight(){
        return mBitmapHeight;
    }

    public void setAntiAlias(Boolean b){
        mPaint.setAntiAlias(b);
        invalidateSelf();
    }

    @Override
    public void setFilterBitmap(boolean filter){
        mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    public void setDither(Boolean dither){
        mPaint.setDither(dither);
        invalidateSelf();
    }

    public Bitmap getBitmap(){
        return mBitmap;
    }
}

