package com.example.myleave;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class background extends AsyncTask<String,Void,String> {

    Context context;
    JSONObject jsonObject;
    public static String id,from_date,to_date,time,status;

    public  background(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            String name = jsonObject.getString("data");
            JSONArray jsonArray = new JSONArray(name);

            JSONObject jsonPart = jsonArray.getJSONObject(0);

            id = jsonPart.getString("id");
            from_date = jsonPart.getString("from_date");
            to_date = jsonPart.getString("to_date");
            time = jsonPart.getString("time");
            status = jsonPart.getString("status");
            String string = "ID : " + id + "\nFrom Date : " + from_date + "\nTo Date : " +  to_date + "\nTime : " + time + "\nStatus : " + status;
            CameraActivity.late_entry_id.setText(string);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        String result = "";

        String connstr = "http://13.232.251.218/MyLeave/php/app_data_retrive.php";

        try {

            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
            String data = URLEncoder.encode("late_entry_id","UTF-8")+"="+URLEncoder.encode(MainActivity.late_entry_id,"UTF-8");

            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();

            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
            String line = "";

            while ((line = reader.readLine())!=null)
            {
                result +=line;
            }
            jsonObject = new JSONObject(result);
            reader.close();
            ips.close();
            http.disconnect();
            return result;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
