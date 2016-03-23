package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RxListFragment extends ListFragment implements OnItemClickListener{

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String rx_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/rx.php";

    RxListListener activityCallback;
    ArrayList<String> itemsList = new ArrayList<String>();

    RxListFragment frag;
    JSONArray array;

    public interface RxListListener {
        public void onRxClick(JSONObject obj) throws JSONException;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityCallback = (RxListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement RxListListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            activityCallback = (RxListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement RxListListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rx_list, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        frag = this;
        new Prescriptions().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            JSONObject rxInfo = array.getJSONObject(position);
            activityCallback.onRxClick(rxInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class Prescriptions extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Prescriptions..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", "1007");

            jsonParser.setParams(query);
            JSONObject json = jsonParser.makeHttpRequest(rx_url, "GET");

            // check for success tag
            try {
                array = json.getJSONArray("Rx");
                if(json.length() > 0){
                    for(int i = 0; i < array.length(); i++){
                        itemsList.add(array.getJSONObject(i).getString("name"));
                    }
                } else {
                    // failed to create product
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            ArrayAdapter<String> myArray = new ArrayAdapter<String> (getActivity(), android.R.layout.simple_list_item_1, itemsList);
            setListAdapter(myArray);
            getListView().setOnItemClickListener(frag);
            pDialog.dismiss();

        }

    }
}
