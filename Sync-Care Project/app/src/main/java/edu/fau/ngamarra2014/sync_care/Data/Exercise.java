package edu.fau.ngamarra2014.sync_care.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class Exercise {

    int id, patient, specialist;
    String name, start, duration, calories, comments, date;

    public Exercise(JSONObject exercise) throws JSONException {
        this.id = exercise.getInt("id");
        this.patient = exercise.getInt("patient");
        this.specialist = exercise.getInt("specialist");
        this.name = exercise.getString("name");
        this.start = exercise.getString("start");
        this.duration = exercise.getString("duration");
        this.calories = exercise.getString("calories");
        this.comments = exercise.getString("comments");
        this.date = exercise.getString("date");
    }
    public Exercise(){}

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
