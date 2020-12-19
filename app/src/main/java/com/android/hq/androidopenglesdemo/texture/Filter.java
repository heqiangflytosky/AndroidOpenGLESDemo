package com.android.hq.androidopenglesdemo.texture;

import android.content.Context;
import android.opengl.GLES20;

import com.android.hq.androidopenglesdemo.R;
import com.android.hq.androidopenglesdemo.utils.Constants;
import com.android.hq.androidopenglesdemo.utils.ShaderHelper;
import com.android.hq.androidopenglesdemo.utils.TextureHelper;
import com.android.hq.androidopenglesdemo.utils.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public abstract class Filter {
    private static final float[] vertexData = {
            // Order of coordinates: X,Y,S,T
            // 第一个三角形
            -1f, -0.4f,  0f, 1f,
            1f, 0.4f,   1f, 0f,
            -1f, 0.4f,  0f, 0f,

            // 第二个三角形
            -1f, -0.4f, 0f, 1f,
            1f, -0.4f, 1f, 1f,
            1f, 0.4f,1f, 0f
    };
    private static final int POSITION_COMPONENT_COUNT=2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT=2;
    private static final int STRIDE=(POSITION_COMPONENT_COUNT+TEXTURE_COORDINATES_COMPONENT_COUNT)* Constants.BYTES_PER_FLOAT;
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    //protected static final String U_CHANGE_TYPE = "u_ChangeType";
    protected static final String U_CHANGE_COLOR = "u_ChangeColor";
    protected static final String U_XY = "u_XY";
    protected static final String U_IS_HALF = "u_IsHalf";

    private FloatBuffer vertexDataBuffer;
    private int textureId;
    private int programeId;

    private int uTextureUnitLocation;
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;
    //private int uChangeType;
    private int uChangeColor;
    private int uXY;
    private int uIsHalf;

    //protected int mType = 0;
    //private float[] mData = new float[]{0.0f,0.0f,0.0f};
    protected float mXY;
    private boolean mIsHalf;

    private Context mContext;
    private String mVertexShader;
    private String mFragmentShader;

    public Filter(Context context,String vertexShader, String fragmentShader) {
        mContext = context;
        mVertexShader = vertexShader;
        mFragmentShader = fragmentShader;
    }

    public void initFilter() {
        GLES20.glClearColor(0.0f,0,0,0);

        vertexDataBuffer = ByteBuffer
                .allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);

        int vertexShader= ShaderHelper.compileVertexShader(mVertexShader);
        int fragmentShader=ShaderHelper.compileFragmentShader(mFragmentShader);

        programeId = ShaderHelper.linkPrograme(vertexShader,fragmentShader);
        ShaderHelper.validatePrograme(programeId);

        this.uTextureUnitLocation=GLES20.glGetUniformLocation(programeId,U_TEXTURE_UNIT);
        this.aPositionLocation=GLES20.glGetAttribLocation(programeId, A_POSITION);
        this.aTextureCoordinatesLocation= GLES20.glGetAttribLocation(programeId,A_TEXTURE_COORDINATES);
        //this.uChangeType = GLES20.glGetUniformLocation(programeId, U_CHANGE_TYPE);
        this.uChangeColor = GLES20.glGetUniformLocation(programeId, U_CHANGE_COLOR);
        this.uXY = GLES20.glGetUniformLocation(programeId, U_XY);
        this.uIsHalf = GLES20.glGetUniformLocation(programeId, U_IS_HALF);

        GLES20.glUseProgram(programeId);

        // 加载纹理
        textureId = TextureHelper.loadTexture(mContext, R.drawable.timg);

        // 激活纹理单元，GL_TEXTURE0代表纹理单元0，GL_TEXTURE1代表纹理单元1，以此类推。OpenGL使用纹理单元来表示被绘制的纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 绑定纹理到这个纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        // 把选定的纹理单元传给片段着色器中的 u_TextureUnit，对应前面设置的 GL_TEXTUREi，设置为 GL_TEXTURE0，这里就设置0
        GLES20.glUniform1i(uTextureUnitLocation, 0);


        // 传递矩形顶点坐标
        vertexDataBuffer.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 16, vertexDataBuffer);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        // 传递纹理顶点坐标
        vertexDataBuffer.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aTextureCoordinatesLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 16, vertexDataBuffer);
        GLES20.glEnableVertexAttribArray(aTextureCoordinatesLocation);

        //setDefaultFilter();
    }


    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        mXY = width/(float)height;
    }

    public void onDrawFrame() {
        initFilter();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        doFilter();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);
    }

    private void doFilter() {
        //GLES20.glUniform1i(uChangeType, mType);
        GLES20.glUniform3fv(uChangeColor,1,getData(),0);
        GLES20.glUniform1f(uXY,mXY);
        GLES20.glUniform1i(uIsHalf,mIsHalf ? 1 : 0);
    }

//    public void setFilter(int type, float[] data) {
//        mType = type;
//        mData = data;
//    }

//    public void setDefaultFilter() {
//        mType = 0;
//        mData = new float[]{0.0f,0.0f,0.0f};
//    }

    public void setHalfMode(boolean isHalf) {
        mIsHalf = isHalf;
    }

    public boolean isHalfMode() {
        return mIsHalf;
    }

    public abstract float[] getData();
}
