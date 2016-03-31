package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Nick on 3/3/2016.
 */
public class registerActivity extends Activity {

    Globals globals = Globals.getInstance();

    EditText inputFirst;
    EditText inputLast;
    EditText inputEmail;
    EditText inputUsername;
    EditText inputPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        inputFirst = (EditText) findViewById(R.id.firstname);
        inputLast = (EditText) findViewById(R.id.lastname);
        inputEmail = (EditText) findViewById(R.id.regemail);
        inputUsername = (EditText) findViewById(R.id.regusername);
        inputPassword = (EditText) findViewById(R.id.regpass);

        Button register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new CreateNewUser().execute();
            }
        });

    }


    class CreateNewUser extends AsyncTask<String, String, String> {

        JSONObject user = new JSONObject();
        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String register_caretaker_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/register.php";

        String first, last, email, username, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(registerActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            first = inputFirst.getText().toString();
            last = inputLast.getText().toString();
            email = inputEmail.getText().toString();
            username = inputUsername.getText().toString();
            password = inputPassword.getText().toString();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("first", first);
            query.add("last", last);
            query.add("email", email);
            query.add("username", username);
            query.add("password", password);

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(register_caretaker_url, "POST");

            try {
                int success = json.getInt(0);

                if (success == 1) {
                    user.put("first", first);
                    user.put("last", last);
                    user.put("email", email);
                    user.put("username", username);
                    globals.setUser(user);

                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);

                    finish();
                } else {
                    // failed
                }
            } catch (JSONException e) {
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
