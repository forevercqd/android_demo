package com.example.demoopengl.demoopengl.util;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by Administrator on 2016/6/30.
 */
public class ShaderHelper {
  private static final String TAG = "ShaderHelper";

  public static int compileVertexShader(String shaderCode) {
    return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
  }

  public static int compileFragmentShader(String shaderCode) {
    return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
  }

  private static int compileShader(int type, String shaderCode) {
    final int shaderObjectId = GLES20.glCreateShader(type);  // step.1 创建　shader
    if (shaderObjectId == 0) {
      if (LoggerConfig.ON) {
        Log.w(TAG, "Could not create new shader.");
      }
    }
    GLES20.glShaderSource(shaderObjectId, shaderCode);  // step.2 加载Shader源码
    GLES20.glCompileShader(shaderObjectId); // step.3 编译Shader

    final int[] compileStatus = new int[1];

    GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0); // step.4 检查Shader状态

    if (LoggerConfig.ON) {
      Log.v(TAG,
          "Results of compiling source:" + "\n" + shaderCode + "\n:" + GLES20.glGetShaderInfoLog(
              shaderObjectId));
    }

    if (compileStatus[0] == 0) {
      GLES20.glDeleteShader(shaderObjectId);
      if (LoggerConfig.ON) {
        Log.e(TAG, "Compilation of shader failed");
      }
    }
    return shaderObjectId;
  }

  public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
    final int programObjectId = GLES20.glCreateProgram(); // step.5 创建Program
    if (programObjectId == 0) {
      Log.w(TAG, "Could not create new program");
    }

    GLES20.glAttachShader(programObjectId, vertexShaderId); // setp.6 绑定Program与Shader
    if (LoggerConfig.ON) {
      Log.v(TAG, "cqd, Results of glAttachShader, vertexShaderId:\n" + GLES20.glGetProgramInfoLog(programObjectId));
    }

    GLES20.glAttachShader(programObjectId, fragmentShaderId);
    if (LoggerConfig.ON) {
      Log.v(TAG, "Results of glAttachShader, fragmentShaderId:\n" + GLES20.glGetProgramInfoLog(programObjectId));
    }


    GLES20.glLinkProgram(programObjectId);  // step.7 链接Program

    final int[] linkStatus = new int[1];
    GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0); // step.8 检查Program状态

    if (LoggerConfig.ON) {
      Log.v(TAG, "Results of linking program:\n" + GLES20.glGetProgramInfoLog(programObjectId));
    }

    if (linkStatus[0] == 0) {
      GLES20.glDeleteProgram(programObjectId);
      if (LoggerConfig.ON) {
        Log.w(TAG, "Linking of program failed.");
      }
    }

    return programObjectId;
  }

  public static boolean validateProgram(int programObjectId) {
    GLES20.glValidateProgram(programObjectId);

    final int[] validateStatus = new int[1];
    GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
    Log.v(TAG, "Results of validating program:"
        + validateStatus[0]
        + "\nLog:"
        + GLES20.glGetProgramInfoLog(programObjectId));

    return validateStatus[0] != 0;
  }
}
