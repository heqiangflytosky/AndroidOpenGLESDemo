package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

public class NoFilter extends Filter {
    public NoFilter(Context context) {
//        super(context,
//                Utils.assetsFileLoader(context,"filter/texture_multi_effect_vertex_shader.glsl"),
//                Utils.assetsFileLoader(context,"filter/texture_multi_effect_fragment_shader.glsl"));
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_no_filter.glsl"));
//        mType = 0;
    }

    @Override
    public float[] getData() {
        return new float[]{0.0f,0.0f,0.0f};
    }

    @Override
    public void filter() {

    }
}
