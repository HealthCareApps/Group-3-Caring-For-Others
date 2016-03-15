package edu.fau.ngamarra2014.sync_care;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RxActivity extends FragmentActivity implements RxListFragment.RxListListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx);
    }

    @Override
    public void onRxClick(JSONObject obj) throws JSONException {
        RxInfoFragment info = (RxInfoFragment) getSupportFragmentManager().findFragmentById(R.id.info);
        info.changeTextProperties(obj);
    }
}
