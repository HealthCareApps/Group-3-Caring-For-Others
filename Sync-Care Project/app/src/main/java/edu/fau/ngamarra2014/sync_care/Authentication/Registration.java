package edu.fau.ngamarra2014.sync_care.Authentication;

import android.app.Activity;
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

import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.HomeActivity;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.R;

public class Registration extends Activity {

    //Globals globals = Globals.getInstance();
    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, null, null, 2);

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
                    user.setUsername(inputUsername.getText().toString());
                    user.setPassword(inputPassword.getText().toString());
                    user.setFirst(inputFirst.getText().toString());
                    user.setLast(inputLast.getText().toString());
                    user.setEmail(inputEmail.getText().toString());
                    user.setAccountType("primary");
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

        //JSONObject user = new JSONObject();
        //private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String register_caretaker_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/register.php";

        //String first, last, email, username, password;
        int success;
        String id;
        private JSONArray json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* first = inputFirst.getText().toString();
            last = inputLast.getText().toString();
            email = inputEmail.getText().toString();
            username = inputUsername.getText().toString();
            password = inputPassword.getText().toString();*/
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("first", user.getFirst());
            query.add("last", user.getLast());
            query.add("email", user.getEmail());
            query.add("username", user.getUsername());
            query.add("password", user.getPassword());

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
                    user.setID(json.getInt(1));
                    /*user.put("id", json.getString(1));
                    user.put("first", first);
                    user.put("last", last);
                    user.put("email", email);
                    user.put("username", username);
                    globals.setUser(user);*/
                    dbHandler.addUser(user);
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
