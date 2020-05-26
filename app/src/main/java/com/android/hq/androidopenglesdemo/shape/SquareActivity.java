package com.android.hq.androidopenglesdemo.shape;

import android.opengl.GLSurfaceView;

import com.android.hq.androidopenglesdemo.BasicGLSurfaceViewActivity;

public class SquareActivity extends BasicGLSurfaceViewActivity {
    private SquareRenderer mSquareRenderer;

    @Override
    public GLSurfaceView.Renderer getRenderer() {
        mSquareRenderer = new SquareRenderer();
        return mSquareRenderer;
    }
}
