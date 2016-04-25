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
import org.json.JSONObject;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;

public class InsuranceRecyclerAdapter extends RecyclerView.Adapter<InsuranceRecyclerAdapter.ViewHolder> {

    User user = User.getInstance();

    private ArrayList<String> provider = new ArrayList<String>();
    private ArrayList<String> memberid = new ArrayList<String>();
    private ArrayList<String> groupnumber = new ArrayList<String>();
    private ArrayList<String> rxbin = new ArrayList<String>();
    private ArrayList<String> rxpcn = new ArrayList<String>();
    private ArrayList<String> rxgrp = new ArrayList<String>();

    private int[] images = {R.drawable.insurance_icon};
    InsuranceListActivity Ins;

    private int id;

    public InsuranceRecyclerAdapter(InsuranceListActivity ins){

        Ins = ins;

        for(int i = 0; i < user.patient.getNumberOfInsurances(); i++){
            provider.add(user.patient.getInsurance(i).getProvider());
            memberid.add(user.patient.getInsurance(i).getMID());
            groupnumber.add(user.patient.getInsurance(i).getGroupNumber());
            rxbin.add(user.patient.getInsurance(i).getRxBin());
            rxpcn.add(user.patient.getInsurance(i).getRxPcn());
            rxgrp.add(user.patient.getInsurance(i).getRxGroup());
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

            if(user.getAccountType().equals("Medical Specialist")){
                edit.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
            }

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.patient.setCurrentInsurance(getAdapterPosition());
                    v.getContext().startActivity(new Intent(v.getContext(), InsuranceEditActivity.class));
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = user.patient.getInsurance(getAdapterPosition()).getID();
                    new DeleteIns(getAdapterPosition()).execute();
                }
            });
        }
    }
    class DeleteIns extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        private String delete_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/deleteDoc.php";
        DBHandler dbHandler = new DBHandler(Ins, null, null, 2);
        int index;

        public DeleteIns(int index){
            this.index = index;
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", Integer.toString(id));
            query.add("database", "Insurances");

            jsonParser.setParams(query);
            JSONObject response = jsonParser.makeHttpRequest(delete_url, "POST");

            try {
                if (response.has("Successful")) {
                    dbHandler.deleteDoc("insurances", id);
                    user.patient.removeInsurance(index);
                    Ins.onFinishCallback();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

