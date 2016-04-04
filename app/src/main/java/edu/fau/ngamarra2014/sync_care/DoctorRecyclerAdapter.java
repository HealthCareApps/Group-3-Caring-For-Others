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
public class DoctorRecyclerAdapter extends RecyclerView.Adapter<DoctorRecyclerAdapter.ViewHolder> {

    Globals globals = Globals.getInstance();

    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> type = new ArrayList<String>();
    private ArrayList<String> phone = new ArrayList<String>();
    private ArrayList<String> email = new ArrayList<String>();
    private ArrayList<String> address = new ArrayList<String>();
    private ArrayList<String> city = new ArrayList<String>();
    private ArrayList<String> state = new ArrayList<String>();
    private ArrayList<String> zip = new ArrayList<String>();
    private ArrayList<String> fax = new ArrayList<String>();


    private int[] images = { R.drawable.doctor_icon};
    DoctorListActivity Doc;

    private String id;
    private int position;

    public DoctorRecyclerAdapter(DoctorListActivity doc){

        Doc = doc;

        for(int i = 0; i < globals.getPatientDoctors().length(); i++){
            try{
                name.add(globals.getPatientDoctors().getJSONObject(i).getString("name"));
                type.add(globals.getPatientDoctors().getJSONObject(i).getString("type"));
                phone.add(globals.getPatientDoctors().getJSONObject(i).getString("phone"));
                email.add(globals.getPatientDoctors().getJSONObject(i).getString("email"));
                address.add(globals.getPatientDoctors().getJSONObject(i).getString("address"));
                city.add(globals.getPatientDoctors().getJSONObject(i).getString("city"));
                state.add(globals.getPatientDoctors().getJSONObject(i).getString("state"));
                zip.add(globals.getPatientDoctors().getJSONObject(i).getString("zip"));
                fax.add(globals.getPatientDoctors().getJSONObject(i).getString("fax"));
            }catch(JSONException e){

            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_doctor_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemName.setText(name.get(i));
        viewHolder.itemType.setText(type.get(i));
        viewHolder.itemPhone.setText(phone.get(i));
        viewHolder.itemEmail.setText(email.get(i));
        viewHolder.itemAddress.setText(address.get(i));
        viewHolder.itemCity.setText(city.get(i));
        viewHolder.itemState.setText(state.get(i));
        viewHolder.itemZip.setText(zip.get(i));
        viewHolder.itemFax.setText(fax.get(i));
        viewHolder.itemImage.setImageResource(images[0]);
    }
    @Override
    public int getItemCount() {
        return name.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemName;
        public TextView itemType;
        public TextView item;
        public TextView itemPhone;
        public TextView itemEmail;
        public TextView itemAddress;
        public TextView itemCity;
        public TextView itemState;
        public TextView itemZip;
        public TextView itemFax;
        public ImageButton edit, delete;

        public ViewHolder(View itemView) {
            super(itemView);


            itemImage =
                    (ImageView) itemView.findViewById(R.id.item_image);
            itemName =
                    (TextView) itemView.findViewById(R.id.item_name);
            itemType =
                    (TextView) itemView.findViewById(R.id.item_type);
            itemPhone =
                    (TextView) itemView.findViewById(R.id.item_phone);
            itemEmail =
                    (TextView) itemView.findViewById(R.id.item_email);
            itemAddress =
                    (TextView) itemView.findViewById(R.id.item_address);
            itemCity =
                    (TextView) itemView.findViewById(R.id.item_city);
            itemState =
                    (TextView) itemView.findViewById(R.id.item_state);
            itemZip =
                    (TextView) itemView.findViewById(R.id.item_zip);
            itemFax =
                    (TextView) itemView.findViewById(R.id.item_fax);
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
                        globals.setCurrentDoctor(globals.getPatientDoctors().getJSONObject(position));
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
                        id = globals.getPatientDoctors().getJSONObject(position).getString("id");
                        new DeleteDoc().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    class DeleteDoc extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String delete_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/deleteDoc.php";

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", id);
            query.add("database", "Doctors");

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(delete_url, "POST");

            try {
                int success = json.getInt(0);

                if (success == 1) {
                    globals.getPatientDoctors().remove(position);
                    Doc.onFinishCallback();
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
