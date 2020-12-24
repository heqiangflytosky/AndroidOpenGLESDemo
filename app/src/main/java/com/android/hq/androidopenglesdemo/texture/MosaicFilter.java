package com.android.hq.androidopenglesdemo.texture;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

/***
 * 马赛克
 */

public class MosaicFilter extends Filter {
    public MosaicFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_mosaic_filter.glsl"));
    }
    @Override
    public float[] getData() {
        return new float[]{0, 0, 0};
    }

    @Override
    public void filter() {

    }
}
