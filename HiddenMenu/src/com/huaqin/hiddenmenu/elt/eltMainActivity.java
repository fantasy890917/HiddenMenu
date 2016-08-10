package com.huaqin.hiddenmenu.elt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.huaqin.hiddenmenu.R;

public class eltMainActivity extends Activity {
    RadioButton rb = null;
    Button testBt = null;
    TextView tv = null;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.elt_activity);
        Log.i("hiddenMenu", "elt main activity!!!");
        rb = ((RadioButton) findViewById(R.id.radio0));
        tv = ((TextView) findViewById(R.id.textView3));
        testBt = ((Button) findViewById(R.id.button1));
        testBt.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                if (rb.getId() == R.id.radio0) {
                    intent.setClass(eltMainActivity.this, eltAutoActivity.class);
                } else if (rb.getId() == R.id.radio1) {
                    intent.setClass(eltMainActivity.this, eltManualActivity.class);
                }
                startActivity(intent);
            }
        });
        ((RadioGroup) findViewById(R.id.radioGroup1)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                int checked = radioGroup.getCheckedRadioButtonId();
                rb = ((RadioButton) findViewById(checked));
                tv.setText(rb.getText());
            }
        });
    }
}

