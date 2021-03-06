package edu.fau.ngamarra2014.sync_care.Add.Edit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Data.Pharmacy;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.R;

public class PharmacyEditActivity extends Activity {

    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, user.getUsername(), null, 2);
    private String url;

    EditText name, phone, address, city, state, zip;
    int id = 0;
    Button save, add;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacy_edit_activity);

        name = (EditText) findViewById(R.id.epharmacy);
        phone = (EditText) findViewById(R.id.epharphone);
        address = (EditText) findViewById(R.id.epharaddress);
        city = (EditText) findViewById(R.id.epharcity);
        state = (EditText) findViewById(R.id.epharstate);
        zip = (EditText) findViewById(R.id.epharzip);
        save = (Button) findViewById(R.id.save);
        add = (Button) findViewById(R.id.add);

        if(user.patient.pharmacy != null) {
            id = user.patient.pharmacy.getID();
            name.setText(user.patient.pharmacy.getName());
            phone.setText(user.patient.pharmacy.getPhone());
            address.setText(user.patient.pharmacy.getAddress()[0]);
            city.setText(user.patient.pharmacy.getAddress()[1]);
            state.setText(user.patient.pharmacy.getAddress()[2]);
            zip.setText(user.patient.pharmacy.getAddress()[3]);
            save.setVisibility(View.VISIBLE);
        }else add.setVisibility(View.VISIBLE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/updateDoc.php";
                new UpdatePhar().execute();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/addDoc.php";
                new UpdatePhar().execute();
            }
        });
    }

    class UpdatePhar extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        JSONObject response;
        String pharName, pharPhone, pharAddress, pharCity, pharState, pharZip;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pharName = name.getText().toString();
            pharPhone = phone.getText().toString();
            pharAddress = address.getText().toString();
            pharCity = city.getText().toString();
            pharState = state.getText().toString();
            pharZip = zip.getText().toString();

        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("database", "Pharmacies");
            query.add("Patient", Integer.toString(user.patient.getID()));
            if(id != 0) query.add("id", Integer.toString(id));
            query.add("name", pharName);
            query.add("phone", pharPhone);
            query.add("address", pharAddress);
            query.add("city", pharCity);
            query.add("state", pharState);
            query.add("zip", pharZip);


            jsonParser.setParams(query);


            try {
                response = jsonParser.makeHttpRequest(url, "POST");
                if (response.has("Successful")) {
                    Pharmacy pharmacy = new Pharmacy();
                    pharmacy.setID(response.getInt("id"));
                    pharmacy.setName(pharName);
                    pharmacy.setPhone(pharPhone);
                    pharmacy.setAddress(pharAddress, pharCity, pharState, pharZip);
                    pharmacy.setPatient(user.patient.getID());

                    if(response.getString("Successful").equals("Updated")){
                        user.patient.pharmacy.update(pharmacy);
                        dbHandler.updatePharmacy(pharmacy);
                    }else{
                        user.patient.addPharmacy(pharmacy);
                        dbHandler.addPharmacy(pharmacy);
                    }
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String url){
            super.onPostExecute(url);
            if(response.has("Internet")){
                Toast toast = Toast.makeText(PharmacyEditActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
