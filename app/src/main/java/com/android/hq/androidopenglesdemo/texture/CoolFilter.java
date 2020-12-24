package com.android.hq.androidopenglesdemo.texture;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

/**
 * 冷色调效果
 */

public class CoolFilter extends Filter {
    public CoolFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_cool_filter.glsl"));
    }
    @Override
    public float[] getData() {
        return new float[]{0.0f,0.0f,0.1f};
    }

    @Override
    public void filter() {

    }
}
