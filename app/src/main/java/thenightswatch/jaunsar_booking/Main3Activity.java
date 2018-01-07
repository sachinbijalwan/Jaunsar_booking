package thenightswatch.jaunsar_booking;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity implements BackgroundWorker.AsyncResponse {

    String out="[{\"ID\":\"1\",\"START_TIME\":\"1\",\"END_TIME\":\"1\",\"START_LOCATION\":\"1\",\"END_LOCATION\":\"1\"},{\"ID\":\"2\",\"START_TIME\":\"19_12_2017+14_59\",\"END_TIME\":\"19_12_2017+14_59\",\"START_LOCATION\":\"fds\",\"END_LOCATION\":\"fds\"},{\"ID\":\"4\",\"START_TIME\":\"19_12_2017 15_3\",\"END_TIME\":\"19_12_2017 15_3\",\"START_LOCATION\":\"fac\",\"END_LOCATION\":\"fsjdk\"},{\"ID\":\"5\",\"START_TIME\":\"25_12_2017 21_0\",\"END_TIME\":\"28_12_2017 12_0\",\"START_LOCATION\":\"vikasnagar\",\"END_LOCATION\":\"dehradun\"}]";
    String[] phone_number;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Log.d("Deb","running");
        setContentView(R.layout.progress_bar);
        //Log.d("Deb","running");
        BackgroundWorker backgroundworker=new BackgroundWorker(this);
        backgroundworker.delegate=this;
        //Log.d("Deb","running");
        backgroundworker.execute("output");
        //Log.d("Deb","running");
        //Log.d("Deb","oncreate finished");

    }

    @Override
    public void processFinish(String output) {
        out=output;
        setContentView(R.layout.activity_main3);
        try {
            loadIntoListView(out);
//            Log.d("Deb","LOad into listview has run");
        } catch (JSONException e) {
            e.printStackTrace();
  //          Log.d("Deb","Error "+e.getMessage());
        }
    }


    private void loadIntoListView(String json) throws JSONException {
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);

        ArrayList<element> a=new ArrayList<element>();
        //looping through all the elements in json array
        phone_number=new String[jsonArray.length()];
    //    Log.d("Deb","Phone number created successfully");
        for (int i = 0; i < jsonArray.length(); i++) {

            //getting json object from the json array
            JSONObject obj = jsonArray.getJSONObject(i);

            //getting the name from the json object and putting it inside string array
            element b=new element(obj.getString("START_TIME"),obj.getString("END_TIME"),obj.getString("START_PLACE"),obj.getString("END_PLACE"));
            a.add(b);
            phone_number[i]=obj.getString("PHONE_NUMBER");
      //      Log.d("Deb",phone_number[i]+"it is the phoen number");

        }
        CustomArrayAdapter customArrayAdapter=new CustomArrayAdapter(this,a);
        listView=(ListView) findViewById(R.id.list);
        listView.setAdapter(customArrayAdapter);

       }
       public void contacting(View v){
           int position = listView.getPositionForView(v);
           dialContactPhone(phone_number[position]);
       }
    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
    @Override
    public void onBackPressed(){
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

}
