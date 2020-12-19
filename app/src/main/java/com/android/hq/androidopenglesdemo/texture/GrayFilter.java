package com.android.hq.androidopenglesdemo.texture;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

public class GrayFilter extends Filter {
    public GrayFilter(Context context) {
//        super(context,
//                Utils.assetsFileLoader(context,"filter/texture_multi_effect_vertex_shader.glsl"),
//                Utils.assetsFileLoader(context,"filter/texture_multi_effect_fragment_shader.glsl"));
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_gray_filter.glsl"));
//        mType = 1;
    }
    @Override
    public float[] getData() {
        return new float[]{0.299f,0.587f,0.114f};
    }
}
