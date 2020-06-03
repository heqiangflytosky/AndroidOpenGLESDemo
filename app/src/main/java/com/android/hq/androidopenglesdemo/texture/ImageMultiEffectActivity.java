package com.android.hq.androidopenglesdemo.texture;

import android.opengl.GLSurfaceView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.hq.androidopenglesdemo.BasicGLSurfaceViewActivity;
import com.android.hq.androidopenglesdemo.R;

public class ImageMultiEffectActivity extends BasicGLSurfaceViewActivity {
    private ImageMultiEffectRenderer mRenderer;

    @Override
    public GLSurfaceView.Renderer getRenderer() {
        mRenderer = new ImageMultiEffectRenderer(this);
        return mRenderer;
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
            case R.id.mRelief:
                mRenderer.setFilter(5, new float[]{0.2125f, 0.7154f, 0.0721f});
                break;
            case R.id.mMosaic:
                mRenderer.setFilter(6, new float[]{0, 0, 0});
                break;
        }
        mGLSurfaceView.requestRender();
        return super.onOptionsItemSelected(item);
    }

}
