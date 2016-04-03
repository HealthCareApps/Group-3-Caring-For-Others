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
public class InsuranceRecyclerAdapter extends RecyclerView.Adapter<InsuranceRecyclerAdapter.ViewHolder> {

    Globals globals = Globals.getInstance();

    private ArrayList<String> provider = new ArrayList<String>();
    private ArrayList<String> memberid = new ArrayList<String>();
    private ArrayList<String> groupnumber = new ArrayList<String>();
    private ArrayList<String> rxbin = new ArrayList<String>();
    private ArrayList<String> rxpcn = new ArrayList<String>();
    private ArrayList<String> rxgrp = new ArrayList<String>();

    private int[] images = {R.drawable.insurance_icon};
    InsuranceListActivity Ins;

    private String id;
    private int position;

    public InsuranceRecyclerAdapter(InsuranceListActivity ins){

        Ins = ins;

        for(int i = 0; i < globals.getPatientPrescriptions().length(); i++){
            /*try{
                provider.add(globals.getPatientPrescriptions().getJSONObject(i).getString("provider"));
                memberid.add(globals.getPatientPrescriptions().getJSONObject(i).getString("mid"));
                groupnumber.add(globals.getPatientPrescriptions().getJSONObject(i).getString("groupnum"));
                rxbin.add(globals.getPatientPrescriptions().getJSONObject(i).getString("rxbin"));
                rxpcn.add(globals.getPatientPrescriptions().getJSONObject(i).getString("rxpcn"));
                rxgrp.add(globals.getPatientPrescriptions().getJSONObject(i).getString("rxgrp"));
            }catch(JSONException e){

            }*/
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_insurance_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemProvider.setText(provider.get(i));
        viewHolder.itemMemberID.setText(memberid.get(i));
        viewHolder.itemGroupNumber.setText(groupnumber.get(i));
        viewHolder.itemRxBin.setText(rxbin.get(i));
        viewHolder.itemRxPCN.setText(rxpcn.get(i));
        viewHolder.itemRxGrp.setText(rxgrp.get(i));
        viewHolder.itemImage.setImageResource(images[0]);
    }
    @Override
    public int getItemCount() {
        return provider.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemProvider;
        public TextView item;
        public TextView itemMemberID;
        public TextView itemGroupNumber;
        public TextView itemRxBin;
        public TextView itemRxPCN;
        public TextView itemRxGrp;
        public ImageButton edit, delete;

        public ViewHolder(View itemView) {
            super(itemView);


            itemImage =
                    (ImageView) itemView.findViewById(R.id.item_image);
            itemProvider =
                    (TextView) itemView.findViewById(R.id.item_provider);
            itemMemberID =
                    (TextView) itemView.findViewById(R.id.item_memberid);
            itemGroupNumber =
                    (TextView) itemView.findViewById(R.id.item_groupnumber);
            itemRxBin =
                    (TextView) itemView.findViewById(R.id.item_rxbin);
            itemRxPCN =
                    (TextView) itemView.findViewById(R.id.item_rxpcn);
            itemRxGrp =
                    (TextView) itemView.findViewById(R.id.item_rxgrp);
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
                        new DeleteIns().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    class DeleteIns extends AsyncTask<String, String, String> {

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
                    Ins.onFinishCallback();
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

