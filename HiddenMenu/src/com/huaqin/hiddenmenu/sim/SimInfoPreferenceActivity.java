package com.huaqin.hiddenmenu.sim;

import android.content.res.Resources;
import android.os.Bundle;
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
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.huaqin.hiddenmenu.R;

import java.util.List;
import android.util.Log;
import static android.R.attr.key;

public class SimInfoPreferenceActivity extends PreferenceActivity {
    private static final String TAG = "SimInfoPreferenceActivity";

    private static final String KEY_MCC = "sim_info_mcc";
    private static final String KEY_MNC = "sim_info_mnc";
    private static final String KEY_GID = "sim_info_gid";
    private static final String KEY_SPN = "sim_info_spn";
    private static final String KEY_IMSI = "sim_info_imsi";
    private static final String KEY_ICCID = "sim_info_iccid";

    private TelephonyManager mTelephonyManager;
    private Phone mPhone = null;
    private Resources mRes;
    private Preference mSignalStrength;
    private SubscriptionInfo mSir;
    private boolean mShowLatestAreaInfo;

    // Default summary for items
    private String mDefaultText;

    private TabHost mTabHost;
    private TabWidget mTabWidget;
    private ListView mListView;
    private List<SubscriptionInfo> mSelectableSubInfos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        mSelectableSubInfos = SubscriptionManager.from(this).getActiveSubscriptionInfoList();

        addPreferencesFromResource(R.xml.sim_info);

        mRes = getResources();


        if (mSelectableSubInfos == null) {
            mSir = null;
        } else {
            mSir = mSelectableSubInfos.size() > 0 ? mSelectableSubInfos.get(0) : null;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePreference();
    }


    private OnTabChangeListener mTabListener = new OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            final int slotId = Integer.parseInt(tabId);
            mSir = mSelectableSubInfos.get(slotId);

            // The User has changed tab; update the SIM information.
            updatePreference();
        }
    };

    private TabContentFactory mEmptyTabContent = new TabContentFactory() {
        @Override
        public View createTabContent(String tag) {
            return new View(mTabHost.getContext());
        }
    };

    private TabSpec buildTabSpec(String tag, String title) {
        return mTabHost.newTabSpec(tag).setIndicator(title).setContent(
                mEmptyTabContent);
    }

    private void updatePreference() {
        if(mSir != null){
            //final String mccMncString = mTelephonyManager.getSimOperatorNumericForSubscription(mSir.getSubscriptionId());
            final String mccMncString = mTelephonyManager.getNetworkOperatorForPhone(mSir.getSimSlotIndex());//getSimSlotIndex mPhoneId
            setSummaryText(KEY_MCC,mccMncString.substring(0, 3));
            setSummaryText(KEY_MNC,mccMncString.substring(3));
            setSummaryText(KEY_ICCID,String.valueOf(mSir.getIccId()));
            setSummaryText(KEY_IMSI,mTelephonyManager.getSubscriberId(mSir.getSubscriptionId()));
            setSummaryText(KEY_GID,mTelephonyManager.getGroupIdLevel1(mSir.getSubscriptionId()));
            
            setSummaryText(KEY_SPN,mSir.getCarrierName().toString());
            //Log.d(TAG,"mnc ="+mSir.getMnc()+" mccmnc="+);
        }

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
