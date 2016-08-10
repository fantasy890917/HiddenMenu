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
import com.huaqin.hiddenmenu.R;
public class eltAutoActivity extends Activity {
    String para = "1";
    RadioButton rb = null;
    Button testBt = null;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.elt_auto_activity);
        Log.i("hiddenMenu", "elt auto activity!!!");
        rb = ((RadioButton) findViewById(R.id.radio1));
        testBt = ((Button) findViewById(R.id.button1));
        testBt.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent intent = new Intent();
                intent.setClass(eltAutoActivity.this, eltActivity.class);
                switch (rb.getId()){
                    case R.id.radio1:
                        para = "1";
                        break;
                    case R.id.radio2:
                        para = "2";
                        break;
                    case R.id.radio3:
                        para = "3";
                        break;
                    case R.id.radio4:
                        para = "4";
                        break;
                    case R.id.radio5:
                        para = "5";
                        break;
                    case R.id.radio6:
                        para = "0";
                        break;
                    case R.id.radio7:
                        para = "100";
                        break;
                }
                intent.putExtra("elt_count", para);
                startActivity(intent);
            }
        });

        ((RadioGroup) findViewById(R.id.radioGroup1)).setOnCheckedChangeListener(
                new OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup radioGroup, int id) {
                        int checked = radioGroup.getCheckedRadioButtonId();
                        rb = ((RadioButton)findViewById(checked));
                    }
                }

        );
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
    }
}

