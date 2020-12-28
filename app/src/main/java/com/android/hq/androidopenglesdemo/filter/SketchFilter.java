package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.android.hq.androidopenglesdemo.utils.Utils;

/**
 * 黑白色调效果
 * 滤镜叠加：黑白滤镜+纹理采样
 * 使用FBO
 */

public class SketchFilter extends Filter {
    private final static String TEXTURE_WIDTH = "texelWidth";
    private final static String TEXTURE_HEIGHT = "texelHeight";
    private int uniformTexelWidthLocation;
    private int uniformTexelHeightLocation;

    private float lineSize = 1.0f;

    public SketchFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context, "filter/fragment_shader_sketch_filter_1.glsl"));
    }
    @Override
    public float[] getData() {
        return new float[]{0.299f,0.587f,0.114f};
    }

    @Override
    public void initFilter() {
        super.initFilter();
        uniformTexelWidthLocation = GLES20.glGetUniformLocation(programeId, TEXTURE_WIDTH);
        uniformTexelHeightLocation = GLES20.glGetUniformLocation(programeId, TEXTURE_HEIGHT);
    }

    @Override
    public void filter() {
        float texelWidth;
        float texelHeight;
        if(mTextureHeight == 0 || mTextureWidth == 0) {
            texelWidth = lineSize/((ImageMultiEffectRenderer)((ImageMultiEffectActivity)mContext).getRenderer()).getTextureWidth();
            texelHeight = lineSize/((ImageMultiEffectRenderer)((ImageMultiEffectActivity)mContext).getRenderer()).getTextureHeight();
        } else {
            texelWidth = lineSize/mTextureWidth;
            texelHeight = lineSize/mTextureHeight;
        }
        GLES20.glUniform1f(uniformTexelWidthLocation, texelWidth);
        GLES20.glUniform1f(uniformTexelHeightLocation, texelHeight);
    }
}
