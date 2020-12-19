package com.android.hq.androidopenglesdemo.texture;

import android.content.Context;

import com.android.hq.androidopenglesdemo.utils.Utils;

public class WarmFilter extends CoolFilter {
    public WarmFilter(Context context) {
        super(context);
    }

    @Override
    public float[] getData() {
        return new float[]{0.1f,0.1f,0.0f};
    }
}
