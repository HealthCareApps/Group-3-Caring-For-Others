package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Data.Insurance;
import edu.fau.ngamarra2014.sync_care.Data.Prescription;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;

public class InsuranceEditActivity extends Activity {

    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, null, null, 2);
    private String url;

    EditText provider, mid, groupnum, rxbin, rxpcn, rxgrp;
    int id = 0;
    Button save, add;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insurance_edit_activity);

        provider = (EditText) findViewById(R.id.eprovider);
        mid = (EditText) findViewById(R.id.emid);
        groupnum = (EditText) findViewById(R.id.egroupnum);
        rxbin = (EditText) findViewById(R.id.erxbin);
        rxpcn = (EditText) findViewById(R.id.erxpcn);
        rxgrp = (EditText) findViewById(R.id.erxgrp);
        save = (Button) findViewById(R.id.save);
        add = (Button) findViewById(R.id.add);

        if(user.patient.insurance != null){
            id = user.patient.insurance.getID();
            provider.setText(user.patient.insurance.getProvider());
            mid.setText(user.patient.insurance.getMID());
            groupnum.setText(user.patient.insurance.getGroupNumber());
            rxbin.setText(user.patient.insurance.getRxBin());
            rxpcn.setText(user.patient.insurance.getRxPcn());
            rxgrp.setText(user.patient.insurance.getRxGroup());
            save.setVisibility(View.VISIBLE);
        }else add.setVisibility(View.VISIBLE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/updateDoc.php";
                new UpdateIns().execute();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/addDoc.php";
                new UpdateIns().execute();
            }
        });
    }

    class UpdateIns extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();

        String insProvider, insMID, insGroupnum, insRxBin, insRxPCN, insRxGrp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            insProvider = provider.getText().toString();
            insMID = mid.getText().toString();
            insGroupnum = groupnum.getText().toString();
            insRxBin = rxbin.getText().toString();
            insRxPCN = rxpcn.getText().toString();
            insRxGrp = rxgrp.getText().toString();

        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("database", "Insurances");
            query.add("Patient", Integer.toString(user.patient.getID()));
            if(id != 0) query.add("id", Integer.toString(id));
            query.add("provider", insProvider);
            query.add("mid", insMID);
            query.add("groupnum", insGroupnum);
            query.add("rxbin", insRxBin);
            query.add("rxpcn", insRxPCN);
            query.add("rxgrp", insRxGrp);

            jsonParser.setParams(query);
            JSONObject response = jsonParser.makeHttpRequest(url, "POST");

            try {
                if (response.has("Successful")) {
                    Insurance insurance = new Insurance();
                    insurance.setID(response.getInt("id"));
                    insurance.setProvider(insProvider);
                    insurance.setMID(insMID);
                    insurance.setGroupNumber(insGroupnum);
                    insurance.setRxBin(insRxBin);
                    insurance.setRxPcn(insRxPCN);
                    insurance.setRxGroup(insRxGrp);
                    insurance.setPatient(user.patient.getID());
                    if(response.getString("Successful").equals("Updated")){
                        user.patient.insurance.update(insurance);
                        dbHandler.updateInsurance(insurance);
                    }else{
                        user.patient.addInsurance(insurance);
                        dbHandler.addInsurance(insurance);
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

