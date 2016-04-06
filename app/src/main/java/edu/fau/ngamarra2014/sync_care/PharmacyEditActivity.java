package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class PharmacyEditActivity extends Activity {

    Globals globals = Globals.getInstance();

    EditText name, phone, email, address, city, state, zip;
    String id;
    Button save;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_edit_activity);

        name = (EditText) findViewById(R.id.epharmacy);
        phone = (EditText) findViewById(R.id.epharphone);
        email = (EditText) findViewById(R.id.epharemail);
        address = (EditText) findViewById(R.id.epharaddress);
        city = (EditText) findViewById(R.id.epharcity);
        state = (EditText) findViewById(R.id.epharstate);
        zip = (EditText) findViewById(R.id.epharzip);
        save = (Button) findViewById(R.id.save);

        try {
            id = globals.getCurrentPharmacy().getString("id");
            name.setText(globals.getCurrentPharmacy().getString("name"));
            phone.setText(globals.getCurrentPharmacy().getString("phone"));
            email.setText(globals.getCurrentPharmacy().getString("email"));
            address.setText(globals.getCurrentPharmacy().getString("address"));
            city.setText(globals.getCurrentPharmacy().getString("city"));
            state.setText(globals.getCurrentPharmacy().getString("state"));
            zip.setText(globals.getCurrentPharmacy().getString("zip"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    globals.getCurrentPharmacy().put("name", name.getText().toString());
                    globals.getCurrentPharmacy().put("phone", phone.getText().toString());
                    globals.getCurrentPharmacy().put("email", email.getText().toString());
                    globals.getCurrentPharmacy().put("address", address.getText().toString());
                    globals.getCurrentPharmacy().put("city", city.getText().toString());
                    globals.getCurrentPharmacy().put("state", state.getText().toString());
                    globals.getCurrentPharmacy().put("zip", zip.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new UpdatePhar().execute();
            }
        });
    }

    class UpdatePhar extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String update_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/updatePrescription.php";

        String pharName, pharPhone, pharEmail, pharAddress, pharCity, pharState, pharZip, pharFax;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pharName = name.getText().toString();
            pharPhone = phone.getText().toString();
            pharEmail = email.getText().toString();
            pharAddress = address.getText().toString();
            pharCity = city.getText().toString();
            pharState = state.getText().toString();
            pharZip = zip.getText().toString();

        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", id);
            query.add("name", pharName);
            query.add("phone", pharPhone);
            query.add("email", pharEmail);
            query.add("address", pharAddress);
            query.add("city", pharCity);
            query.add("state", pharState);
            query.add("zip", pharZip);


            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(update_url, "POST");

            try {
                int success = json.getInt(0);

                if (success == 1) {
                    finish();
                } else {
                    // failed
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

        }

    }
}
