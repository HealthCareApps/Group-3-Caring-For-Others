package edu.fau.ngamarra2014.sync_care.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.R;

public class LinkedRequestAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> patient = new ArrayList<String>();
    private ArrayList<String> specialist = new ArrayList<String>();
    private Context context;

    public LinkedRequestAdapter(ArrayList<String> patient, ArrayList<String> specialist, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linked_requests_activity, null);
        }

        //Handle TextView and display string from your list
        TextView patientName = (TextView)view.findViewById(R.id.patientname);
        TextView specialistName = (TextView)view.findViewById(R.id.specialistname);
        patientName.setText(patient.get(position));
        specialistName.setText(specialist.get(position));

        //Handle buttons and add onClickListeners
        ImageButton acceptBtn = (ImageButton)view.findViewById(R.id.accept);
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.cancel);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
               //list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        return view;
    }
}