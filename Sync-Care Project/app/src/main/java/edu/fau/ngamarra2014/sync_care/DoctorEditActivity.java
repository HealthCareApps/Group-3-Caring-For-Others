package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Data.Doctor;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;

public class DoctorEditActivity extends Activity {

    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, null, null, 2);
    private String url;

    EditText name, type, phone, email, address, city, state, zip, fax;
    int id = 0;
    Button save, add;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_edit_activity);

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
        add = (Button) findViewById(R.id.add);

        if(user.patient.doctor != null) {
            save.setVisibility(View.VISIBLE);
            id = user.patient.doctor.getID();
            name.setText(user.patient.doctor.getName());
            type.setText(user.patient.doctor.getType());
            phone.setText(user.patient.doctor.getContactInfo()[0]);
            email.setText(user.patient.doctor.getContactInfo()[2]);
            address.setText(user.patient.doctor.getAddress()[0]);
            city.setText(user.patient.doctor.getAddress()[1]);
            state.setText(user.patient.doctor.getAddress()[2]);
            zip.setText(user.patient.doctor.getAddress()[3]);
            fax.setText(user.patient.doctor.getContactInfo()[1]);
        }else add.setVisibility(View.VISIBLE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/updateDoc.php";
                new UpdateDoc().execute();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/addDoc.php";
                new UpdateDoc().execute();
            }
        });
    }

    class UpdateDoc extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();

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
            QueryString query = new QueryString("database", "Doctors");
            query.add("Patient", Integer.toString(user.patient.getID()));
            query.add("name", docName);
            query.add("type", docType);
            query.add("phone", docPhone);
            query.add("email", docEmail);
            query.add("address", docAddress);
            query.add("city", docCity);
            query.add("state", docState);
            query.add("zip", docZip);
            query.add("fax", docFax);
            if(id != 0) query.add("id", Integer.toString(id));

            jsonParser.setParams(query);
            JSONObject response = jsonParser.makeHttpRequest(url, "POST");

            try {
               if (response.has("Successful")) {
                    Doctor doc = new Doctor();
                    doc.setID(response.getInt("id"));
                    doc.setName(docName);
                    doc.setContactInfo(docPhone, docFax, docEmail);
                    doc.setAddress(docAddress, docCity, docState, docZip);
                    doc.setType(docType);
                    doc.setPatient(user.patient.getID());
                    if(response.getString("Successful").equals("Updated")){
                        user.patient.doctor.update(doc);
                        dbHandler.updateDoctor(doc);
                    }else{
                        user.patient.addDoctor(doc);
                        dbHandler.addDoctor(doc);
                    }

                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
