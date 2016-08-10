package com.huaqin.hiddenmenu;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.huaqin.hiddenmenu.elt.eltMainActivity;
import com.huaqin.hiddenmenu.sim.SimInfoPreferenceActivity;
import com.huaqin.hiddenmenu.sim.VSimPreferenceActivity;
import com.huaqin.hiddenmenu.uap.VirtualUAPPreferenceActivity;
import com.huaqin.hiddenmenu.calltest.CallTestItemsActivity;

public class HideMenu extends ListActivity {

    private static final String TAG = "EM/HIDDEN_MENU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    private void setAdapter(){
        String[] settingList = new String[] {
                getResources().getString(R.string.list_sim_info),
                getResources().getString(R.string.list_vsim_test),
                getResources().getString(R.string.list_virtual_uap),
                getResources().getString(R.string.elt_title),
                getResources().getString(R.string.list_call_test)
        };
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                settingList));
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Log.d(TAG,"onListItemClick  position=="+position);
        switch (position) {
            case 0:
                Intent SimInfoPreferenceIntent = new Intent(HideMenu.this,
                        SimInfoPreferenceActivity.class);
                startActivity(SimInfoPreferenceIntent);
                break;
            case 1:
                Intent VSimPreferenceIntent = new Intent(HideMenu.this,
                        VSimPreferenceActivity.class);
                startActivity(VSimPreferenceIntent);
                break;
            case 2:
                Intent VirtualUAPIntent = new Intent(HideMenu.this,
                        VirtualUAPPreferenceActivity.class);
                startActivity(VirtualUAPIntent);
                break;
            case 3:
                Intent ELTIntent = new Intent(HideMenu.this,
                        eltMainActivity.class);
                startActivity(ELTIntent);
                break;
			case 4:
                Intent viewDurationIntent = new Intent(HideMenu.this,
                        CallTestItemsActivity.class);
                startActivity(viewDurationIntent);
                break;
            default:
                break;
        }
    }
}
