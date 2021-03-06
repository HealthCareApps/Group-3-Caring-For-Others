package edu.fau.ngamarra2014.sync_care.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Patient {
    private int id, caretaker;
    private String first, last, dob, gender;
    private String primaryPhoneNum, emergencyPhoneNum;
    private ArrayList<Doctor> doctors = new ArrayList<>();
    private ArrayList<Prescription> prescriptions = new ArrayList<>();
    private ArrayList<Insurance> insurances = new ArrayList<>();
    private ArrayList<Pharmacy> pharmacies = new ArrayList<>();
    private ArrayList<Exercise> exercises = new ArrayList<>();

    public Doctor doctor;
    public Prescription prescription;
    public Insurance insurance;
    public Pharmacy pharmacy;

    public Patient(){}
    public Patient(JSONObject patient) throws JSONException {
        this.id = patient.getInt("id");
        this.first = patient.getString("first");
        this.last = patient.getString("last");
        this.gender = patient.getString("gender");
        this.dob = patient.getString("birthdate");
        this.caretaker = patient.getInt("caretaker_id");
        this.primaryPhoneNum = patient.getString("phone");
        this.emergencyPhoneNum = patient.getString("emergency");
    }
    public Patient(int id, String first, String last, String gender, String dob, int caretaker){
        this.id = id;
        this.first = first;
        this.last = last;
        this.gender = gender;
        this.dob = dob;
        this.caretaker = caretaker;
    }

    public void setID(int id){
        this.id = id;
    }
    public void setName(String first, String last){
        this.first = first;
        this.last = last;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public void setDOB(String dob){
        this.dob = dob;
    }
    public void setCaretaker(int caretaker){
        this.caretaker = caretaker;
    }
    public void setPrimaryPhoneNumber(String number){
        this.primaryPhoneNum = number;
    }
    public void setEmergencyPhoneNumber(String number){
        this.emergencyPhoneNum = number;
    }

    public int getID(){
        return this.id;
    }
    public String getName(){
        return this.first + " " + this.last;
    }
    public String getFirst(){
        return this.first;
    }
    public String getLast(){
        return this.last;
    }
    public String getGender(){
        return this.gender;
    }
    public String getDOB(){
        return this.dob;
    }
    public String getPrimaryPhoneNumber(){
        return this.primaryPhoneNum;
    }
    public String getEmergencyPhoneNumber(){
        return this.emergencyPhoneNum;
    }

    public int getCaretaker(){
        return this.caretaker;
    }

    public void addDoctor(Doctor doctor){
        this.doctors.add(doctor);
    }
    public Doctor addDoctor(JSONObject doc) throws JSONException {
        Doctor doctor = new Doctor(doc);
        this.doctors.add(doctor);
        return doctor;
    }
    public void setDoctors(ArrayList<Doctor> doctors){
        this.doctors = doctors;
    }
    public Doctor getDoctor(int index){
        return this.doctors.get(index);
    }
    public void removeDoctor(int index) {this.doctors.remove(index);}
    public int getNumberOfDoctors(){
        return this.doctors.size();
    }
    public void setCurrentDoctor(int index){
        this.doctor = this.doctors.get(index);
    }

    public void addPrescription(Prescription rx){
        this.prescriptions.add(rx);
    }
    public Prescription addPrescription(JSONObject rx) throws JSONException {
        Prescription script = new Prescription(rx);
        this.prescriptions.add(script);
        return script;
    }
    public void setPrescriptions(ArrayList<Prescription> prescriptions){
        this.prescriptions = prescriptions;
    }
    public Prescription getPrescription(int index){
        return this.prescriptions.get(index);
    }
    public void removePrescription(int index) {this.prescriptions.remove(index);}
    public int getNumberOfPrescriptions(){
        return this.prescriptions.size();
    }
    public void setCurrentPrescription(int index){
        this.prescription = this.prescriptions.get(index);
    }

    public void addInsurance(Insurance insurance){
        this.insurances.add(insurance);
    }
    public Insurance addInsurance(JSONObject insurance) throws JSONException{
        Insurance insure = new Insurance(insurance);
        this.insurances.add(insure);
        return insure;
    }
    public void setInsurances(ArrayList<Insurance> insurances){
        this.insurances = insurances;
    }
    public Insurance getInsurance(int index){
        return this.insurances.get(index);
    }
    public void removeInsurance(int index) {this.insurances.remove(index);}
    public int getNumberOfInsurances(){
        return this.insurances.size();
    }
    public void setCurrentInsurance(int index){
        this.insurance = this.insurances.get(index);
    }

    public void addPharmacy(Pharmacy pharmacy){
        this.pharmacies.add(pharmacy);
    }
    public Pharmacy addPharmacy(JSONObject pharmacy) throws JSONException {
        Pharmacy pharm = new Pharmacy(pharmacy);
        this.pharmacies.add(pharm);
        return pharm;
    }
    public void setPharmacies(ArrayList<Pharmacy> pharmacies){
        this.pharmacies = pharmacies;
    }
    public Pharmacy getPharmacy(int index){
        return this.pharmacies.get(index);
    }
    public void removePharmacy(int index) {this.pharmacies.remove(index);}
    public int getNumberOfPharmacies(){
        return this.pharmacies.size();
    }
    public void setCurrentPharmacy(int index){
        this.pharmacy = this.pharmacies.get(index);
    }

    public void addExercise(Exercise exercise){
        this.exercises.add(exercise);
    }
    public Exercise addExercise(JSONObject exercise) throws JSONException {
        Exercise ex = new Exercise(exercise);
        this.exercises.add(ex);
        return ex;
    }
    public void setExercises(ArrayList<Exercise> exercises){
        this.exercises = exercises;
    }
    public Exercise getExercise(int index){
        return this.exercises.get(index);
    }
    public int getNumberOfExercises(){
        return this.exercises.size();
    }
}
