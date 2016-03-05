package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends FragmentActivity
        implements ToolbarFragment.ToolbarListener {

    Button settings;
    String username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        settings = (Button) findViewById(R.id.settings);

        TextView welcome = (TextView) findViewById(R.id.welcome);
        username = getIntent().getStringExtra("username");
        welcome.setText("Welcome " + username);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                i.putExtra("settingsFor", username);
                startActivity(i);

            }
        });

    }

    public void onButtonClick(int fontsize, String text) {

        TextFragment textFragment =
                (TextFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment2);
        textFragment.changeTextProperties(fontsize, text);
    }
}
