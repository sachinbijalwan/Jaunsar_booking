package thenightswatch.jaunsar_booking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;

public class Main2Activity extends AppCompatActivity {
    int value=0;
    SharedPreferences mBooking;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBooking = this.getSharedPreferences("Booking", Context.MODE_PRIVATE);
       // Log.d("Deb","Workign till here");
        boolean used=mBooking.getBoolean("open",false);
      //  Log.d("first","used is "+used);
        if(used == true)
        {
            int number=mBooking.getInt("number",1);
            value=1;
            if(number==1)
            {
                startmain(null);

            }
            else{
                startanother(null);
            }
        }
        else {

            setContentView(R.layout.activity_main2);

        }
      //  Log.d("Deb","Workign till here");

    }
    public void startmain(View view){
        if(value == 0){
            editor = mBooking.edit();
            editor.putBoolean("open",true);
            editor.putInt("number",1);
            editor.apply();
        }
        Intent myIntent = new Intent(Main2Activity.this, MainActivity.class);
        Main2Activity.this.startActivity(myIntent);

    }

    public void startanother(View view){
        if(value == 0){
            editor = mBooking.edit();
            editor.putBoolean("open",true);
            editor.putInt("number",2);

        }

        Intent myIntent = new Intent(Main2Activity.this, Main3Activity.class);
        Main2Activity.this.startActivity(myIntent);

    }
}
