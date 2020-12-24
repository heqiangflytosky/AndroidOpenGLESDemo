package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

/**
 * 浮雕效果
 */

public class ReliefFilter extends Filter {
    public ReliefFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_relief_filter.glsl"));
    }
    @Override
    public float[] getData() {
        return new float[]{0.2125f, 0.7154f, 0.0721f};
    }

    @Override
    public void filter() {

    }
}
