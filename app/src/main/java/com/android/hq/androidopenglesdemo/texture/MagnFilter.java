package com.android.hq.androidopenglesdemo.texture;

import android.content.Context;
import android.opengl.GLES20;

import com.android.hq.androidopenglesdemo.utils.Utils;

/***
 * 放大镜效果
 */

public class MagnFilter extends Filter {
    protected static final String U_XY = "u_XY";
    private float mXY;
    private int uXY;
    public MagnFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_magn_filter.glsl"));
        mXY = ((ImageMultiEffectRenderer)((ImageMultiEffectActivity)context).getRenderer()).getTextureWidth()/
                (float)((ImageMultiEffectRenderer)((ImageMultiEffectActivity)context).getRenderer()).getTextureHeight();
    }
    @Override
    public float[] getData() {
        return new float[]{0.0f,0.0f,0.4f};
    }

    @Override
    public void filter() {
        GLES20.glUniform1f(uXY,mXY);
    }

    @Override
    public void initFilter() {
        this.uXY = GLES20.glGetUniformLocation(programeId, U_XY);
    }
}
