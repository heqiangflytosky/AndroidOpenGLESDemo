package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.android.hq.androidopenglesdemo.R;
import com.android.hq.androidopenglesdemo.utils.Constants;
import com.android.hq.androidopenglesdemo.utils.ShaderHelper;
import com.android.hq.androidopenglesdemo.utils.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class Filter {
//    private static final float[] vertexData = {
//            // Order of coordinates: X,Y,S,T
//            // 第一个三角形
//            -1f, -0.4f,  0f, 1f,
//            1f, 0.4f,   1f, 0f,
//            -1f, 0.4f,  0f, 0f,
//
//            // 第二个三角形
//            -1f, -0.4f, 0f, 1f,
//            1f, -0.4f, 1f, 1f,
//            1f, 0.4f,1f, 0f
//    };

    private static final float[] vertexData = {
            // Order of coordinates: X,Y,S,T
            // 第一个三角形
            -1f, -1f,  0f, 1f,
            1f, 1f,   1f, 0f,
            -1f, 1f,  0f, 0f,

            // 第二个三角形
            -1f, -1f, 0f, 1f,
            1f, -1f, 1f, 1f,
            1f, 1f,1f, 0f
    };
    private static final int POSITION_COMPONENT_COUNT=2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT=2;
    private static final int STRIDE=(POSITION_COMPONENT_COUNT+TEXTURE_COORDINATES_COMPONENT_COUNT)* Constants.BYTES_PER_FLOAT;
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    //protected static final String U_CHANGE_TYPE = "u_ChangeType";
    protected static final String U_CHANGE_COLOR = "u_ChangeColor";
    //protected static final String U_XY = "u_XY";
    protected static final String U_IS_HALF = "u_IsHalf";

    private FloatBuffer vertexDataBuffer;
    private int textureId;
    protected int programeId;

    private int uTextureUnitLocation;
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;
    private int uChangeColor;
    private int uIsHalf;

    protected boolean mIsHalf;

    protected Context mContext;
    private String mVertexShader;
    private String mFragmentShader;

    private boolean mIsInited;

    protected int mTextureWidth;
    protected int mTextureHeight;

    public Filter(Context context,String vertexShader, String fragmentShader) {
        mContext = context;
        mVertexShader = vertexShader;
        mFragmentShader = fragmentShader;
    }

    public void init() {
        GLES20.glClearColor(0.0f,0,0,0);

        // 加载纹理
        textureId = TextureHelper.loadTexture(mContext, R.drawable.timg);

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
        this.uChangeColor = GLES20.glGetUniformLocation(programeId, U_CHANGE_COLOR);
        this.uIsHalf = GLES20.glGetUniformLocation(programeId, U_IS_HALF);

        initFilter();

        GLES20.glUseProgram(programeId);

        // 传递矩形顶点坐标
        vertexDataBuffer.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 16, vertexDataBuffer);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        // 传递纹理顶点坐标
        vertexDataBuffer.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aTextureCoordinatesLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 16, vertexDataBuffer);
        GLES20.glEnableVertexAttribArray(aTextureCoordinatesLocation);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.init();
        mIsInited = true;
    }


    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    public void onDrawFrame() {
        checkInit();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 绘制纹理
        // 激活纹理单元，GL_TEXTURE0代表纹理单元0，GL_TEXTURE1代表纹理单元1，以此类推。OpenGL使用纹理单元来表示被绘制的纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 绑定纹理到这个纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        // 把选定的纹理单元传给片段着色器中的 u_TextureUnit，对应前面设置的 GL_TEXTUREi，设置为 GL_TEXTURE0，这里就设置0
        GLES20.glUniform1i(uTextureUnitLocation, 0);

        doFilter();

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);
    }

    protected void checkInit() {
        if (!mIsInited) {
            this.init();
            mIsInited = true;
        }
    }

    private void doFilter() {
        GLES20.glUniform3fv(uChangeColor,1,getData(),0);
        filter();
        GLES20.glUniform1i(uIsHalf,mIsHalf ? 1 : 0);
    }

    public void setHalfMode(boolean isHalf) {
        mIsHalf = isHalf;
    }

    public boolean isHalfMode() {
        return mIsHalf;
    }

    public void initFilter(){}

    public void updateTextureSize(int width, int height) {
        mTextureWidth = width;
        mTextureHeight = height;
    }

    public boolean canSeek() {
        return false;
    }

    public int getDefaultProgress() {
        return 50;
    }

    public void onProgressChanged(int progress) {
        if (canSeek()) {
            ((ImageMultiEffectActivity)mContext).requestRender();
        }
    }

    public abstract float[] getData();
    public abstract void filter();
}
