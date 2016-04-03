package edu.fau.ngamarra2014.sync_care;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Globals {

    private static Globals instance;

    // Global variable
    private JSONObject user; //Person logged in
    private JSONArray patients, prescriptions;
    private JSONObject currentPatient, currentPrescription;


    // Restrict the constructor from being instantiated
    private Globals(){}

    //Logged user information
    public void setUser(JSONObject o) throws JSONException {
        this.user = o;
        this.user.put("name", this.user.getString("first") + " " + this.user.getString("last"));
    }
    public JSONObject getuser(){
        return this.user;
    }

    //Patients
    public void setPatients(JSONArray a){ this.patients = a;}
    public JSONArray getPatients(){ return this.patients;
    }
    public void addPatient(JSONObject patient) throws JSONException { this.patients.put(this.patients.length(), patient);}

    //Current patient being viewed
    public void setCurrentPatient(JSONObject o){ this.currentPatient = o; }
    public JSONObject getCurrentPatient(){
        return this.currentPatient;
    }

    //List of patient prescriptions
    public void setPatientPrescriptions(JSONArray a){
        this.prescriptions = a;
    }
    public JSONArray getPatientPrescriptions(){
        return this.prescriptions;
    }

    //Current prescription being worked on
    public void setCurrentPrescription(JSONObject o){
        this.currentPrescription = o;
    }
    public JSONObject getCurrentPrescription(){
        return this.currentPrescription;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

}
