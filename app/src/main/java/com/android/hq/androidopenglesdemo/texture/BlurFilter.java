package com.android.hq.androidopenglesdemo.texture;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

public class BlurFilter extends Filter {
    public BlurFilter(Context context) {
//        super(context,
//                Utils.assetsFileLoader(context,"filter/texture_multi_effect_vertex_shader.glsl"),
//                Utils.assetsFileLoader(context,"filter/texture_multi_effect_fragment_shader.glsl"));
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_blur_filter.glsl"));
//        mType = 1;
    }
    @Override
    public float[] getData() {
        return new float[]{0.006f,0.004f,0.002f};
    }

    @Override
    public void filter() {

    }
}
