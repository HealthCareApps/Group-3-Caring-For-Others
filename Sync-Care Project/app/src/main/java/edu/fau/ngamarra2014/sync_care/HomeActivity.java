package edu.fau.ngamarra2014.sync_care;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


public class HomeActivity extends NavigationActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.home_activity, null, false);
        drawer.addView(contentView, 0);

    }
    protected void onRestart(){
        super.onRestart();

    }
}
