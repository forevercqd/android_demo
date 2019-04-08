package com.example.demoopengl.demoopengl;


import android.content.Context;
import android.graphics.Shader;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.demoopengl.demoopengl.util.LoggerConfig;
import com.example.demoopengl.demoopengl.util.ShaderHelper;
import com.example.demoopengl.demoopengl.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceRender implements GLSurfaceView.Renderer{
    private Context context = null;
    private final FloatBuffer vertexData;
    private static final int BYTES_PER_FLOAT = 4;
    private int program;

    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";

    private static final int POSITION_COMOPNENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMOPNENT_COUNT+COLOR_COMPONENT_COUNT)*BYTES_PER_FLOAT;

    int aPostionLocation;
    int aColorLocation;

    private static final String U_MATRIX = "u_Matrix";
    final float[] projectionMatrix = new float[16];
    int uMatrixLocation;


    private final String TAG = "GLSurfaceRender";


    float[] tableVertices = {
            //Order of coordinates:X,Y,R,G,B
            //Triangle Fan
            0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

            // 水平分割线
            -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.5f, 0.0f, 1.0f, 0.0f, 0.0f,

            // 下中心点
            0.0f, -0.25f, 0.0f, 0.0f, 1.0f,

            // 上中心点
            0.0f, 0.25f, 1.0f, 0.0f, 0.0f
    };

    public GLSurfaceRender(Context context){
        this.context = context;

        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT).
                order(ByteOrder.nativeOrder()).
                asFloatBuffer();
        vertexData.put(tableVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // 获取shader文件名
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program);
        }

        GLES20.glUseProgram(program); // a_Position
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        aPostionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);


        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPostionLocation, POSITION_COMOPNENT_COUNT, GLES20.GL_FLOAT,
        true, STRIDE, vertexData); // 指定了渲染时索引值为 index 的顶点属性数组的数据格式和位置
        GLES20.glEnableVertexAttribArray(
                aPostionLocation);// Enable or disable a generic vertex attribute array

        vertexData.position(POSITION_COMOPNENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,true,STRIDE,vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        final float aspectRatio = width > height ? (1.0f * width / height) : (1.0f * height / width);
        if (width > height) {
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1, -1, 1);
        } else {
            Matrix.orthoM(projectionMatrix, 0, -1, 1, -aspectRatio, aspectRatio, -1, 1);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);    // cqd.note 此处的颜色渐变是如何做到的？

        GLES20.glDrawArrays(GLES20.GL_LINES,6,2);

        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);

        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);
    }
}
