package edu.fau.ngamarra2014.sync_care.Data;

public class Pharmacy {
    private int id, patient;
    private String name, phone;
    private String address, city, state, zip;

    public void setID(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPatient(int id){
        this.patient = id;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setAddress(String address, String city, String state, String zip){
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
    public int getID(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public int getPatient(){
        return this.patient;
    }
    public String getPhone(){
        return this.phone;
    }
    public String[] getAddress(){
        return new String[] {this.address, this.city, this.state, this.zip};
    }

    public void update(Pharmacy pharmacy){
        this.name = pharmacy.getName();
        this.phone = pharmacy.getPhone();
        this.address = pharmacy.getAddress()[0];
        this.city = pharmacy.getAddress()[1];
        this.state = pharmacy.getAddress()[2];
        this.zip = pharmacy.getAddress()[3];
    }
}

