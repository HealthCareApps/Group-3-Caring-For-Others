package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by nangi_000 on 4/3/2016.
 */
public class PharmacyRecyclerAdapter extends RecyclerView.Adapter<PharmacyRecyclerAdapter.ViewHolder> {

    Globals globals = Globals.getInstance();

    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> phone = new ArrayList<String>();
    private ArrayList<String> address = new ArrayList<String>();
    private ArrayList<String> city = new ArrayList<String>();
    private ArrayList<String> state = new ArrayList<String>();
    private ArrayList<String> zip = new ArrayList<String>();

    private int[] images = { R.drawable.pharmacy_icon};
    PharmacyListActivity Phar;

    private String id;
    private int position;

    public PharmacyRecyclerAdapter(PharmacyListActivity phar){

        Phar = phar;

        for(int i = 0; i < globals.getPatientPrescriptions().length(); i++){
            /*try{
                *//*name.add(globals.getPatientPrescriptions().getJSONObject(i).getString("name"));
                phone.add(globals.getPatientPrescriptions().getJSONObject(i).getString("phone"));
                address.add(globals.getPatientPrescriptions().getJSONObject(i).getString("address"));
                city.add(globals.getPatientPrescriptions().getJSONObject(i).getString("city"));
                state.add(globals.getPatientPrescriptions().getJSONObject(i).getString("state"));
                zip.add(globals.getPatientPrescriptions().getJSONObject(i).getString("zip"));*//*
            }catch(JSONException e){

            }*/
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_pharmacy_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemName.setText(name.get(i));
        viewHolder.itemPhone.setText(phone.get(i));
        viewHolder.itemAddress.setText(address.get(i));
        viewHolder.itemCity.setText(city.get(i));
        viewHolder.itemState.setText(state.get(i));
        viewHolder.itemZip.setText(zip.get(i));
        viewHolder.itemImage.setImageResource(images[0]);
    }
    @Override
    public int getItemCount() {
        return name.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemName;
        public TextView item;
        public TextView itemPhone;
        public TextView itemAddress;
        public TextView itemCity;
        public TextView itemState;
        public TextView itemZip;
        public ImageButton edit, delete;

        public ViewHolder(View itemView) {
            super(itemView);


            itemImage =
                    (ImageView) itemView.findViewById(R.id.item_image);
            itemName =
                    (TextView) itemView.findViewById(R.id.item_name);
            itemPhone =
                    (TextView) itemView.findViewById(R.id.item_phone);
            itemAddress =
                    (TextView) itemView.findViewById(R.id.item_address);
            itemCity =
                    (TextView) itemView.findViewById(R.id.item_city);
            itemState =
                    (TextView) itemView.findViewById(R.id.item_state);
            itemZip =
                    (TextView) itemView.findViewById(R.id.item_zip);
            edit =
                    (ImageButton) itemView.findViewById(R.id.item_edit);
            delete =
                    (ImageButton) itemView.findViewById(R.id.item_delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    Intent i = new Intent();
                    //i.setClass(v.getContext(), RxEditActivity.class);
                    try {
                        globals.setCurrentPrescription(globals.getPatientPrescriptions().getJSONObject(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    v.getContext().startActivity(i);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    try {
                        id = globals.getPatientPrescriptions().getJSONObject(position).getString("id");
                        new DeletePhar().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    class DeletePhar extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String delete_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/deletePrescription.php";

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", id);

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(delete_url, "POST");

            try {
                int success = json.getInt(0);

                if (success == 1) {
                    globals.getPatientPrescriptions().remove(position);
                    Phar.onFinishCallback();
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
