package com.example.robert.demo_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    // catch 后续处理工作
    public static boolean catchMethod() {
        Log.d(TAG, "call catchMethod and return  --->>  ");
        return false;
    }

    // finally后续处理工作
    public static void finallyMethod() {
        Log.d(TAG, "call finallyMethod and do something  --->>  ");
    }

    public static boolean catchTest() {
        try {
            int i = 10 / 0;          // 抛出 Exception，后续处理被拒绝
            Log.d(TAG, "i vaule is : " + i);
            return true;             // Exception 已经抛出，没有获得被执行的机会
        } catch (Exception e) {
            Log.d(TAG, " -- Exception --");
            return catchMethod();    // Exception 抛出，获得了调用方法并返回方法值的机会
        }
        finally {
            Log.d(TAG,"finally ok");
        }

//        Log.d(TAG,"catchTest, return normal");
//        return true;
    }


    public static boolean catchFinallyTest2() {
       try {
           int i = 10 / 2;  // 不抛出 Exception
           Log.d(TAG, "i vaule is : " + i);
           return true;   // 获得被执行的机会，但执行需要在 finally 执行完成之后才能被执行
       } catch (Exception e) {
           Log.d(TAG, " -- Exception --");
           return catchMethod();
        }finally{
            finallyMethod();
            return false; // finally 中含有 return 语句，这个 return 将结束这个方法，不会在执行完之后再跳回 try 或 catch 继续执行，方法到此结束，返回 false
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);

        // cqd.note 此处是使用 native　层字符串;
//        tv.setText(stringFromJNI());  // 如果此处打开则会使用 native　层定义的字符串　std::string hello = "Hello from C++";
        // 关于使用native层文本,具体见　add_library　中
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void sendMessage(View view){
        Log.d(TAG,"cqd, sendMessage");
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        TextView editText = (TextView) findViewById(R.id.sample_text);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
}
