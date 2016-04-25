package edu.fau.ngamarra2014.sync_care.Data;


import org.json.JSONException;
import org.json.JSONObject;

public class Prescription {
    private int id, patient;
    private String name, dosage, symptoms, instructions, doctor;

    public Prescription(){}
    public Prescription(JSONObject rx) throws JSONException {
        this.id = rx.getInt("id");
        this.patient = rx.getInt("patient_id");
        this.name = rx.getString("name");
        this.dosage = rx.getString("dosage");
        this.symptoms = rx.getString("symptoms");
        this.instructions = rx.getString("instructions");
        this.doctor = rx.getString("doctor");
    }
    public Prescription(int id, String name, String dosage,String symptoms, String instructions, String doctor, int patient){
        this.id = id;
        this.patient = patient;
        this.name = name;
        this.dosage = dosage;
        this.symptoms = symptoms;
        this.instructions = instructions;
        this.doctor = doctor;
    }

    public void setID(int id){
        this.id = id;
    }
    public void setPatient(int id){
        this.patient = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDosage(String dosage){
        this.dosage = dosage;
    }
    public void setSymptoms(String symptoms){
        this.symptoms = symptoms;
    }
    public void setInstructions(String instructions){
        this.instructions = instructions;
    }
    public void setDoctorName(String name){
        this.doctor = name;
    }
    public int getID(){
        return this.id;
    }
    public int getPatient(){
        return this.patient;
    }
    public String getName(){
        return this.name;
    }
    public String getDosage(){
        return this.dosage;
    }
    public String getSymptoms(){
        return this.symptoms;
    }
    public String getInstructions(){
        return this.instructions;
    }
    public String getDoctorName(){
        return this.doctor;
    }

    public void update(Prescription rx){
        this.name = rx.getName();
        this.doctor = rx.getDoctorName();
        this.dosage = rx.getDosage();
        this.symptoms = rx.getSymptoms();
        this.instructions = rx.getInstructions();
    }
}
