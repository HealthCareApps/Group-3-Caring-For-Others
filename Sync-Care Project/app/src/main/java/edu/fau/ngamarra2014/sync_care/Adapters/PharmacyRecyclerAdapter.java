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
import edu.fau.ngamarra2014.sync_care.Add.Edit.PharmacyEditActivity;
import edu.fau.ngamarra2014.sync_care.PharmacyListActivity;
import edu.fau.ngamarra2014.sync_care.R;

public class PharmacyRecyclerAdapter extends RecyclerView.Adapter<PharmacyRecyclerAdapter.ViewHolder> {

    User user = User.getInstance();

    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> phone = new ArrayList<String>();
    private ArrayList<String> address = new ArrayList<String>();
    private ArrayList<String> city = new ArrayList<String>();
    private ArrayList<String> state = new ArrayList<String>();
    private ArrayList<String> zip = new ArrayList<String>();

    private int[] images = { R.drawable.pharmacy_icon};
    PharmacyListActivity Phar;

    private int id;

    public PharmacyRecyclerAdapter(PharmacyListActivity phar){

        Phar = phar;

        for(int i = 0; i < user.patient.getNumberOfPharmacies(); i++){
            name.add(user.patient.getPharmacy(i).getName());
            phone.add(user.patient.getPharmacy(i).getPhone());
            address.add(user.patient.getPharmacy(i).getAddress()[0]);
            city.add(user.patient.getPharmacy(i).getAddress()[1]);
            state.add(user.patient.getPharmacy(i).getAddress()[2]);
            zip.add(user.patient.getPharmacy(i).getAddress()[3]);
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

            if(user.getAccountType().equals("Specialist")){
                edit.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
            }

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.patient.setCurrentPharmacy(getAdapterPosition());
                    v.getContext().startActivity(new Intent(v.getContext(), PharmacyEditActivity.class));
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = user.patient.getPharmacy(getAdapterPosition()).getID();
                    new DeletePhar(getAdapterPosition()).execute();
                }
            });
        }
    }
    class DeletePhar extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        private String delete_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/deleteDoc.php";
        DBHandler dbHandler = new DBHandler(Phar, user.getUsername(), null, 2);
        int index;

        public DeletePhar(int index){
            this.index = index;
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", Integer.toString(id));
            query.add("database", "Pharmacies");

            jsonParser.setParams(query);

            try {
                JSONObject response = jsonParser.makeHttpRequest(delete_url, "POST");
                if (response.has("Successful")) {
                    dbHandler.deleteDoc("pharmacies", id);
                    user.patient.removePharmacy(index);
                    Phar.onFinishCallback();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
