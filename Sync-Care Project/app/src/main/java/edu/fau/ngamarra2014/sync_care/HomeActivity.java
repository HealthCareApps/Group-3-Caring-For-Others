package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;

public class HomeActivity extends NavigationActivity{

    User user = User.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.home_activity, null, false);
        drawer.addView(contentView, 0);
        //Log.i("Doctors", " " + user.getPatient(0).getNumberOfDoctors());
        //new GrabPatients().execute();
    }
    protected void onRestart(){
        super.onRestart();
        //Log.i("Doctors", " " + user.getPatient(0).getNumberOfDoctors());
    }

    /*class GrabPatients extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String grab_patient_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/getPatients.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Getting Patients...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", userid);

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(grab_patient_url, "GET");

            try {
                if(json.length() > 0){
                    //Set the patients for user
                    globals.setPatients(json);
                } else {
                    //If user has no patients initialize empty array
                    globals.setPatients(new JSONArray());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }*/
}
