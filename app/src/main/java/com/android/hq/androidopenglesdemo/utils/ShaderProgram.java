package com.android.hq.androidopenglesdemo.utils;

import android.content.Context;
import android.opengl.GLES20;

public class ShaderProgram {
    protected static final String U_MATRIX = "u_Matrix";

    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    protected static final String A_POSITION = "a_Position";

    protected static final String A_COLOR = "a_Color";

    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderReourceId){
        this.program= ShaderHelper.buildProgram(
                Utils.fileLoader(context,vertexShaderResourceId),
                Utils.fileLoader(context,fragmentShaderReourceId));
    }

    public void useProgram(){
        GLES20.glUseProgram(program);
    }
}
