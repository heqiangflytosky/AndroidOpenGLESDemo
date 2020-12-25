package com.android.hq.androidopenglesdemo.fbo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import com.android.hq.androidopenglesdemo.R;
import com.android.hq.androidopenglesdemo.utils.Constants;
import com.android.hq.androidopenglesdemo.utils.ShaderHelper;
import com.android.hq.androidopenglesdemo.utils.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FBORender implements GLSurfaceView.Renderer {
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

    private FloatBuffer vertexDataBuffer;
    private int[] textureId;
    protected int programeId;

    private int uTextureUnitLocation;
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;

    private Context mContext;

    private int mWidth;
    private int mHeight;

    private ImageListener mImageListener;

    public interface ImageListener {
        void onGetImageData(ByteBuffer buffer, int width, int height);
    }

    public FBORender(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 绘制纹理
        // 激活纹理单元，GL_TEXTURE0代表纹理单元0，GL_TEXTURE1代表纹理单元1，以此类推。OpenGL使用纹理单元来表示被绘制的纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 绑定纹理到这个纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
        // 把选定的纹理单元传给片段着色器中的 u_TextureUnit，对应前面设置的 GL_TEXTUREi，设置为 GL_TEXTURE0，这里就设置0
        GLES20.glUniform1i(uTextureUnitLocation, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);

        if (mImageListener != null) {
            ByteBuffer grayImg = ByteBuffer.allocate(mWidth * mHeight * 4);
            GLES20.glReadPixels(0, 0, mWidth, mHeight, GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE, grayImg);
            grayImg.position(0);
            mImageListener.onGetImageData(grayImg, mWidth, mHeight);
            mImageListener = null;
        }
    }

    public void setImageListener(ImageListener listener) {
        mImageListener = listener;
    }

    private void init() {
        GLES20.glClearColor(0.0f,0,0,0);

        vertexDataBuffer = ByteBuffer
                .allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);

        int vertexShader= ShaderHelper.compileVertexShader(Utils.assetsFileLoader(mContext,"filter/vertex_shader.glsl"));
        int fragmentShader=ShaderHelper.compileFragmentShader(Utils.assetsFileLoader(mContext,"filter/fragment_shader_plate_filter.glsl"));

        programeId = ShaderHelper.linkPrograme(vertexShader,fragmentShader);
        ShaderHelper.validatePrograme(programeId);

        this.uTextureUnitLocation=GLES20.glGetUniformLocation(programeId,U_TEXTURE_UNIT);
        this.aPositionLocation=GLES20.glGetAttribLocation(programeId, A_POSITION);
        this.aTextureCoordinatesLocation= GLES20.glGetAttribLocation(programeId,A_TEXTURE_COORDINATES);

        GLES20.glUseProgram(programeId);

        // 加载纹理
        textureId = loadTextures(mContext, R.drawable.timg);

        // initFrameBuffer 之后，渲染会在 fbo1 上进行。渲染结果不会在 glSurfaceView 上展示
        // 如果去掉 initFrameBuffer，渲染结果在 glSurfaceView 展示
        initFrameBuffer();

        // 传递矩形顶点坐标
        vertexDataBuffer.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 16, vertexDataBuffer);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        // 传递纹理顶点坐标
        vertexDataBuffer.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aTextureCoordinatesLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 16, vertexDataBuffer);
        GLES20.glEnableVertexAttribArray(aTextureCoordinatesLocation);
    }

    private void initFrameBuffer(){
        int fboId[] = new int[1];
        GLES20.glGenFramebuffers(1, fboId, 0);

        int[] fRender = new int[1];

        /**
         * GL_FRAMEBUFFER相当于一个激活的变量，应该只有一个才能激活，当前fbo被激活了，
         * 后续对Render的渲染都会把数据放到这个fbo，所有对像素数据的读写操作都在这个fbo，
         * 如果不需要在此fbo操作，要解除绑定，并切换使用以下同一个方法切换绑定
         */
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId[0]);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[1]);
        //纹理作为颜色附着在FBO上
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, textureId[1], 0);

        GLES20.glGenRenderbuffers(1, fRender, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, fRender[0]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16,
                mWidth, mHeight);
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
                GLES20.GL_RENDERBUFFER, fRender[0]);
    }

    private int[] loadTextures(Context context, int resourceId){
        final int[] textureObjectIds = new int[2];
        // 创建2个纹理
        GLES20.glGenTextures(2, textureObjectIds, 0);

        if (textureObjectIds[0] == 0 || textureObjectIds[1] == 0){
            Log.e("Test","gen texture fail");
            return textureObjectIds;
        }

        for(int i = 0; i < 2; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureObjectIds[i]);

            if (i == 0) {
                final BitmapFactory.Options options=new BitmapFactory.Options();
                options.inScaled=false;
                final Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),resourceId,options);
                if(bitmap==null){
                    Log.e("Test","load bitmap fial");
                    GLES20.glDeleteTextures(1,textureObjectIds,0);
                    return textureObjectIds;
                }

                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
                bitmap.recycle();
                GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

                // 后面 fbo 使用
                mWidth = bitmap.getWidth();
                mHeight = bitmap.getHeight();
            } else {
                //事先为FBO的纹理附着分配内存
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,mWidth,
                        mHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
            }
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            // 解绑纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        }



        return textureObjectIds;
    }
}
