package com.huaqin.hiddenmenu.uap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.mediatek.custom.CustomProperties;
import com.huaqin.hiddenmenu.R;

/**
 * Created by wangxiaoyu on 16-7-30.
 */
public class BrowserUapString extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_uap_string);

        String operatorUAP = CustomProperties.getString(CustomProperties.MODULE_BROWSER,
                CustomProperties.UAPROF_URL);
        TextView browser_uap = (TextView)findViewById(R.id.browser_uap);
        browser_uap.setText(operatorUAP);

    }

}
