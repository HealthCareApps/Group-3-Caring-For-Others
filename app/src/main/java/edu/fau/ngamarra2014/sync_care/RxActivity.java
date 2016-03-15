package edu.fau.ngamarra2014.sync_care;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class RxActivity extends FragmentActivity implements RxListFragment.RxListListener {

    private RxInfoFragment info = (RxInfoFragment) getSupportFragmentManager().findFragmentById(R.id.info);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx);
    }

    @Override
    public void onRxClick(String name) {
        info.changeTextProperties(name);
    }
}
