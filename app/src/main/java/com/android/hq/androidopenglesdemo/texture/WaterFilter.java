package com.android.hq.androidopenglesdemo.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.android.hq.androidopenglesdemo.R;
import com.android.hq.androidopenglesdemo.utils.TextureHelper;
import com.android.hq.androidopenglesdemo.utils.Utils;

public class WaterFilter extends Filter {
    private static final String U_TEXTURE_WATER = "u_TextureWater";
    private Context mContext;
    private int waterTextureId;
    private int uTextureWaterLocation;
    public WaterFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_water_filter.glsl"));
        mContext = context;
    }

    @Override
    public float[] getData() {
        return new float[]{0.0f,0.0f,0.0f};
    }

    @Override
    public void initFilter() {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        Bitmap bitmap = Utils.createTextImage("我是水印", 40, "#fff000", "#00000000", 0);
        // 加载水印纹理
        waterTextureId = TextureHelper.loadBitmapTexture(bitmap);
        this.uTextureWaterLocation=GLES20.glGetUniformLocation(programeId,U_TEXTURE_WATER);
    }

    @Override
    public void filter() {
        // 激活纹理单元，GL_TEXTURE0代表纹理单元1，GL_TEXTURE1代表纹理单元1，以此类推。OpenGL使用纹理单元来表示被绘制的纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        // 绑定纹理到这个纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, waterTextureId);
        // 把选定的纹理单元传给片段着色器中的 u_TextureUnit，对应前面设置的 GL_TEXTUREi，设置为 GL_TEXTURE1，这里就设置1
        GLES20.glUniform1i(uTextureWaterLocation, 1);
    }
}
