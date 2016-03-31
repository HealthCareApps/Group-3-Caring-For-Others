package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    EditText inputUsername;
    EditText inputPassword;
    TextView create;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        findViewById(R.id.background).getBackground().setAlpha(222);


        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);

        create = (TextView) findViewById(R.id.signup);
        signin = (Button) findViewById(R.id.login);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), registerActivity.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Signin().execute();
            }
        });

    }

    class Signin extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String login_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/login.php";

        String username;
        String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            username = inputUsername.getText().toString();
            password = inputPassword.getText().toString();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("username", username);
            query.add("password", password);

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(login_url, "POST");

            try {
                if(!json.getString(0).equals("Invalid")){

                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    globals.setUser(json.getJSONObject(0));
                    startActivity(i);

                    finish();
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
