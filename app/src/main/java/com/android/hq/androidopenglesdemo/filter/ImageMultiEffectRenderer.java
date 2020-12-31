package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.widget.Toast;

import com.android.hq.androidopenglesdemo.utils.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ImageMultiEffectRenderer implements GLSurfaceView.Renderer  {
    private Context mContext;

    private boolean mIsHalf;

    private Filter mFilter;
    private int mWidth;
    private int mHeight;
    private boolean mSavePicture = false;

    public ImageMultiEffectRenderer(Context context) {
        mContext = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mFilter.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mFilter.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mFilter.onDrawFrame();
        if (mSavePicture) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/" +System.currentTimeMillis()+".png";
            TextureHelper.saveBitmap(path, mWidth, mHeight);
            mSavePicture = false;
        }
    }

    public void setHalfMode(boolean isHalf) {
        mIsHalf = isHalf;
        mFilter.setHalfMode(isHalf);
    }

    public boolean isHalfMode() {
        return mFilter.isHalfMode();
    }

    public void setFilter(Filter filter) {
        mFilter = filter;
        mFilter.setHalfMode(mIsHalf);
    }

    public Filter getFilter() {
        return mFilter;
    }

    public void savePicture(int width, int height) {
        if (mSavePicture) {
            Toast.makeText(mContext,"稍后再试",Toast.LENGTH_SHORT);
            return;
        }
        mWidth = width;
        mHeight = height;
        mSavePicture = true;
    }

    public void updateTextureSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        mFilter.updateTextureSize(width,height);
    }

    public int getTextureWidth() {
        return mWidth;
    }

    public int getTextureHeight() {
        return mHeight;
    }

}
