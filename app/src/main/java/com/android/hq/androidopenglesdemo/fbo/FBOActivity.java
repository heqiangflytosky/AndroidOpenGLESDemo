package com.android.hq.androidopenglesdemo.fbo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.hq.androidopenglesdemo.R;
import com.android.hq.androidopenglesdemo.utils.TextureHelper;

import java.nio.ByteBuffer;

public class FBOActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;
    private ImageView mImageView;
    private FBORender mRender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbo);
        mGLSurfaceView = findViewById(R.id.gl_view);
        mImageView = findViewById(R.id.image_view);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mRender = new FBORender(this);
        mGLSurfaceView.setRenderer(mRender);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    public void onClick(View view) {
        mRender.setImageListener(mListener);
        mGLSurfaceView.requestRender();
    }

    private FBORender.ImageListener mListener = new FBORender.ImageListener() {
        @Override
        public void onGetImageData(final ByteBuffer buffer, int width, int height) {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);

            bitmap = TextureHelper.rotateBitmap(bitmap, 180, true);
            final Bitmap b = TextureHelper.flipBitmap(bitmap, true);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageBitmap(b);
                    buffer.clear();
                }
            });
        }
    };
}
