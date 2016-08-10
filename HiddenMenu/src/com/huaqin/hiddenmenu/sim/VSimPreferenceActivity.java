package com.huaqin.hiddenmenu.sim;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.huaqin.hiddenmenu.R;
import com.android.internal.telephony.TelephonyProperties;
import java.util.List;
import android.os.SystemProperties;
import static android.R.attr.key;
import android.preference.PreferenceScreen;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

public class VSimPreferenceActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "VSimPreferenceActivity";

    private static final String KEY_MCCMNC = "vsim_mccmnc";
    private static final String KEY_GID = "vsim_gid";
    private static final String KEY_SPN = "vsim_spn";
    private static final String KEY_IMSI = "vsim_imsi";
    private static final String KEY_NTCODE = "vsim_ntcode";
    private static final String KEY_NTCODE_SUBSET = "vsim_ntcode_subset";
    private static final String KEY_FLEX_MODE = "vsim_flexmode";

    private EditTextPreference mMccMnc;
    private EditTextPreference mGid;
    private EditTextPreference mSpn;
    private EditTextPreference mImsi;
    private EditTextPreference mNtCodeMccMNc;
    private EditTextPreference mNtCodeSubset;
    private Preference mFlexMode;

    private TabHost mTabHost;
    private TabWidget mTabWidget;
    private ListView mListView;
    private List<SubscriptionInfo> mSelectableSubInfos;
    private TelephonyManager mTelephonyManager;
    private SubscriptionInfo mSir;
    private SubscriptionManager mSubscriptionManager = null;
    // Default summary for items
    private String mDefaultText;
    private static int mPhoneId = -1;

    //HQ add by fanta for flex mode-START
    private static final String LG_FLEX_MODE_ENABLE = "persist.service.flex.enable";
    private static final String LG_FLEX_MODE_MCCMNC = "persist.service.flex.sim-mccmnc";
    private static final String LG_FLEX_MODE_GID = "persist.service.flex.sim-gid";
    private static final String LG_FLEX_MODE_SPN = "persist.service.flex.sim-spn";
    private static final String LG_FLEX_MODE_IMSI = "persist.service.flex.sim-imsi";
    //HQ add by fanta-END
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.vsim_preference);
        init();
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mSelectableSubInfos = SubscriptionManager.from(this).getActiveSubscriptionInfoList();
        mSubscriptionManager = SubscriptionManager.from(this);
        //if(mSelectableSubInfos == null
        //        || mSelectableSubInfos.size() ==0){
        //    Toast.makeText(this,getResources().getString(R.string.null_sim_card),Toast.LENGTH_SHORT);
        //    finish();
        //}

       // if (mSelectableSubInfos == null) {
       //     mSir = null;
       // } else {
           // mSir = mSelectableSubInfos.size() > 0 ? mSelectableSubInfos.get(0) : null;
            mPhoneId =0;
            /*
            if (mSelectableSubInfos.size() > 1) {
                setContentView(com.android.internal.R.layout.common_tab_settings);

                mTabHost = (TabHost) findViewById(android.R.id.tabhost);
                mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
                mListView = (ListView) findViewById(android.R.id.list);

                mTabHost.setup();
                mTabHost.setOnTabChangedListener(mTabListener);
                mTabHost.clearAllTabs();

                for (int i = 0; i < mSelectableSubInfos.size(); i++) {
                    mTabHost.addTab(buildTabSpec(String.valueOf(i),
                            String.valueOf(mSelectableSubInfos.get(i).getDisplayName())));
                }
            }
            
        }
        */
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Log.d(TAG,"onPreferenceClick");
        String key = preference.getKey();
        if(KEY_FLEX_MODE.equals(key)){
            String flex_mode = SystemProperties.get(LG_FLEX_MODE_ENABLE,"0");
            if(flex_mode.equals("1")){
                SystemProperties.set(LG_FLEX_MODE_ENABLE,"0");
                mFlexMode.setSummary(R.string.default_mode_off);
            }else{
                SystemProperties.set(LG_FLEX_MODE_ENABLE,"1");
                mFlexMode.setSummary(R.string.default_mode_on);
            }
        }
        return true;
    }
    
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        Log.d(TAG,"onPreferenceChange key="+key);
        if (KEY_MCCMNC.equals(key)) {
            try {
                String mccmnc = (String) newValue;
                SystemProperties.set(LG_FLEX_MODE_MCCMNC,mccmnc);
                mMccMnc.setSummary(mccmnc);
                Log.d(TAG,"mccmnc="+mccmnc+"mPhoneId="+mPhoneId);
            } catch (NumberFormatException e) {
                return false;
            }
        }else if (KEY_IMSI.equals(key)) {
            try {
                String imsi = (String) newValue;
                if(imsi.length()<6 || imsi.length()>15){
                    new AlertDialog.Builder(VSimPreferenceActivity.this).setTitle("")
                            .setMessage(R.string.imsi_format)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {//添加确定按钮
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                      // TODO Auto-generated method stub
                                      finish();
                                  }
                            }).show();
                    return false;
                }
                SystemProperties.set(LG_FLEX_MODE_IMSI,imsi);
                mImsi.setSummary(imsi);
                Log.d(TAG,"imsi="+imsi+"mPhoneId="+mPhoneId);
            } catch (NumberFormatException e) {
                return false;
            }
        }else if(KEY_SPN.equals(key)){
            try {
                String spn = (String) newValue;
                SystemProperties.set(LG_FLEX_MODE_SPN,spn);
                mSpn.setSummary(spn);
                Log.d(TAG,"spn="+spn+"mPhoneId="+mPhoneId);

            } catch (NumberFormatException e) {
                return false;
            }
        }else if(KEY_GID.equals(key)){
            try {
                String gid = (String) newValue;
                SystemProperties.set(LG_FLEX_MODE_GID,gid);
                mGid.setSummary(gid);
                Log.d(TAG,"gid="+gid+"mPhoneId="+mPhoneId);

            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    private void init(){
        Log.d(TAG,"init()");
        mMccMnc = (EditTextPreference) findPreference(KEY_MCCMNC);
        mGid = (EditTextPreference) findPreference(KEY_GID);
        mSpn = (EditTextPreference) findPreference(KEY_SPN);
        mImsi = (EditTextPreference) findPreference(KEY_IMSI);
        mNtCodeMccMNc = (EditTextPreference) findPreference(KEY_NTCODE);
        mNtCodeSubset = (EditTextPreference) findPreference(KEY_NTCODE_SUBSET);
        mFlexMode =findPreference(KEY_FLEX_MODE);
        mFlexMode.setEnabled(true);
        mMccMnc.setOnPreferenceChangeListener(this);
        mSpn.setOnPreferenceChangeListener(this);
        mGid.setOnPreferenceChangeListener(this);
        mImsi.setOnPreferenceChangeListener(this);
        
        String flex_mode = SystemProperties.get(LG_FLEX_MODE_ENABLE,"0");
        String flex_summary = getResources().getString(R.string.default_mode_off);
        if(flex_mode.equals("1")){
                flex_summary = getResources().getString(R.string.default_mode_on);
        }
        setSummaryText(KEY_FLEX_MODE,flex_summary);
        setSummaryText(KEY_MCCMNC,SystemProperties.get(LG_FLEX_MODE_MCCMNC,""));
        setSummaryText(KEY_IMSI,SystemProperties.get(LG_FLEX_MODE_IMSI,""));
        setSummaryText(KEY_SPN,SystemProperties.get(LG_FLEX_MODE_SPN,""));
        setSummaryText(KEY_GID,SystemProperties.get(LG_FLEX_MODE_GID,""));
    }

    private TabHost.OnTabChangeListener mTabListener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            final int slotId = Integer.parseInt(tabId);
            mSir = mSelectableSubInfos.get(slotId);
            mPhoneId = slotId;
            // The User has changed tab; update the SIM information.
           // updatePreference();
        }
    };

    private void updatePreference(){
        if(mSir != null){
            setSummaryText(KEY_MCCMNC,mTelephonyManager.getSimOperator(mSir.getSubscriptionId()));
        }
    }


    private TabHost.TabContentFactory mEmptyTabContent = new TabHost.TabContentFactory() {
        @Override
        public View createTabContent(String tag) {
            return new View(mTabHost.getContext());
        }
    };

    private TabHost.TabSpec buildTabSpec(String tag, String title) {
        return mTabHost.newTabSpec(tag).setIndicator(title).setContent(
                mEmptyTabContent);
    }

    private void setSummaryText(String key, String text) {
        if (TextUtils.isEmpty(text)) {
            text = mDefaultText;
        }
        // some preferences may be missing
        final Preference preference = findPreference(key);
        if (preference != null) {
            preference.setSummary(text);
        }
    }
}

