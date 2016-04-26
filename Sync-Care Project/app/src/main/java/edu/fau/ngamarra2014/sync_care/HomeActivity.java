package edu.fau.ngamarra2014.sync_care;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Data.User;
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

        new checkSync().execute();

    }
    class checkSync extends AsyncTask<String, String, String>{

        private String url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/checksync.php";
        JSONParser jsonParser = new JSONParser();
        JSONObject response;

        protected String doInBackground(String... args) {

            jsonParser.setParams(new QueryString("id", Integer.toString(user.getID())));
            response = jsonParser.makeHttpRequest(url, "GET");

            return null;
        }
        protected void onPostExecute(String url){
            super.onPostExecute(url);

            if(response.has("Successful")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setTitle("Sync Care");
                alertDialogBuilder.setMessage("You have pending specialist");

                alertDialogBuilder.setPositiveButton("View", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(), LinkedRequestsActivity.class));
                    }
                });
                alertDialogBuilder.setNegativeButton("View Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }
}
