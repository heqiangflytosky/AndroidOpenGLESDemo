package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

/**
 * 底片效果
 */

public class PlateFilter extends Filter {
    public PlateFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_plate_filter.glsl"));
    }
    @Override
    public float[] getData() {
        return new float[]{0f,0f,0f};
    }

    @Override
    public void filter() {

    }
}
