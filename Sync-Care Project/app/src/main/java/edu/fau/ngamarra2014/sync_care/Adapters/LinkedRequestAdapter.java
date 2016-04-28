package edu.fau.ngamarra2014.sync_care.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.LinkedRequestsActivity;
import edu.fau.ngamarra2014.sync_care.R;

public class LinkedRequestAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<JSONObject> patient = new ArrayList<>();
    private ArrayList<JSONObject> specialist = new ArrayList<>();
    private Context context;

    public LinkedRequestAdapter(ArrayList<JSONObject> patient, ArrayList<JSONObject> specialist, Context context) {
        this.patient = patient;
        this.specialist = specialist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return patient.size();
    }

    @Override
    public Object getItem(int pos) {
        return patient.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linked_requests_activity, null);
        }

        try{
            //Handle TextView and display string from your list
            TextView patientName = (TextView)view.findViewById(R.id.patientname);
            TextView specialistName = (TextView)view.findViewById(R.id.specialistname);
            patientName.setText(patient.get(position).getString("name"));
            specialistName.setText(specialist.get(position).getString("first") + " " + specialist.get(position).getString("last"));

            //Handle buttons and add onClickListeners
            ImageButton acceptBtn = (ImageButton)view.findViewById(R.id.accept);
            ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.cancel);

            specialistName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parent.getContext());
                        alertDialogBuilder.setTitle("Sync Care");
                        String message = "Name: " + specialist.get(position).getString("first");
                        message += "\nEmail: " + specialist.get(position).getString("email");
                        alertDialogBuilder.setMessage(message);
                        alertDialogBuilder.show();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            });

            acceptBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    try {
                        new deletePending().execute(patient.get(position).getString("id"), patient.get(position).getString("patient_id"), specialist.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    patient.remove(position); //or some other task
                    notifyDataSetChanged();
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    //list.remove(position); //or some other task
                    notifyDataSetChanged();
                }
            });
        }catch(JSONException e){
            e.printStackTrace();
        }


        return view;
    }
    class deletePending extends AsyncTask<String, String, String> {

        private String delete_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/deleteDoc.php";
        private String update_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/updateDoc.php";
        JSONParser jsonParser = new JSONParser();
        JSONObject response;

        @Override
        protected String doInBackground(String... params) {
            QueryString query = new QueryString("id", params[0]);
            query.add("database", "Sync");
            jsonParser.setParams(query);
            response = jsonParser.makeHttpRequest(delete_url, "POST");

            query = new QueryString("id", params[2]);
            query.add("database", "specialist");
            query.add("patient", params[1]);
            jsonParser.setParams(query);
            response = jsonParser.makeHttpRequest(update_url, "POST");

            return null;
        }
    }
}