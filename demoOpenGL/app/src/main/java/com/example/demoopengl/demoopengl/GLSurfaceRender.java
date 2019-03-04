package com.example.demoopengl.demoopengl;


import android.content.Context;
import android.graphics.Shader;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
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

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    int aPostionLocation;
    int uColorLocation;

    private final String TAG = "GLSurfaceRender";

    private static int POSITION_COMOPNENT_COUNT = 2;
    float [] tableVertices = {
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0f
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
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

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
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPostionLocation = GLES20.glGetAttribLocation(program, A_POSITION);


        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPostionLocation, POSITION_COMOPNENT_COUNT, GLES20.GL_FLOAT,
        true, 0, vertexData); // 指定了渲染时索引值为 index 的顶点属性数组的数据格式和位置
        GLES20.glEnableVertexAttribArray(
                aPostionLocation);// Enable or disable a generic vertex attribute array
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES20.glViewport(0, 0, i, i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f,
                1.0f);//为 u_Color 这个 Uniform 设置颜色值 RGB 为 0 1 0 1 绿色
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3);//画三角形

    }
}
