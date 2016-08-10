package com.huaqin.hiddenmenu.calltest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.util.Log;

import com.huaqin.hiddenmenu.R;

import java.io.File;
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CallTestItemsActivity extends PreferenceActivity {
    private static final String TAG = "CallTestItemsActivity";

    private static final String KEY_CALL_TEST = "call_test";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.call_test);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        Log.i("CallTimer", "[onPreferenceTreeClick] key:" + key);
        if (KEY_CALL_TEST.equals(key)) {
            viewSIMCallDuration();
        }
        
        return true;
    }

    private String getSIM1CallDuration() {
        File f1 = new File("/persist/callduration", "CallDuration1");
        if (f1.exists()) {
            long duration = 0;
            try {
                FileInputStream fis = new FileInputStream(f1);  
                byte[] buffer = new byte[2048];
                int len = fis.read(buffer);
                if (len > 0) {
                    duration = Long.parseLong(new String(buffer, 0, len));
                }
                fis.close();
            } catch(IOException e) {
                e.printStackTrace();
            }

            long hour = duration/(60*60*1000);
            long min = (duration%(60*60*1000))/(60*1000);
            long sec = ((duration%(60*60*1000))%(60*1000))/1000;
            long ms = ((duration%(60*60*1000))%(60*1000))%1000;

            return String.format("SIM1: %02d:%02d:%02d:%03d", hour, min, sec, ms);
        }
        
        return null;
    }

    private String getSIM2CallDuration() {
        File f2 = new File("/persist/callduration", "CallDuration2");
        if (f2.exists()) {
            long duration = 0;
            try {
                FileInputStream fis = new FileInputStream(f2);  
                byte[] buffer = new byte[8];
                int len = fis.read(buffer);
                if (len > 0) {
                    duration = Long.parseLong(new String(buffer, 0, len));
                }
                fis.close();
            } catch(IOException e) {
                e.printStackTrace();
            }

            long hour = duration/(60*60*1000);
            long min = (duration%(60*60*1000))/(60*1000);
            long sec = ((duration%(60*60*1000))%(60*1000))/1000;
            long ms = ((duration%(60*60*1000))%(60*1000))%1000;

            return String.format("SIM2: %02d:%02d:%02d:%03d", hour, min, sec, ms);
        }
        
        return null;
    }

    private void viewSIMCallDuration() {
        List<String> durationList = new ArrayList<String>();
        durationList.clear();
        
        String sim1Duration = getSIM1CallDuration();
        if (!TextUtils.isEmpty(sim1Duration)) {
            durationList.add(sim1Duration);
        }

        String sim2Duration = getSIM2CallDuration();
        if (!TextUtils.isEmpty(sim2Duration)) {
            durationList.add(sim2Duration);
        }
        
        if (durationList.size() > 0) {
            AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(R.string.call_duration_dialog_title)
                .setItems(durationList.toArray(new String[durationList.size()]), null)
                .setCancelable(true)
                .show();
       } else {
            AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(R.string.call_duration_dialog_title)
                .setMessage(R.string.no_calls)
                .setCancelable(true)
                .show();
       }
    }
}
