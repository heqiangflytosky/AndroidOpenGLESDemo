package com.android.hq.androidopenglesdemo.texture;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

public class CoolFilter extends Filter {
    public CoolFilter(Context context) {
//        super(context,
//                Utils.assetsFileLoader(context,"filter/texture_multi_effect_vertex_shader.glsl"),
//                Utils.assetsFileLoader(context,"filter/texture_multi_effect_fragment_shader.glsl"));
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_cool_filter.glsl"));
//        mType = 1;
    }
    @Override
    public float[] getData() {
        return new float[]{0.0f,0.0f,0.1f};
    }
}
