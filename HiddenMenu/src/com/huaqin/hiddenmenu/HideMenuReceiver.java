package com.huaqin.hiddenmenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.net.Uri;
public class HideMenuReceiver extends BroadcastReceiver {

    private static final String TAG = "EM/HIDDEN_MENU";
    private static final String SECRET_CODE_ACTION = "android.provider.Telephony.LG_SECRET_CODE";
    // process *#*#3646633#*#*
    private final Uri mEmUri = Uri.parse("android_secret_code://546368");

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) {
            Log.i("@M_" + TAG, "Null action");
            return;
        }
        if (intent.getAction().equals(SECRET_CODE_ACTION)) {
            Uri uri = intent.getData();
            Log.i("@M_" + TAG, "getIntent success in if");
            if (uri.equals(mEmUri)) {
                Intent intentEm = new Intent(context, HideMenu.class);
                intentEm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.i("@M_" + TAG, "Before start EM activity");
                context.startActivity(intentEm);
            } else {
                Log.i("@M_" + TAG, "No matched URI!");
            }
        } else {
            Log.i("@M_" + TAG, "Not SECRET_CODE_ACTION!");
        }
    }
}
