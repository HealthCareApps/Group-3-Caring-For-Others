package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class HomeActivity extends FragmentActivity
        implements ToolbarFragment.ToolbarListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

    }

    public void onButtonClick(int fontsize, String text) {

        TextFragment textFragment =
                (TextFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment2);
        textFragment.changeTextProperties(fontsize, text);
    }
}
