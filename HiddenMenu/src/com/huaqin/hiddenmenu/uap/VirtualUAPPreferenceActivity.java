package com.huaqin.hiddenmenu.uap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.*;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.huaqin.hiddenmenu.R;
import com.huaqin.hiddenmenu.uap.BrowserUapString;

/**
 * Created by wangxiaoyu on 16-7-30.
 */
public class VirtualUAPPreferenceActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    private static final String KEY_SIMULATE_SIM_CARD = "simulate_sim_card";
    private static final String KEY_BROWSER_UA = "browser_ua";
    private static final String KEY_BROWSER_UAP = "browser_uap";
    private static final String KEY_MMS_UA = "mms_ua";
    private static final String KEY_MMS_UAP = "mms_uap";
    private TelephonyManager telephony;
    private String mUaString = null;
    private String mUapString = null;
    private WebView webView;
    WebSettings webSettings;
    private String mBrowserUaString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.vuap_preference);
    }

    @Override
    protected void onResume() {
        super.onResume();

        webView = new WebView(getApplicationContext());
        webSettings = webView.getSettings();
        webSettings.getUserAgent();
        mBrowserUaString = webSettings.getUserAgentString();
        EditTextPreference browserUaPreference = (EditTextPreference)findPreference(KEY_BROWSER_UA);
        browserUaPreference.setOnPreferenceChangeListener(this);
        browserUaPreference.setText(mBrowserUaString);

        telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mUaString = telephony.getMmsUserAgent();
        mUapString = telephony.getMmsUAProfUrl();

        EditTextPreference mmsUaPreference = (EditTextPreference)findPreference(KEY_MMS_UA);
        mmsUaPreference.setOnPreferenceChangeListener(this);
        mmsUaPreference.setText(mUaString);

        EditTextPreference mmsUaPPreference = (EditTextPreference)findPreference(KEY_MMS_UAP);
        mmsUaPPreference.setOnPreferenceChangeListener(this);
        mmsUaPPreference.setText(mUapString);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(preference == null){
            return false;
        }
        if(preference.getKey().equals(KEY_BROWSER_UA)){
            ((EditTextPreference)preference).setText(mBrowserUaString);
        }
        if(preference.getKey().equals(KEY_BROWSER_UAP)){
            Intent intent = new Intent(VirtualUAPPreferenceActivity.this, BrowserUapString.class);
            startActivity(intent);
        }
        if(preference.getKey().equals(KEY_MMS_UA)){
            ((EditTextPreference)preference).setText(mUaString);
        }
        if(preference.getKey().equals(KEY_MMS_UAP)){
            ((EditTextPreference)preference).setText(mUapString);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference.getKey().equals(KEY_MMS_UA) || preference.getKey().equals(KEY_MMS_UAP)){
            Settings.Global.putString(getContentResolver(), preference.getKey(), newValue.toString());
        }
        if(preference.getKey().equals(KEY_BROWSER_UA)){
            Settings.Global.putString(getContentResolver(), preference.getKey(), newValue.toString());
            //webSettings.setUserAgentString(newValue.toString());
        }

        return true;
    }
}
