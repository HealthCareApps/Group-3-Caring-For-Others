package edu.fau.ngamarra2014.sync_care.Data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {

    private static User instance;
    private int id;
    private String first, last, accountType;
    private String username, email, password;
    private ArrayList<Patient> Patients = new ArrayList<>();
    public Patient patient;

    private User(){}

    public void setUser(JSONObject user) throws JSONException {
        this.id = user.getInt("id");
        this.first = user.getString("first");
        this.last = user.getString("last");
        this.email = user.getString("email");
        this.username = user.getString("username");
        this.password = user.getString("password");
        this.accountType = user.getString("account");
    }
    public void setID(int id){
        this.id = id;
    }
    public void setFirst(String first){
        this.first = first;
    }
    public void setLast(String last){
        this.last = last;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setAccountType(String type){
        this.accountType = type;
    }
    public void setPassword(String password){
        this.password = password;
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
    public String getUsername(){
        return this.username;
    }
    public String getEmail(){
        return this.email;
    }
    public String getAccountType(){
        return this.accountType;
    }
    public String getPassword(){
        return this.password;
    }

    public void addPatient(Patient p){
        this.Patients.add(p);
    }
    public Patient addPatient(JSONObject patient) throws JSONException {
        Patient p = new Patient(patient);
        this.Patients.add(p);
        return p;
    }
    public Patient getPatient(int index){
        return this.Patients.get(index);
    }
    public int getNumberOfPatients(){
        return this.Patients.size();
    }
    public void setCurrentPatient(int index){
        this.patient = this.Patients.get(index);
    }

    public static synchronized User getInstance(){
        if(instance==null){
            instance=new User();
        }
        return instance;
    }
}
