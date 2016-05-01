package edu.fau.ngamarra2014.sync_care.Authentication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.HomeActivity;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.PatientListActivity;
import edu.fau.ngamarra2014.sync_care.R;

public class Registration extends Activity {
    User user = User.getInstance();
    DBHandler login = new DBHandler(this, "USERS", null, 2);
    MCrypt mcrypt = new MCrypt();

    EditText inputFirst, inputLast, inputEmail, inputUsername, inputPassword;
    Button register;
    RadioGroup account;
    RadioButton type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        inputFirst = (EditText) findViewById(R.id.firstname);
        inputLast = (EditText) findViewById(R.id.lastname);
        inputEmail = (EditText) findViewById(R.id.regemail);
        inputUsername = (EditText) findViewById(R.id.regusername);
        inputPassword = (EditText) findViewById(R.id.regpass);

        register = (Button) findViewById(R.id.register);
        account = (RadioGroup) findViewById(R.id.account);

        TextView login = (TextView) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FormValidation((ViewGroup) findViewById(R.id.registration))){
                    type = (RadioButton) findViewById(account.getCheckedRadioButtonId());
                    user.setUsername(inputUsername.getText().toString());
                    try {
                        user.setPassword(MCrypt.bytesToHex(mcrypt.encrypt(inputPassword.getText().toString())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    user.setFirst(inputFirst.getText().toString());
                    user.setLast(inputLast.getText().toString());
                    user.setEmail(inputEmail.getText().toString());
                    user.setAccountType(type.getText().toString());
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

        JSONParser jsonParser = new JSONParser();
        private String register_caretaker_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Authentication/register.php";

        private JSONObject response;

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("first", user.getFirst());
            query.add("last", user.getLast());
            query.add("email", user.getEmail());
            query.add("username", user.getUsername());
            query.add("password", user.getPassword());
            query.add("account", user.getAccountType());

            jsonParser.setParams(query);
            try {
                response = jsonParser.makeHttpRequest(register_caretaker_url, "POST");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            try {
                if (response.has("Successful")) {
                    user.setID(response.getInt("id"));
                    login.addUser(user);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }else if(response.has("Error")){
                    inputUsername.setError("Username already exists");
                }else if(response.has("Internet")){
                    Toast toast = Toast.makeText(Registration.this, "No Internet Connection", Toast.LENGTH_LONG);
                    toast.show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

    }

}
