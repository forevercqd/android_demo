package com.example.robert.demo_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
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

    }
}
