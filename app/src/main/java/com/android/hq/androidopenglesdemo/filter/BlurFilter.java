package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

/**
 * 模糊效果
 */

public class BlurFilter extends Filter {
    public BlurFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_blur_filter.glsl"));
    }
    @Override
    public float[] getData() {
        return new float[]{0.006f,0.004f,0.002f};
    }

    @Override
    public void filter() {

    }
}
