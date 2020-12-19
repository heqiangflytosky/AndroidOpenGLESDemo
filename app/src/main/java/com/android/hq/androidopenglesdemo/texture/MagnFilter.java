package com.android.hq.androidopenglesdemo.texture;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

public class MagnFilter extends Filter {
    public MagnFilter(Context context) {
//        super(context,
//                Utils.assetsFileLoader(context,"filter/texture_multi_effect_vertex_shader.glsl"),
//                Utils.assetsFileLoader(context,"filter/texture_multi_effect_fragment_shader.glsl"));
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_magn_filter.glsl"));
        //mType = 4;
        mXY = 0.5f;
    }
    @Override
    public float[] getData() {
        return new float[]{0.0f,0.0f,0.4f};
    }
}
