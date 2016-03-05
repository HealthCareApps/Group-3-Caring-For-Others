package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        TextView welcome = (TextView) findViewById(R.id.welcome);
        welcome.setText("Welcome " + getIntent().getStringExtra("username"));
    }
}
