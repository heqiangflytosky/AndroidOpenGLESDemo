package com.android.hq.androidopenglesdemo.filter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.android.hq.androidopenglesdemo.BasicGLSurfaceViewActivity;
import com.android.hq.androidopenglesdemo.R;

public class ImageMultiEffectActivity extends BasicGLSurfaceViewActivity {
    private ImageMultiEffectRenderer mRenderer;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        mSeekBar = findViewById(R.id.seekBar);
        setSeekBar(mRenderer.getFilter());
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((ImageMultiEffectRenderer)getRenderer()).getFilter().onProgressChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public GLSurfaceView.Renderer getRenderer() {
        if (mRenderer == null) {
            mRenderer = new ImageMultiEffectRenderer(this);
            mRenderer.setFilter(new NoFilter(this));
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
                mRenderer.setFilter(new NoFilter(this));
                break;
            case R.id.mGray:
                mRenderer.setFilter(new GrayFilter(this));
                break;
            case R.id.mCool:
                mRenderer.setFilter(new CoolFilter(this));
                break;
            case R.id.mWarm:
                mRenderer.setFilter(new WarmFilter(this));
                break;
            case R.id.mBlur:
                mRenderer.setFilter(new BlurFilter(this));
                break;
            case R.id.mMagn:
                mRenderer.setFilter(new MagnFilter(this));
                break;
            case R.id.mRelief:
                mRenderer.setFilter(new ReliefFilter(this));
                break;
            case R.id.mMosaic:
                mRenderer.setFilter(new MosaicFilter(this));
                break;
            case R.id.mTextWater:
                mRenderer.setFilter(new WaterFilter(this));
                break;
            case R.id.mPlate:
                mRenderer.setFilter(new PlateFilter(this));
                break;
            case R.id.mHalfTone:
                mRenderer.setFilter(new HalfToneFilter(this));
                break;
            case R.id.mSketch:
                mRenderer.setFilter(new SketchFilter(this));
                break;
        }
        setSeekBar(mRenderer.getFilter());
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
                // 取消缩放，这样保存图片时可以按原来分辨率保存
//                int w = mGLSurfaceView.getWidth();
//                int h = mGLSurfaceView.getHeight();
//                if(width>0 && height>0){
//                    float textureAspect = ((float)width)/height;
//                    float viewAspect = ((float)w)/h;
//                    if(textureAspect > viewAspect){
//                        h = (int)(w * (1/textureAspect));
//                    }else{
//                        w = (int)(h * textureAspect);
//                    }
//                }
                ViewGroup.LayoutParams layoutParams = mGLSurfaceView.getLayoutParams();
                layoutParams.width = width;
                layoutParams.height = height;
                mGLSurfaceView.setLayoutParams(layoutParams);
            }
        });
    }

    public void requestRender() {
        mGLSurfaceView.requestRender();
    }

    private void setSeekBar(Filter filter) {
        if (filter.canSeek()) {
            mSeekBar.setVisibility(View.VISIBLE);
            mSeekBar.setProgress(mRenderer.getFilter().getDefaultProgress());
        } else {
            mSeekBar.setVisibility(View.GONE);
        }
    }
}
