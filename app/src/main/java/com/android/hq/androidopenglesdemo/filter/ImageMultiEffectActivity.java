package com.android.hq.androidopenglesdemo.filter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.android.hq.androidopenglesdemo.BasicGLSurfaceViewActivity;
import com.android.hq.androidopenglesdemo.R;

public class ImageMultiEffectActivity extends BasicGLSurfaceViewActivity {
    private ImageMultiEffectRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
    }

    @Override
    public GLSurfaceView.Renderer getRenderer() {
        if (mRenderer == null) {
            mRenderer = new ImageMultiEffectRenderer(this);
        }
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
            case R.id.mSave:
                mRenderer.savePicture(mGLSurfaceView.getWidth(),mGLSurfaceView.getHeight());
                break;
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
            case R.id.mNoFilter:
                //mRenderer.setDefaultFilter();
                mRenderer.setFilter(new NoFilter(this));
                break;
            case R.id.mGray:
                //mRenderer.setFilter(1, new float[]{0.299f,0.587f,0.114f});
                mRenderer.setFilter(new GrayFilter(this));
                break;
            case R.id.mCool:
                //mRenderer.setFilter(2, new float[]{0.0f,0.0f,0.1f});
                mRenderer.setFilter(new CoolFilter(this));
                break;
            case R.id.mWarm:
                //mRenderer.setFilter(2, new float[]{0.1f,0.1f,0.0f});
                mRenderer.setFilter(new WarmFilter(this));
                break;
            case R.id.mBlur:
                //mRenderer.setFilter(3, new float[]{0.006f,0.004f,0.002f});
                mRenderer.setFilter(new BlurFilter(this));
                break;
            case R.id.mMagn:
                //mRenderer.setFilter(4, new float[]{0.0f,0.0f,0.4f});
                mRenderer.setFilter(new MagnFilter(this));
                break;
            case R.id.mRelief:
                //mRenderer.setFilter(5, new float[]{0.2125f, 0.7154f, 0.0721f});
                mRenderer.setFilter(new ReliefFilter(this));
                break;
            case R.id.mMosaic:
                //mRenderer.setFilter(6, new float[]{0, 0, 0});
                mRenderer.setFilter(new MosaicFilter(this));
                break;
            case R.id.mTextWater:
                mRenderer.setFilter(new WaterFilter(this));
                break;
            case R.id.mPlate:
                mRenderer.setFilter(new PlateFilter(this));
                break;
        }
        mGLSurfaceView.requestRender();
        return super.onOptionsItemSelected(item);
    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissionString = {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            boolean isAllGranted = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if (isAllGranted) {
                return;
            }
            requestPermissions(
                    permissionString, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void updateViewSize(final int width, final int height) {
        mRenderer.updateTextureSize(width,height);
        mGLSurfaceView.post(new Runnable() {
            @Override
            public void run() {
                int w = mGLSurfaceView.getWidth();
                int h = mGLSurfaceView.getHeight();
                if(width>0 && height>0){
                    float textureAspect = ((float)width)/height;
                    float viewAspect = ((float)w)/h;
                    if(textureAspect > viewAspect){
                        h = (int)(w * (1/textureAspect));
                    }else{
                        w = (int)(h * textureAspect);
                    }
                }
                ViewGroup.LayoutParams layoutParams = mGLSurfaceView.getLayoutParams();
                layoutParams.width = w;
                layoutParams.height = h;
                mGLSurfaceView.setLayoutParams(layoutParams);
            }
        });
    }
}
