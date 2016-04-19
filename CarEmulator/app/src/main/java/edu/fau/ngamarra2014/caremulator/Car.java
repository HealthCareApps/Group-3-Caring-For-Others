package edu.fau.ngamarra2014.caremulator;

/**
 * Created by nicho_000 on 1/22/2016.
 */
public class Car {
    private String 	make;
    private String 	model;
    private int year;
    private String 	color;
    private int mileage;
    private int noDoors;
    private int id;
    private static int noOfCars = 0;



    public Car() { // default constructor
        make = "Unknown";
        model = "Unknown";
        year = 2009;
        color = "Unknown";
        mileage = 0;
        noDoors = 4;
        id = noOfCars;
        noOfCars++;

    }
    //constructor with 2 parameters
    public Car(String mk, String mo) {
        make = mk;
        model = mo;
        year = 2009;
        color = "Unknown";
        mileage = 0;
        noDoors = 4;
        id = noOfCars;
        noOfCars++;

    }
    //constructor with 6 parameters
    public Car(String mk, String mo, int y, String c, int mi, int n) {
        make = mk;
        model = mo;
        year = y;
        color = c;
        mileage = mi;
        noDoors = n;
        id = noOfCars;
        noOfCars++;

    }

    public void paint(String newColor) {
        if (newColor == "BLUE" || newColor == "GREEN" || newColor == "RED")
            color = newColor;
    }

    public static int getNoOfCars() {
        return noOfCars;
    }

    public String printToString() {
        String s = "ID: " + id + "\n";
        s = s + "Make: " + make + "\n";
        s = s + "Model: " + model + "\n";
        s = s + "Year: " + year + "\n";
        s = s + "Color: " + color + "\n";
        s = s + "Mileage: " + mileage + "\n";
        s = s + "Doors: " + noDoors + "\n";
        return s;
    }


}
