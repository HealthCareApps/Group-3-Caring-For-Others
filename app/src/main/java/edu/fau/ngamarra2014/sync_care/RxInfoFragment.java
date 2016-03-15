package edu.fau.ngamarra2014.sync_care;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RxInfoFragment extends Fragment {

    private static EditText name;
    private static EditText dosage;
    private static EditText symptoms;
    private static EditText doc;
    private static EditText instructions;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.rx_info,
                container, false);

        name = (EditText) view.findViewById(R.id.name);
        dosage = (EditText) view.findViewById(R.id.dosage);
        symptoms = (EditText) view.findViewById(R.id.symptom);
        doc = (EditText) view.findViewById(R.id.doc);
        instructions = (EditText) view.findViewById(R.id.instructions);

        return view;
    }

    public void changeTextProperties(JSONObject obj) throws JSONException {
        name.setText(obj.getString("name"));
        dosage.setText(obj.getString("dosage"));
        symptoms.setText(obj.getString("symptoms"));
        doc.setText(obj.getString("doctor"));
        instructions.setText(obj.getString("instructions"));
    }

}
