package com.android.hq.androidopenglesdemo.utils;

import android.opengl.GLES20;
import android.util.Log;

public class OpenGLUtils {
    private final static String TAG = "OpenGLUtils";
    /**
     * 检查是否出错
     * @param op
     */
    public static void checkGlError(String op) {
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR) {
            String msg = op + ": glError 0x" + Integer.toHexString(error);
            Log.e(TAG, msg);
        }
    }
}
