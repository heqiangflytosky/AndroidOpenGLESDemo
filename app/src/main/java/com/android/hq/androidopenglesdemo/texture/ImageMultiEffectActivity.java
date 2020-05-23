package com.android.hq.androidopenglesdemo.texture;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.hq.androidopenglesdemo.R;

public class ImageMultiEffectActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurfaceView;
    private ImageMultiEffectRenderer mRenderer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_multi_effect);
        mGLSurfaceView = findViewById(R.id.gl_view);
        mRenderer = new ImageMultiEffectRenderer(this);

        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(mRenderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mDeal:
                boolean isHalf = mRenderer.isHalfMode();
                isHalf = !isHalf;
                mRenderer.setHalfMode(isHalf);
                if (isHalf) {
                    item.setTitle("处理一半");
                } else {
                    item.setTitle("全部处理");
                }
                break;
            case R.id.mDefault:
                mRenderer.setDefaultFilter();
                break;
            case R.id.mGray:
                mRenderer.setFilter(1, new float[]{0.299f,0.587f,0.114f});
                break;
            case R.id.mCool:
                mRenderer.setFilter(2, new float[]{0.0f,0.0f,0.1f});
                break;
            case R.id.mWarm:
                mRenderer.setFilter(2, new float[]{0.1f,0.1f,0.0f});
                break;
            case R.id.mBlur:
                mRenderer.setFilter(3, new float[]{0.006f,0.004f,0.002f});
                break;
            case R.id.mMagn:
                mRenderer.setFilter(4, new float[]{0.0f,0.0f,0.4f});
                break;
        }
        mGLSurfaceView.requestRender();
        return super.onOptionsItemSelected(item);
    }

}
