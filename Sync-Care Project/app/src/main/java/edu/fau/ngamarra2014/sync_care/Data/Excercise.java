package edu.fau.ngamarra2014.sync_care.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class Excercise {

    int id, patient, specialist;
    String name, start, duration, calories, comments, date;

    public Excercise(JSONObject excercise) throws JSONException {
        this.id = excercise.getInt("id");
        this.patient = excercise.getInt("patient");
        this.specialist = excercise.getInt("specialist");
        this.name = excercise.getString("name");
        this.start = excercise.getString("start");
        this.duration = excercise.getString("duration");
        this.calories = excercise.getString("calories");
        this.comments = excercise.getString("comments");
        this.date = excercise.getString("date");
    }
    public Excercise(){}

    public void setID(int id){
        this.id = id;
    }
    public void setPatient(int id){
        this.patient = id;
    }
    public void setSpecialist(int id){
        this.specialist = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setStart(String start){
        this.start = start;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    public void setCalories(String calories){
        this.calories = calories;
    }
    public void setComments(String comments){
        this.comments = comments;
    }
    public void setDate(String date){
        this.date = date;
    }

    public int getId(){
        return id;
    }
    public int getPatient(){
        return patient;
    }
    public int getSpecialist(){
        return specialist;
    }
    public String getName(){
        return name;
    }
    public String getStart(){
        return start;
    }
    public String getDuration(){
        return duration;
    }
    public String getCalories(){
        return calories;
    }
    public String getComments(){
        return comments;
    }
    public String getDate(){
        return date;
    }
}
