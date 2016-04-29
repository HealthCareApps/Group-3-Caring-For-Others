package edu.fau.ngamarra2014.sync_care.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Authentication.MCrypt;
import edu.fau.ngamarra2014.sync_care.Data.Doctor;
import edu.fau.ngamarra2014.sync_care.Data.Excercise;
import edu.fau.ngamarra2014.sync_care.Data.Insurance;
import edu.fau.ngamarra2014.sync_care.Data.Patient;
import edu.fau.ngamarra2014.sync_care.Data.Pharmacy;
import edu.fau.ngamarra2014.sync_care.Data.Prescription;
import edu.fau.ngamarra2014.sync_care.Data.User;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "sync_care.db";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PATIENTS = "patients";
    public static final String TABLE_SPECIALIST_PATIENTS = "specialist_patients";
    public static final String TABLE_DOCTORS = "doctors";
    public static final String TABLE_INSURANCES = "insurances";
    public static final String TABLE_PHARMACIES = "pharmacies";
    public static final String TABLE_PRESCRIPTIONS = "prescriptions";
    public static final String TABLE_EXCERCISES = "excercises";

    User user = User.getInstance();

    public static final String COLUMN_ID = "_id";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + "username TEXT UNIQUE,"
                + "password TEXT,"
                + "first TEXT,"
                + "last TEXT,"
                + "email TEXT,"
                + "account_type TEXT" + ")";
        String CREATE_USERS_PATIENTS = "CREATE TABLE " + TABLE_PATIENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + "first TEXT,"
                + "last TEXT,"
                + "birthdate TEXT,"
                + "phone TEXT,"
                + "emergency TEXT,"
                + "gender TEXT,"
                + "caretaker_id INTEGER" + ")";
        String CREATE_SPECIALIST_PATIENTS = "CREATE TABLE " + TABLE_SPECIALIST_PATIENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + "patient INTEGER,"
                + "first TEXT,"
                + "last TEXT,"
                + "birthdate TEXT,"
                + "phone TEXT,"
                + "emergency TEXT,"
                + "gender TEXT,"
                + "specialist_id INTEGER" + ")";
        String CREATE_USERS_DOCTORS = "CREATE TABLE " + TABLE_DOCTORS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + "name TEXT,"
                + "type TEXT,"
                + "phone TEXT,"
                + "email TEXT,"
                + "address TEXT,"
                + "city TEXT,"
                + "state TEXT,"
                + "zip TEXT,"
                + "fax TEXT,"
                + "patient_id INTEGER" + ")";
        String CREATE_USERS_INSURANCES = "CREATE TABLE " + TABLE_INSURANCES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + "provider TEXT,"
                + "mid TEXT,"
                + "groupnum TEXT,"
                + "rxbin TEXT,"
                + "rxpcn TEXT,"
                + "rxgrp TEXT,"
                + "patient_id INTEGER" + ")";
        String CREATE_USERS_PHARMACIES = "CREATE TABLE " + TABLE_PHARMACIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + "name TEXT,"
                + "address TEXT,"
                + "city TEXT,"
                + "state TEXT,"
                + "zip INTEGER,"
                + "phone TEXT,"
                + "patient_id INTEGER" + ")";
        String CREATE_USERS_PRESCRIPTIONS = "CREATE TABLE " + TABLE_PRESCRIPTIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + "name TEXT,"
                + "dosage TEXT,"
                + "symptoms TEXT,"
                + "doctor TEXT,"
                + "instructions TEXT,"
                + "patient_id INTEGER" + ")";
        String CREATE_USERS_EXCERCISES = "CREATE TABLE " + TABLE_EXCERCISES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + "name TEXT,"
                + "start TEXT,"
                + "duration TEXT,"
                + "calories TEXT,"
                + "comments TEXT,"
                + "patient_id INTEGER,"
                + "specialist_id INTEGER" + ")";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_USERS_PATIENTS);
        db.execSQL(CREATE_USERS_DOCTORS);
        db.execSQL(CREATE_USERS_INSURANCES);
        db.execSQL(CREATE_USERS_PHARMACIES);
        db.execSQL(CREATE_USERS_PRESCRIPTIONS);
        db.execSQL(CREATE_SPECIALIST_PATIENTS);
        db.execSQL(CREATE_USERS_EXCERCISES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSURANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHARMACIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESCRIPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIALIST_PATIENTS );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXCERCISES);
        onCreate(db);
    }

    public void addUser(User user){

        ContentValues values = new ContentValues();

        values.put("password", user.getPassword());
        values.put("_id", user.getID());
        values.put("username", user.getUsername());
        values.put("first", user.getFirst());
        values.put("last", user.getLast());
        values.put("email", user.getEmail());
        values.put("account_type", user.getAccountType());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public int AuthenticateUser(String username, String password){
        String query = "Select * FROM " + TABLE_USERS + " WHERE " + "username" + " =  \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MCrypt mcrypt = new MCrypt();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            String decrypted;

            try {
                decrypted = new String(mcrypt.decrypt(cursor.getString(2))).trim();
            } catch (Exception e) {
                decrypted = "";
            }
            if(decrypted.equals(password)){
                user.setID(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setFirst(cursor.getString(3));
                user.setLast(cursor.getString(4));
                user.setEmail(cursor.getString(5));
                user.setAccountType(cursor.getString(6));
                db.close();
                cursor.close();
                return 1;
            }else{
                cursor.close();
                return 2;
            }
        }
        db.close();
        return 0;
    }

    public String lastPatientAdded(int id){
        String query = "SELECT * FROM " + TABLE_SPECIALIST_PATIENTS + " WHERE specialist_id = \"" + id + "\" ORDER BY _id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        return "0";
    }

    public void addSpecialistPatient(int id, Patient patient){
        ContentValues values = new ContentValues();
        values.put("_id", id);
        values.put("patient", patient.getID());
        values.put("first", patient.getFirst());
        values.put("last", patient.getLast());
        values.put("birthdate", patient.getDOB());
        values.put("phone", patient.getPrimaryPhoneNumber());
        values.put("emergency", patient.getEmergencyPhoneNumber());
        values.put("gender", patient.getGender());
        values.put("specialist_id", patient.getCaretaker());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SPECIALIST_PATIENTS, null, values);
        db.close();
    }

    public void addPatient(Patient patient){
        ContentValues values = new ContentValues();
        values.put("_id", patient.getID());
        values.put("first", patient.getFirst());
        values.put("last", patient.getLast());
        values.put("birthdate", patient.getDOB());
        values.put("phone", patient.getPrimaryPhoneNumber());
        values.put("emergency", patient.getEmergencyPhoneNumber());
        values.put("gender", patient.getGender());
        values.put("caretaker_id", patient.getCaretaker());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PATIENTS, null, values);
        db.close();
    }

    public void loadPatients(int caretaker) {
        String query = "Select * FROM " + TABLE_PATIENTS + " WHERE " + "caretaker_id" + " =  \"" + caretaker + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Patient patient;

        while(cursor.moveToNext()) {
            patient = new Patient();
            patient.setID(cursor.getInt(0));
            patient.setName(cursor.getString(1), cursor.getString(2));
            patient.setDOB(cursor.getString(3));
            patient.setPrimaryPhoneNumber(cursor.getString(4));
            patient.setEmergencyPhoneNumber(cursor.getString(5));
            patient.setGender(cursor.getString(6));
            patient.setCaretaker(cursor.getInt(7));

            user.addPatient(patient);
        }
        cursor.close();
        db.close();
    }

    public void loadSpecialistPatients(int id) {
        String query = "Select * FROM " + TABLE_SPECIALIST_PATIENTS + " WHERE " + "specialist_id" + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Patient patient;

        while(cursor.moveToNext()) {
            patient = new Patient();
            Log.i("ADDING", "loadSpecialistPatients: ");
            patient.setID(cursor.getInt(1));
            patient.setName(cursor.getString(2), cursor.getString(3));
            patient.setDOB(cursor.getString(4));
            patient.setPrimaryPhoneNumber(cursor.getString(5));
            patient.setEmergencyPhoneNumber(cursor.getString(6));
            patient.setGender(cursor.getString(7));
            patient.setCaretaker(cursor.getInt(8));

            user.addPatient(patient);
        }
        cursor.close();
        db.close();
    }

    public void addDoctor(Doctor doc){
        ContentValues values = new ContentValues();
        values.put("_id", doc.getID());
        values.put("name", doc.getName());
        values.put("type", doc.getType());
        values.put("phone", doc.getContactInfo()[0]);
        values.put("email", doc.getContactInfo()[2]);
        values.put("address", doc.getAddress()[0]);
        values.put("city", doc.getAddress()[1]);
        values.put("state", doc.getAddress()[2]);
        values.put("zip", doc.getAddress()[2]);
        values.put("fax", doc.getContactInfo()[1]);
        values.put("patient_id", doc.getPatient());

        SQLiteDatabase db = this.getWritableDatabase();

        Long result = db.insertOrThrow(TABLE_DOCTORS, null, values);
        Log.i("Result", "addDoctor: " + result);
        db.close();
    }
    public void updateDoctor(Doctor doc){
        String filter = "_id = " + doc.getID();
        ContentValues values = new ContentValues();
        values.put("name", doc.getName());
        values.put("type", doc.getType());
        values.put("phone", doc.getContactInfo()[0]);
        values.put("email", doc.getContactInfo()[2]);
        values.put("address", doc.getAddress()[0]);
        values.put("city", doc.getAddress()[1]);
        values.put("state", doc.getAddress()[2]);
        values.put("zip", doc.getAddress()[2]);
        values.put("fax", doc.getContactInfo()[1]);
        values.put("patient_id", doc.getPatient());

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_DOCTORS, values, filter, null);
        db.close();
    }
    public ArrayList<Doctor> loadDoctors(int patient) {
        String query = "Select * FROM " + TABLE_DOCTORS + " WHERE " + "patient_id" + " =  \"" + patient + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Doctor doc;
        ArrayList<Doctor> doctors = new ArrayList<>();

        while(cursor.moveToNext()) {
            doc = new Doctor();
            doc.setID(cursor.getInt(0));
            doc.setName(cursor.getString(1));
            doc.setType(cursor.getString(2));
            doc.setContactInfo(cursor.getString(3), cursor.getString(9), cursor.getString(4));
            doc.setAddress(cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            doc.setPatient(cursor.getInt(10));

            doctors.add(doc);
            //user.patient.addDoctor(doc);
        }

        cursor.close();
        db.close();
        return doctors;
    }

    public void addPrescription(Prescription rx){
        ContentValues values = new ContentValues();
        values.put("_id", rx.getID());
        values.put("name", rx.getName());
        values.put("dosage", rx.getDosage());
        values.put("symptoms", rx.getSymptoms());
        values.put("doctor", rx.getDoctorName());
        values.put("instructions", rx.getInstructions());
        values.put("patient_id", rx.getPatient());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PRESCRIPTIONS, null, values);
        db.close();
    }
    public void updatePrescription(Prescription rx){
        String filter = "_id = " + rx.getID();
        ContentValues values = new ContentValues();
        values.put("name", rx.getName());
        values.put("dosage", rx.getDosage());
        values.put("symptoms", rx.getSymptoms());
        values.put("doctor", rx.getDoctorName());
        values.put("instructions", rx.getInstructions());
        values.put("patient_id", rx.getPatient());

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_PRESCRIPTIONS, values, filter, null);
        db.close();
    }
    public ArrayList<Prescription> loadPrescriptions(int patient) {
        String query = "Select * FROM " + TABLE_PRESCRIPTIONS + " WHERE " + "patient_id" + " =  \"" + patient + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Prescription rx;
        ArrayList<Prescription> prescriptions = new ArrayList<>();

        while(cursor.moveToNext()) {
            rx = new Prescription();
            rx.setID(cursor.getInt(0));
            rx.setName(cursor.getString(1));
            rx.setDosage(cursor.getString(2));
            rx.setSymptoms(cursor.getString(3));
            rx.setDoctorName(cursor.getString(4));
            rx.setInstructions(cursor.getString(5));
            rx.setPatient(cursor.getInt(6));

            //user.patient.addPrescription(rx);
            prescriptions.add(rx);
        }
        cursor.close();
        db.close();
        return prescriptions;
    }

    public void addPharmacy(Pharmacy pharmacy){
        ContentValues values = new ContentValues();
        values.put("_id", pharmacy.getID());
        values.put("name", pharmacy.getName());
        values.put("address", pharmacy.getAddress()[0]);
        values.put("city", pharmacy.getAddress()[1]);
        values.put("state", pharmacy.getAddress()[2]);
        values.put("zip", pharmacy.getAddress()[3]);
        values.put("phone", pharmacy.getPhone());
        values.put("patient_id", pharmacy.getPatient());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PHARMACIES, null, values);
        db.close();
    }
    public void updatePharmacy(Pharmacy pharmacy){
        String filter = "_id = " + pharmacy.getID();
        ContentValues values = new ContentValues();
        values.put("name", pharmacy.getName());
        values.put("address", pharmacy.getAddress()[0]);
        values.put("city", pharmacy.getAddress()[1]);
        values.put("state", pharmacy.getAddress()[2]);
        values.put("zip", pharmacy.getAddress()[3]);
        values.put("phone", pharmacy.getPhone());
        values.put("patient_id", pharmacy.getPatient());

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_PHARMACIES, values, filter, null);
        db.close();
    }
    public ArrayList<Pharmacy> loadPharmacies(int patient) {
        String query = "Select * FROM " + TABLE_PHARMACIES + " WHERE " + "patient_id" + " =  \"" + patient + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Pharmacy pharmacy;
        ArrayList<Pharmacy> pharmacies = new ArrayList<>();

        while(cursor.moveToNext()) {
            pharmacy = new Pharmacy();
            pharmacy.setID(cursor.getInt(0));
            pharmacy.setName(cursor.getString(1));
            pharmacy.setAddress(cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            pharmacy.setPhone(cursor.getString(6));
            pharmacy.setPatient(cursor.getInt(7));

            //user.patient.addPrescription(rx);
            pharmacies.add(pharmacy);
        }
        cursor.close();
        db.close();
        return pharmacies;
    }

    public void addInsurance(Insurance insurance){
        ContentValues values = new ContentValues();
        values.put("_id", insurance.getID());
        values.put("provider", insurance.getProvider());
        values.put("mid", insurance.getMID());
        values.put("groupnum", insurance.getGroupNumber());
        values.put("rxbin", insurance.getRxBin());
        values.put("rxpcn", insurance.getRxPcn());
        values.put("rxgrp", insurance.getRxGroup());
        values.put("patient_id", insurance.getPatient());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_INSURANCES, null, values);
        db.close();
    }
    public void updateInsurance(Insurance insurance){
        String filter = "_id = " + insurance.getID();
        ContentValues values = new ContentValues();
        values.put("provider", insurance.getProvider());
        values.put("mid", insurance.getMID());
        values.put("groupnum", insurance.getGroupNumber());
        values.put("rxbin", insurance.getRxBin());
        values.put("rxpcn", insurance.getRxPcn());
        values.put("rxgrp", insurance.getRxGroup());
        values.put("patient_id", insurance.getPatient());

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_INSURANCES, values, filter, null);
        db.close();
    }
    public ArrayList<Insurance> loadInsurances(int patient) {
        String query = "Select * FROM " + TABLE_INSURANCES + " WHERE " + "patient_id" + " =  \"" + patient + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Insurance insurance;
        ArrayList<Insurance> insurances = new ArrayList<>();

        while(cursor.moveToNext()) {
            insurance = new Insurance();
            insurance.setID(cursor.getInt(0));
            insurance.setProvider(cursor.getString(1));
            insurance.setMID(cursor.getString(2));
            insurance.setGroupNumber(cursor.getString(3));
            insurance.setRxBin(cursor.getString(4));
            insurance.setRxPcn(cursor.getString(5));
            insurance.setRxGroup(cursor.getString(6));
            insurance.setPatient(cursor.getInt(7));

            //user.patient.addPrescription(rx);
            insurances.add(insurance);
        }
        cursor.close();
        db.close();
        return insurances;
    }

    public void addExcercise(Excercise excercise){
        ContentValues values = new ContentValues();
        values.put("_id", excercise.getId());
        values.put("name", excercise.getName());
        values.put("start", excercise.getStart());
        values.put("duration", excercise.getDuration());
        values.put("calories", excercise.getCalories());
        values.put("comments", excercise.getComments());
        values.put("date", excercise.getDate());
        values.put("patient_id", excercise.getPatient());
        values.put("specialist_id", excercise.getSpecialist());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EXCERCISES, null, values);
        db.close();
    }

    public void deleteDoc(String table, int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(table, COLUMN_ID + " = " + id, null);
        db.close();
    }


}
