package edu.fau.ngamarra2014.caremulator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);

        String text = Car.getNoOfCars()+ " cars\n";

        Car myCar = new Car();
        text = text + myCar.printToString();
        text = text + Car.getNoOfCars()+ " cars\n";

        Car yourCar = new Car("Porsche", "Cayman", 2007, "Blue", 10000, 2);
        text = text + yourCar.printToString();
        text = text + Car.getNoOfCars()+ " cars\n";

        Car myNextCar = new Car("Porsche", "Cayman");
        text = text + myNextCar.printToString();
        text = text + Car.getNoOfCars()+ " cars\n";

        tv.setText(text);
        setContentView(tv);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
