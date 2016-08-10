package com.huaqin.hiddenmenu.calltest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.huaqin.hiddenmenu.R;

import java.io.File;
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;

public class CallDurationActivity extends Activity {
    private static final String TAG = "CallDurationActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.call_duration_layout);

        long total = 0;

        File f1 = new File("/persist/callduration", "CallDuration1");
        android.util.Log.i("CallTimer", "[CallDurationActivity] CallDuration1");
        if (f1.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f1);  
                byte[] buffer = new byte[2048];
                int len = fis.read(buffer);
                if (len > 0) {
                    total += Long.parseLong(new String(buffer, 0, len));
                }
                android.util.Log.i("CallTimer", "[CallDurationActivity] CallDuration1: " + total);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File f2 = new File("/persist/callduration", "CallDuration2");
        android.util.Log.i("CallTimer", "[CallDurationActivity] CallDuration2");
        if (f2.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f2);  
                byte[] buffer = new byte[2048];
                int len = fis.read(buffer);
                if (len > 0) {
                    total += Long.parseLong(new String(buffer, 0, len));
                }
                android.util.Log.i("CallTimer", "[CallDurationActivity] CallDuration2: " + total);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long hour = total/(60*60*1000);
        long min = (total%(60*60*1000))/(60*1000);
        long sec = ((total%(60*60*1000))%(60*1000))/1000;
        long ms = ((total%(60*60*1000))%(60*1000))%1000;

        TextView tv_CallDuration = (TextView)findViewById(R.id.call_duration);
        tv_CallDuration.setText(String.format("%02d:%02d:%02d:%03d", hour, min, sec, ms));
    }
}
