package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.android.hq.androidopenglesdemo.utils.Utils;

/**
 * 半色调效果
 */

public class HalfToneFilter extends Filter {
    private final static String FRACTIONALWIDTHOFPIXEL = "fractionalWidthOfPixel";
    private final static String ASPECTRATIO = "aspectRatio";

    private int fractionalWidthOfPixelLocation;
    private int aspectRatioLocation;

    private float fractionalWidthOfAPixel;
    private float aspectRatio;
    public HalfToneFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_half_tone_filter.glsl"));
        fractionalWidthOfAPixel = 0.01f;
        aspectRatio = (float)((ImageMultiEffectRenderer)((ImageMultiEffectActivity)context).getRenderer()).getTextureHeight()/
                        ((ImageMultiEffectRenderer)((ImageMultiEffectActivity)context).getRenderer()).getTextureWidth();
    }
    @Override
    public float[] getData() {
        return new float[]{0.0f,0.0f,0.1f};
    }

    @Override
    public void initFilter() {
        super.initFilter();
        fractionalWidthOfPixelLocation = GLES20.glGetUniformLocation(programeId, "fractionalWidthOfPixel");
        aspectRatioLocation = GLES20.glGetUniformLocation(programeId, "aspectRatio");
    }

    @Override
    public void filter() {
        GLES20.glUniform1f(fractionalWidthOfPixelLocation, fractionalWidthOfAPixel);
        GLES20.glUniform1f(aspectRatioLocation, aspectRatio);
    }
}
