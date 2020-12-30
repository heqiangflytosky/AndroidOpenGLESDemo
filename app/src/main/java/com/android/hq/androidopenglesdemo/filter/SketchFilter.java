package com.android.hq.androidopenglesdemo.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.android.hq.androidopenglesdemo.R;
import com.android.hq.androidopenglesdemo.utils.ShaderHelper;
import com.android.hq.androidopenglesdemo.utils.TextureHelper;
import com.android.hq.androidopenglesdemo.utils.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 黑白色调效果
 * 滤镜叠加：黑白滤镜+纹理采样
 * 使用FBO
 */

public class SketchFilter extends Filter {
    private static final int POSITION_COMPONENT_COUNT=2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT=2;

    // 第二个着色器
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
    private FloatBuffer vertexDataBuffer;

    private int uTextureUnitLocation;
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;
    private int uChangeColor;
    private int uIsHalf;


    // 第二个着色器
    private final static String TEXTURE_WIDTH = "texelWidth";
    private final static String TEXTURE_HEIGHT = "texelHeight";
    private int uniformTexelWidthLocation;
    private int uniformTexelHeightLocation;


    protected int uTextureUnitLocation2;
    protected int aPositionLocation2;
    protected int aTextureCoordinatesLocation2;
    protected int uChangeColor2;
    protected int uIsHalf2;

    private float lineSize = 1.0f;

    // 与 vertexData 相比，上下翻转图像
    protected static final float[] vertexData2 = {
            // Order of coordinates: X,Y,S,T
            // 第一个三角形
            -1f, -1f,  0f, 0f,
            1f, 1f,   1f, 1f,
            -1f, 1f,  0f, 1f,

            // 第二个三角形
            -1f, -1f, 0f, 0f,
            1f, -1f, 1f, 0f,
            1f, 1f,1f, 1f
    };
    protected FloatBuffer vertexDataBuffer2;

    private int[] textureIds;
    private int programeId2;
    private int frameBuffer[];

    public SketchFilter(Context context) {
        super(context, "", "");
    }
    @Override
    public float[] getData() {
        return new float[]{0.299f,0.587f,0.114f};
    }

    @Override
    public void init() {
        textureIds = new int[2];

        GLES20.glClearColor(0.0f,0,0,0);

        // 加载纹理
        textureIds[0] = TextureHelper.loadTexture(mContext, R.drawable.timg);

        // 初始化第一个着色器
        int vertexShader= ShaderHelper.compileVertexShader(Utils.assetsFileLoader(mContext,"filter/vertex_shader.glsl"));
        int fragmentShader=ShaderHelper.compileFragmentShader(Utils.assetsFileLoader(mContext, "filter/fragment_shader_sketch_filter_1.glsl"));

        programeId = ShaderHelper.linkPrograme(vertexShader,fragmentShader);
        ShaderHelper.validatePrograme(programeId);

        this.uTextureUnitLocation=GLES20.glGetUniformLocation(programeId,U_TEXTURE_UNIT);
        this.aPositionLocation=GLES20.glGetAttribLocation(programeId, A_POSITION);
        this.aTextureCoordinatesLocation= GLES20.glGetAttribLocation(programeId,A_TEXTURE_COORDINATES);
        this.uChangeColor = GLES20.glGetUniformLocation(programeId, U_CHANGE_COLOR);
        this.uIsHalf = GLES20.glGetUniformLocation(programeId, U_IS_HALF);

        vertexDataBuffer = ByteBuffer
                .allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);


        // 初始化第二个着色器
        vertexShader = ShaderHelper.compileVertexShader(Utils.assetsFileLoader(mContext,"filter/vertex_shader.glsl"));
        fragmentShader =ShaderHelper.compileFragmentShader(Utils.assetsFileLoader(mContext, "filter/fragment_shader_sketch_filter_2.glsl"));

        programeId2 = ShaderHelper.linkPrograme(vertexShader,fragmentShader);
        ShaderHelper.validatePrograme(programeId2);

        this.uTextureUnitLocation2=GLES20.glGetUniformLocation(programeId2,U_TEXTURE_UNIT);
        this.aPositionLocation2=GLES20.glGetAttribLocation(programeId2, A_POSITION);
        this.aTextureCoordinatesLocation2= GLES20.glGetAttribLocation(programeId2,A_TEXTURE_COORDINATES);
        this.uChangeColor2 = GLES20.glGetUniformLocation(programeId2, U_CHANGE_COLOR);
        this.uIsHalf2 = GLES20.glGetUniformLocation(programeId2, U_IS_HALF);
        this.uniformTexelWidthLocation = GLES20.glGetUniformLocation(programeId2, TEXTURE_WIDTH);
        this.uniformTexelHeightLocation = GLES20.glGetUniformLocation(programeId2, TEXTURE_HEIGHT);

        vertexDataBuffer2 = ByteBuffer
                .allocateDirect(vertexData2.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData2);

        initFrameBuffer();
    }

    @Override
    public void filter() {

    }

    @Override
    public void onDrawFrame() {
        checkInit();

        // 第一个着色器
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glUseProgram(programeId);

        // 传递矩形顶点坐标
        vertexDataBuffer.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 16, vertexDataBuffer);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        // 传递纹理顶点坐标
        vertexDataBuffer.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aTextureCoordinatesLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 16, vertexDataBuffer);
        GLES20.glEnableVertexAttribArray(aTextureCoordinatesLocation);

        // 绘制纹理
        // 激活纹理单元，GL_TEXTURE0代表纹理单元0，GL_TEXTURE1代表纹理单元1，以此类推。OpenGL使用纹理单元来表示被绘制的纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 绑定纹理到这个纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
        // 把选定的纹理单元传给片段着色器中的 u_TextureUnit，对应前面设置的 GL_TEXTUREi，设置为 GL_TEXTURE0，这里就设置0
        GLES20.glUniform1i(uTextureUnitLocation, 0);

        GLES20.glUniform3fv(uChangeColor,1,getData(),0);
        GLES20.glUniform1i(uIsHalf,mIsHalf ? 1 : 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);
        GLES20.glDisableVertexAttribArray(aPositionLocation);
        GLES20.glDisableVertexAttribArray(aTextureCoordinatesLocation);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);


        // 第二个着色器
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //GLES20.glClearColor(0, 0, 0, 0);

        GLES20.glUseProgram(programeId2);
        // 传递矩形顶点坐标
        vertexDataBuffer2.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation2, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 16, vertexDataBuffer2);
        GLES20.glEnableVertexAttribArray(aPositionLocation2);
        // 传递纹理顶点坐标
        vertexDataBuffer2.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aTextureCoordinatesLocation2, TEXTURE_COORDINATES_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 16, vertexDataBuffer2);
        GLES20.glEnableVertexAttribArray(aTextureCoordinatesLocation2);

        // 绘制纹理
        // 激活纹理单元，GL_TEXTURE0代表纹理单元0，GL_TEXTURE1代表纹理单元1，以此类推。OpenGL使用纹理单元来表示被绘制的纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 绑定上一个纹理到这个纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[1]);
        // 把选定的纹理单元传给片段着色器中的 u_TextureUnit，对应前面设置的 GL_TEXTUREi，设置为 GL_TEXTURE0，这里就设置0
        GLES20.glUniform1i(uTextureUnitLocation2, 0);

        GLES20.glUniform1f(uniformTexelWidthLocation, lineSize/mTextureWidth);
        GLES20.glUniform1f(uniformTexelHeightLocation, lineSize/mTextureHeight);
        GLES20.glUniform1i(uIsHalf2,mIsHalf ? 1 : 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);
        GLES20.glDisableVertexAttribArray(aPositionLocation2);
        GLES20.glDisableVertexAttribArray(aTextureCoordinatesLocation2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void initFrameBuffer(){
        frameBuffer = new int[1];
        GLES20.glGenFramebuffers(1, frameBuffer, 0);

        GLES20.glGenTextures(1, textureIds, 1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureIds[1]);
        //为FBO的纹理附着分配内存
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,mTextureWidth,
                mTextureHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        // 这个很重要，设置不对导致黑屏
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[1]);
        //纹理作为颜色附着在FBO上
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, textureIds[1], 0);

        // 解绑纹理和fbo
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

}
