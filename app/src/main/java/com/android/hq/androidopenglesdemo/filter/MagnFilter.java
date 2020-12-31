package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.android.hq.androidopenglesdemo.utils.Utils;

/***
 * 放大镜效果
 */

public class MagnFilter extends Filter {
    protected static final String U_XY = "u_XY";
    protected static final String U_RADIUS = "u_Radius";
    private float mXY;
    private int uXY;
    private float mRadius = 0.4f;
    private int uRadius;
    public MagnFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_magn_filter.glsl"));
        mXY = ((ImageMultiEffectRenderer)((ImageMultiEffectActivity)context).getRenderer()).getTextureWidth()/
                (float)((ImageMultiEffectRenderer)((ImageMultiEffectActivity)context).getRenderer()).getTextureHeight();
    }
    @Override
    public float[] getData() {
        return new float[]{0.0f,0.0f,0.0f};
    }

    @Override
    public void filter() {
        GLES20.glUniform1f(uXY,mXY);
        GLES20.glUniform1f(uRadius,mRadius);
    }

    @Override
    public void initFilter() {
        uXY = GLES20.glGetUniformLocation(programeId, U_XY);
        uRadius = GLES20.glGetUniformLocation(programeId, U_RADIUS);
    }

    @Override
    public boolean canSeek() {
        return true;
    }

    @Override
    public int getDefaultProgress() {
        return (int)(mRadius * 100);
    }

    @Override
    public void onProgressChanged(int progress) {
        mRadius = progress/100f;
        super.onProgressChanged(progress);
    }
}
