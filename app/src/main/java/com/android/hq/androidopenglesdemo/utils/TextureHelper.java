package com.android.hq.androidopenglesdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;

public class TextureHelper {
    public static int loadTexture(Context context, int resourceId){
        final int[] textureObjectIds = new int[2];
        GLES20.glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0){
            Log.e("Test","gen texture fail");
            return 0;
        }

        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
        final Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),resourceId,options);
        if(bitmap==null){
            Log.e("Test","load bitmap fial");
            GLES20.glDeleteTextures(1,textureObjectIds,0);
            return 0;
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureObjectIds[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);

        bitmap.recycle();

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        // 解绑纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);

        return textureObjectIds[0];
    }

    public static int loadBitmapTexture(Bitmap bitmap) {
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        //ARGB
        ByteBuffer bitmapBuffer = ByteBuffer.allocate(bitmap.getHeight() * bitmap.getWidth() * 4);
        bitmap.copyPixelsToBuffer(bitmapBuffer);
        bitmapBuffer.flip();

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap.getWidth(),
                bitmap.getHeight(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bitmapBuffer);
        //解绑纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureIds[0];
    }
}
