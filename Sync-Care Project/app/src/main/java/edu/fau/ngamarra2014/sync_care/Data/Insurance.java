package edu.fau.ngamarra2014.sync_care.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class Insurance {
    private int id, patient;
    private String mid, groupnum, rxbin, rxpcn;
    private String provider, rxgrp;

    public Insurance(){}
    public Insurance(JSONObject insurance) throws JSONException {
        this.id = insurance.getInt("id");
        this.patient = insurance.getInt("patient_id");
        this.mid = insurance.getString("mid");
        this.groupnum = insurance.getString("groupnum");
        this.rxbin = insurance.getString("rxbin");
        this.rxpcn = insurance.getString("rxpcn");
        this.provider = insurance.getString("provider");
        this.rxgrp = insurance.getString("rxgrp");
    }

    public void setID(int id){
        this.id = id;
    }
    public void setPatient(int id){
        this.patient = id;
    }
    public void setMID(String mid){
        this.mid = mid;
    }
    public void setGroupNumber(String grpnum){
        this.groupnum = grpnum;
    }
    public void setRxBin(String rxbin){
        this.rxbin = rxbin;
    }
    public void setRxPcn(String rxpcn){
        this.rxpcn = rxpcn;
    }
    public void setRxGroup(String group){
        this.rxgrp = group;
    }
    public void setProvider(String provider){
        this.provider = provider;
    }
    public int getID(){
        return this.id;
    }
    public int getPatient(){
        return this.patient;
    }
    public String getMID(){
        return this.mid;
    }
    public String getGroupNumber(){
        return this.groupnum;
    }
    public String getRxBin(){
        return this.rxbin;
    }
    public String getRxPcn(){
        return this.rxpcn;
    }
    public String getRxGroup(){
        return this.rxgrp;
    }
    public String getProvider(){ return this.provider; }

    public void update(Insurance insurance){
        this.mid = insurance.getMID();
        this.groupnum = insurance.getGroupNumber();
        this.rxbin = insurance.getRxBin();
        this.rxpcn = insurance.getRxPcn();
        this.provider = insurance.getProvider();
        this.rxgrp = insurance.getRxGroup();
    }
}

