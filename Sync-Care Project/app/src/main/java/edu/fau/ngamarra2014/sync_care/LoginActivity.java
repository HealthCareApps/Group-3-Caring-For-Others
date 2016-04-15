package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        //Text Fields
        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);

        //Buttons
        create = (TextView) findViewById(R.id.signup);
        signin = (Button) findViewById(R.id.login);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(getApplicationContext(), registerActivity.class));}
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Signin(v).execute();
            }
        });

    }

    class Signin extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String login_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/login.php";

        private View view;
        String username;
        String password;

        public Signin(View v){
            this.view = v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            //Getting user credentials
            username = inputUsername.getText().toString();
            password = inputPassword.getText().toString();
        }

        protected String doInBackground(String... args) {

            // Building Parameters for php
            QueryString query = new QueryString("username", username);
            query.add("password", password);

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(login_url, "POST");

            try {
                //Login successful
                if(!json.getString(0).equals("Invalid")){
                    //Sets the users information to globals class
                    globals.setUser(json.getJSONObject(0));
                    //Send user to Home Page
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish(); //Finish the Login activity so it is removed from the stack
                } else {
                    //Wrong credentials
                    Snackbar.make(view, "Invalid credentials", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) { pDialog.dismiss();}
    }
}
