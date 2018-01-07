package thenightswatch.jaunsar_booking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sachin on 14/12/17.
 */

public class BackgroundWorker extends AsyncTask<String, Void, Void> {
    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }


    public AsyncResponse delegate = null;
    Context context;
    String response="";
    boolean completed=false;

    BackgroundWorker(Context ctx) {
        context = ctx;
    }


    @Override
    protected Void doInBackground(String... params) {

        //Log.d("Deb","function being called "+params[0]);
        String urlString = "https://jaunsar-bawar.000webhostapp.com/index.php"; // URL to call
       HashMap postDataParams = new HashMap();
        postDataParams.put("type",params[0]);
       // Log.d("Deb","function being called "+params[0]+params[0].equals("input"));
        if(params[0].equals("input")) {
            //Log.d("Deb","function being called running "+params[0]);

            postDataParams.put("Starting_time", params[1]);
            postDataParams.put("Ending_time", params[2]);
            postDataParams.put("Starting_location", params[3]);
            postDataParams.put("Ending_location", params[4]);
            postDataParams.put("Phone_number",params[5]);
        }
        else if(params[0].equals("delete")){
            postDataParams.put("id",params[1]);
        }
        //Log.d("Deb","App working fine"+params[1]+params[2]+params[3]+params[4]);
        OutputStream out = null;
        InputStream in=null;
        try {
            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
           // Log.d("Deb","App working till here");

            out = new BufferedOutputStream(urlConnection.getOutputStream());
           // Log.d("Deb","App working till fine");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            //a="username=888";
           // Log.d("Deb","App working till fine");
            writer.write(getPostDataString(postDataParams));
           // Log.d("Deb","App working till fine");

            writer.flush();

            writer.close();

            out.close();

            int responseCode=urlConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;

                }
            }
            else {
                response="";

            }
            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();




        }
        catch (Exception e) {

            System.out.println(e.getMessage());

        }
         return null;
    }



    private String getPostDataString(HashMap<String,String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
       delegate.processFinish(response);
    }
}