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

public class InsuranceEditActivity extends Activity {

    Globals globals = Globals.getInstance();

    EditText provider, mid, groupnum, rxbin, rxpcn, rxgrp;
    String id;
    Button save;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_edit_activity);

        provider = (EditText) findViewById(R.id.eprovider);
        mid = (EditText) findViewById(R.id.emid);
        groupnum = (EditText) findViewById(R.id.egroupnum);
        rxbin = (EditText) findViewById(R.id.erxbin);
        rxpcn = (EditText) findViewById(R.id.erxpcn);
        rxgrp = (EditText) findViewById(R.id.erxgrp);
        save = (Button) findViewById(R.id.save);

        try {
            id = globals.getCurrentInsurance().getString("id");
            provider.setText(globals.getCurrentInsurance().getString("provider"));
            mid.setText(globals.getCurrentInsurance().getString("mid"));
            groupnum.setText(globals.getCurrentInsurance().getString("groupnum"));
            rxbin.setText(globals.getCurrentInsurance().getString("rxbin"));
            rxpcn.setText(globals.getCurrentInsurance().getString("rxpcn"));
            rxgrp.setText(globals.getCurrentInsurance().getString("rxgrp"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    globals.getCurrentInsurance().put("provider", provider.getText().toString());
                    globals.getCurrentInsurance().put("mid", mid.getText().toString());
                    globals.getCurrentInsurance().put("groupnum", groupnum.getText().toString());
                    globals.getCurrentInsurance().put("rxbin", rxbin.getText().toString());
                    globals.getCurrentInsurance().put("rxpcn", rxpcn.getText().toString());
                    globals.getCurrentInsurance().put("rxgrp", rxgrp.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new UpdateIns().execute();
            }
        });
    }

    class UpdateIns extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String update_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/updatePrescription.php";

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
            QueryString query = new QueryString("id", id);
            query.add("provider", insProvider);
            query.add("mid", insMID);
            query.add("groupnum", insGroupnum);
            query.add("rxbin", insRxBin);
            query.add("rxpcn", insRxPCN);
            query.add("rxgrp", insRxGrp);

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

