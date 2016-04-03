package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                if (FormValidation((ViewGroup) findViewById(R.id.registration))){
                    new CreateNewUser().execute();
                }
            }
        });

    }

    private boolean FormValidation(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                if (((EditText)view).getText().toString().length() == 0){
                    ((EditText) view).setError(((EditText) view).getHint() + " is required!");
                    return false;
                }
            }
        }
        return true;
    }

    class CreateNewUser extends AsyncTask<String, String, String> {

        JSONObject user = new JSONObject();
        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String register_caretaker_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/register.php";

        String first, last, email, username, password;
        int success;
        private JSONArray json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
            json = jsonParser.makeHttpRequest(register_caretaker_url, "POST");

            try {
                success = json.getInt(0);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            try {
                if (success == 1) {
                    user.put("id", json.getString(1));
                    user.put("first", first);
                    user.put("last", last);
                    user.put("email", email);
                    user.put("username", username);
                    globals.setUser(user);

                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                }else if(success == 0){
                    inputUsername.setError("Username already exists");
                }else if(success == 2){
                    Log.i("Database error", "Database failed!");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

    }

}
