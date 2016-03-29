package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    private static String login_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/login.php";

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

        String username;
        String password;

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            username = inputUsername.getText().toString();
            password = inputPassword.getText().toString();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("username", username);
            query.add("password", password);

            // getting JSON Object
            // Note that create product url accepts POST method
            jsonParser.setParams(query);
            JSONObject json = jsonParser.makeHttpRequest(login_url, "POST");

            // check for success tag
            try {
                if(!json.has("error")){
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    i.putExtra("username", username);
                    i.putExtra("id", json.getInt("id"));
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
