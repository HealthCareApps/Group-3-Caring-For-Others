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

public class DoctorEditActivity extends Activity {

    Globals globals = Globals.getInstance();

    EditText name, type, phone, email, address, city, state, zip, fax;
    String id;
    Button save;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_edit_activity);

        name = (EditText) findViewById(R.id.ename);
        type = (EditText) findViewById(R.id.etype);
        phone = (EditText) findViewById(R.id.ephone);
        email = (EditText) findViewById(R.id.eemail);
        address = (EditText) findViewById(R.id.eaddress);
        city = (EditText) findViewById(R.id.ecity);
        state = (EditText) findViewById(R.id.estate);
        zip = (EditText) findViewById(R.id.ezip);
        fax = (EditText) findViewById(R.id.efax);
        save = (Button) findViewById(R.id.save);

        try {
            id = globals.getCurrentDoctor().getString("id");
            name.setText(globals.getCurrentDoctor().getString("name"));
            type.setText(globals.getCurrentDoctor().getString("type"));
            phone.setText(globals.getCurrentDoctor().getString("phone"));
            email.setText(globals.getCurrentDoctor().getString("email"));
            address.setText(globals.getCurrentDoctor().getString("address"));
            city.setText(globals.getCurrentDoctor().getString("city"));
            state.setText(globals.getCurrentDoctor().getString("state"));
            zip.setText(globals.getCurrentDoctor().getString("zip"));
            fax.setText(globals.getCurrentDoctor().getString("fax"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    globals.getCurrentDoctor().put("name", name.getText().toString());
                    globals.getCurrentDoctor().put("type", type.getText().toString());
                    globals.getCurrentDoctor().put("phone", phone.getText().toString());
                    globals.getCurrentDoctor().put("email", email.getText().toString());
                    globals.getCurrentDoctor().put("address", address.getText().toString());
                    globals.getCurrentDoctor().put("city", city.getText().toString());
                    globals.getCurrentDoctor().put("state", state.getText().toString());
                    globals.getCurrentDoctor().put("zip", zip.getText().toString());
                    globals.getCurrentDoctor().put("fax", fax.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new UpdateDoc().execute();
            }
        });
    }

    class UpdateDoc extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String update_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/updatePrescription.php";

        String docName, docType, docPhone, docEmail, docAddress, docCity, docState, docZip, docFax;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            docName = name.getText().toString();
            docType = type.getText().toString();
            docPhone = phone.getText().toString();
            docEmail = email.getText().toString();
            docAddress = address.getText().toString();
            docCity = city.getText().toString();
            docState = state.getText().toString();
            docZip = zip.getText().toString();
            docFax = fax.getText().toString();

        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", id);
            query.add("name", docName);
            query.add("type", docType);
            query.add("phone", docPhone);
            query.add("email", docEmail);
            query.add("address", docAddress);
            query.add("city", docCity);
            query.add("state", docState);
            query.add("zip", docZip);
            query.add("fax", docFax);


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
