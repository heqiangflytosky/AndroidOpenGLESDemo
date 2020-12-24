package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.android.hq.androidopenglesdemo.utils.TextureHelper;
import com.android.hq.androidopenglesdemo.utils.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 水印效果
 */

public class WaterFilter extends Filter {
    private SimpleDateFormat TIME_MARK_FORMATTER = new SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.ENGLISH);
    private static final String U_TEXTURE_WATER = "u_TextureWater";
    private static final String A_TEXTURE_WATER_LOCATION = "a_TextureWaterCoordinates";
    private Context mContext;
    private int waterTextureId;
    private int uTextureWater;
    private int aTextureWaterLocation;
    private FloatBuffer vertexDataBuffer;

    private static final float[] vertexData = {
            // Order of coordinates: S,T
            // 第一个三角形
             -4f, 1f,
            1f, -16f,
            -4f, -16f,

            // 第二个三角形
             -4f, 1f,
            1f, 1f,
            1f, -16f
    };
    public WaterFilter(Context context) {
        super(context,
                Utils.assetsFileLoader(context,"filter/vertex_shader_water_filter.glsl"),
                Utils.assetsFileLoader(context,"filter/fragment_shader_water_filter.glsl"));
        mContext = context;
    }

    @Override
    public float[] getData() {
        return new float[]{0.0f,0.0f,0.0f};
    }

    @Override
    public void initFilter() {
        //GLES20.glEnable(GLES20.GL_BLEND);
        //GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        vertexDataBuffer = ByteBuffer
                .allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);

        String time = TIME_MARK_FORMATTER.format(System.currentTimeMillis());
        Bitmap bitmap = Utils.createTextImage(time, 40, "#ffffffff", "#00000000", 0);
        // 加载水印纹理
        waterTextureId = TextureHelper.loadBitmapTexture(bitmap);
        uTextureWater =GLES20.glGetUniformLocation(programeId,U_TEXTURE_WATER);

        aTextureWaterLocation = GLES20.glGetAttribLocation(programeId, A_TEXTURE_WATER_LOCATION);;
        vertexDataBuffer.position(0);
        GLES20.glVertexAttribPointer(aTextureWaterLocation, 2, GLES20.GL_FLOAT, false, 8, vertexDataBuffer);
        GLES20.glEnableVertexAttribArray(aTextureWaterLocation);
    }

    @Override
    public void filter() {
        // 激活纹理单元，GL_TEXTURE0代表纹理单元1，GL_TEXTURE1代表纹理单元1，以此类推。OpenGL使用纹理单元来表示被绘制的纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        // 绑定纹理到这个纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, waterTextureId);
        // 把选定的纹理单元传给片段着色器中的 u_TextureUnit，对应前面设置的 GL_TEXTUREi，设置为 GL_TEXTURE1，这里就设置1
        GLES20.glUniform1i(uTextureWater, 1);
    }
}
