package in.iecindia.www.indianemploymentcard;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by root on 2/2/18.
 */

public class LogOnBackGroundTask extends AsyncTask<String,Void,String> {
    Context ctx;
    public LogOnBackGroundTask(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... params) {
        String reg_url = "";
        Log.d("TAG", "attempt to register");

        String method = params[0];
        if (method.equals("GetIn")) {
            String EmailorIEC = params[1];
            String Password = params[2];
            Log.d("TAG", EmailorIEC + "" + Password);
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                Log.d("TAG", "open url connection");
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.d("TAG", "buffered writer");
                // encode data
                String OpenConn = URLEncoder.encode("EmailorIEC", "UTF-8") + "=" + URLEncoder.encode(EmailorIEC, "UTF-8") + "&" +
                        URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode("Password", "UTF-8");
                Log.d("TAG", "data parameter set");
                bufferedWriter.write(OpenConn);
                bufferedWriter.flush();
                bufferedWriter.close();
                Log.d("TAG", "buffer writer close");
                os.close();
                // get Reponce from server
                InputStream is = httpURLConnection.getInputStream();
                Log.d("TAG", "debug");
                is.close();
                return "Login";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}