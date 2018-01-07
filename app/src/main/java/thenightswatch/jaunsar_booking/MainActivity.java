package thenightswatch.jaunsar_booking;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.String;
public class MainActivity extends AppCompatActivity implements BackgroundWorker.AsyncResponse {

    int mYear=2018;
    int mMonth=1;
    int mDay=1;
    int mHour=0,mMinute=0;
    Button date,time,date2,time2,temp;
    EditText pickup,drop,phone_number;
    int[] startdate;
    int[] enddate;
    String input="input";
    boolean startd=false,endd=false,phone=false,stored_value=false;
    SharedPreferences mBooking;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mBooking = this.getSharedPreferences("Requests", Context.MODE_PRIVATE);
        stored_value=mBooking.getBoolean("stored",false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date=(Button) findViewById(R.id.date);
        date2=(Button) findViewById(R.id.date2);
        pickup=(EditText) findViewById(R.id.pickup_button);
        drop=(EditText) findViewById(R.id.drop_button);
        phone_number=(EditText) findViewById(R.id.phone_number);
        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

    }
    @Override
    public void onBackPressed(){
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete:
                editor = mBooking.edit();
               BackgroundWorker backgroundworker=new BackgroundWorker(this);
                backgroundworker.delegate=this;
                int value=mBooking.getInt("id",0);
                if(!stored_value)
                {
                    Toast.makeText(this,"आपने कोई request नी डाल रखी है",Toast.LENGTH_LONG).show();
                    return true;
                }
                backgroundworker.execute("delete",String.valueOf(value));
                setContentView(R.layout.progress_bar);
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                return true;
            case R.id.info:
                Intent myIntent = new Intent(MainActivity.this, about.class);
                MainActivity.this.startActivity(myIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onClick(View v) {
        temp=(Button) v;
        if (v == date || v==date2) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            temp.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            if(temp == date)
                            {
                                startdate= new int[]{dayOfMonth, monthOfYear+1, year};
                                startd=true;
                            }
                            if(temp == date2)
                            {
                                enddate = new int[]{dayOfMonth, monthOfYear+1, year};
                                endd=true;

                            }


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }
    public boolean checkvariables(){

        return (startd && endd && !(pickup.getText().toString().isEmpty()) && !(drop.getText().toString().isEmpty()) && !(phone_number.getText().toString().isEmpty()) && !stored_value);
    }

    public void StartAnotherActivity(View v){
        String s=phone_number.getText().toString();
        s=s.trim();
        if(checkvariables() == true && isValidPhoneNumber(s) && !stored_value)
        {
            BackgroundWorker backgroundworker=new BackgroundWorker(this);
            backgroundworker.delegate=this;
            String starting=integertostring(startdate);
            String ending=integertostring(enddate);
           // Log.d("Deb",s+" this is the phoen num");

            backgroundworker.execute(input,starting,ending,pickup.getText().toString(),drop.getText().toString(),s);
            setContentView(R.layout.progress_bar);
            //.d("Deb","completed "+backgroundworker.response);
            backgroundworker.completed=false;
            //.d("Deb","completed "+backgroundworker.response);
            Toast.makeText(this,backgroundworker.response,Toast.LENGTH_LONG).show();

        }
        else if(stored_value){
            Toast.makeText(this,"आपने पहले ही एक रिक्वेस्ट डाल रखी है. नयी रिक्वेस्ट के लिए उसे ऊपर दिए 3 बिन्दु वाले बटन पे क्लिक करके हटाएं.",Toast.LENGTH_LONG).show();
        }
        else if(!checkvariables()){
            Toast.makeText(this,"आपने सारी चीज़ें नी भरी है",Toast.LENGTH_LONG).show();
        }
        else if(!isValidPhoneNumber(s)){
            Toast.makeText(this,"फोन नंबर गलत है",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"unknown error",Toast.LENGTH_LONG).show();
        }

    }
    private static boolean isValidPhoneNumber(String phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber) && android.util.Patterns.PHONE.matcher(phoneNumber).matches() &&(phoneNumber.length()>9 && phoneNumber.length()<14);
    }
    String integertostring(int... intNumbers){
        StringBuffer sbfNumbers = new StringBuffer();

        //define the separator you want in the string. This example uses space.
        String strSeparator = "_";

        if(intNumbers.length > 0){

            //we do not want leading space for first element
            sbfNumbers.append(intNumbers[0]);

                        /*
                         * Loop through the elements of an int array. Please
                         * note that loop starts from 1 not from 0 because we
                         * already appended the first element without leading space.s
                         */
            for(int i=1; i < intNumbers.length; i++){
                sbfNumbers.append(strSeparator).append(intNumbers[i]);
            }

        }
        return sbfNumbers.toString();
    }

    @Override
    public void processFinish(String output) {
        //setContentView(R.layout.activity_main);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.recreate();
        setSupportActionBar(toolbar);
        //.d("Deb","processFinish called "+output+stored_value);
        if(output.isEmpty())
        {
            Toast.makeText(this,"Data not deleted",Toast.LENGTH_LONG).show();
            return;
        }


        if(stored_value == false) {
            int value = Integer.parseInt(output.replaceAll("[^0-9]", ""));
            String output2 = "";
            int i = 0;
            while (output.charAt(i) >= '9' || output.charAt(i) <= '0') {
                output2 = output2 + output.charAt(i);
                i++;
            }
            editor = mBooking.edit();
            editor.putBoolean("stored", true);
            stored_value = true;
            editor.putInt("id", value);
            editor.apply();
            //.d("Deb", value + output);
            if (!output2.equals("डाटा डल चुका है")) {
                output2 = "डाटा डाला नहीं गया. कृपया दोबारा डालें";
            }
            Toast.makeText(this, output2, Toast.LENGTH_LONG).show();
        }
        else{
            if(output.equals("डाटा हट गया है")) {
                editor.putBoolean("stored", false);
                editor.apply();
                stored_value = false;
            }
            Toast.makeText(this,output,Toast.LENGTH_LONG).show();
        }

    }
}
