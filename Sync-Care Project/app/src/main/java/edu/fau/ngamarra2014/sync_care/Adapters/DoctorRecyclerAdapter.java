package edu.fau.ngamarra2014.sync_care.Adapters;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.DoctorEditActivity;
import edu.fau.ngamarra2014.sync_care.DoctorListActivity;
import edu.fau.ngamarra2014.sync_care.R;

public class DoctorRecyclerAdapter extends RecyclerView.Adapter<DoctorRecyclerAdapter.ViewHolder> {

    User user = User.getInstance();

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

    private int id;

    public DoctorRecyclerAdapter(DoctorListActivity doc){

        Doc = doc;

        for(int i = 0; i < user.patient.getNumberOfDoctors(); i++){
            name.add(user.patient.getDoctor(i).getName());
            type.add(user.patient.getDoctor(i).getType());
            phone.add(user.patient.getDoctor(i).getContactInfo()[0]);
            email.add(user.patient.getDoctor(i).getContactInfo()[2]);
            address.add(user.patient.getDoctor(i).getAddress()[0]);
            city.add(user.patient.getDoctor(i).getAddress()[1]);
            state.add(user.patient.getDoctor(i).getAddress()[2]);
            zip.add(user.patient.getDoctor(i).getAddress()[3]);
            fax.add(user.patient.getDoctor(i).getContactInfo()[1]);
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

            if(user.getAccountType().equals("Specialist")){
                edit.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
            }

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.patient.setCurrentDoctor(getAdapterPosition());
                    v.getContext().startActivity(new Intent(v.getContext(), DoctorEditActivity.class));
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = user.patient.getDoctor(getAdapterPosition()).getID();
                    new DeleteDoc(getAdapterPosition()).execute();
                }
            });
        }
    }
    class DeleteDoc extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        private String delete_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/deleteDoc.php";
        DBHandler dbHandler = new DBHandler(Doc, null, null, 2);
        int index;

        public DeleteDoc(int index){
            this.index = index;
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", Integer.toString(id));
            query.add("database", "Doctors");

            jsonParser.setParams(query);
            JSONObject response = jsonParser.makeHttpRequest(delete_url, "POST");

            try {
                if (response.has("Successful")) {
                    dbHandler.deleteDoc("doctors", id);
                    user.patient.removeDoctor(index);
                    Doc.onFinishCallback();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}
