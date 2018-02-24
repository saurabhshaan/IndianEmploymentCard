package in.iecindia.www.indianemploymentcard;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
 * Created by root on 11/2/18.
 */

public class SignUpBackground extends AsyncTask<String,Void,String> {
    Context ctx;

    public SignUpBackground(Context ctx1){
        this.ctx = ctx1;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://172.28.172.2/IndianEmploymentCard/IndividualSignUp.php";
        Log.d("TAG", "attempt to register");

        String method = params[0];
        if (method.equals("SENDDATA")) {
            String firstname = params[1];
            String lastname = params[2];
            String middleName = params[3];
            String Email = params[4];
            String AdharNumber = params[5];
            String Addline1 = params[6];
            String Addline2 = params[7];
            String Addline3 = params[8];
            String AddPincode = params[9];
            String AddState = params[10];
            String AddCountry = params[11];
            String hscholl = params[12];
            String hrollnumber = params[13];
            String imedite = params[14];
            String irollnumber = params[15];
            String gcourse = params[16];
            String gstatus = params[17];
            String guniversity = params[18];
            String grollnumber = params[19];
            String encodedImage = params[20];

            Log.d("SIT", firstname + "" + lastname+""+middleName+""+Email+""+AdharNumber+""+Addline1+""+Addline2+""
            +Addline3+""+AddPincode+""+AddState+""+AddCountry+""+hscholl+""+hrollnumber+""+imedite+""+irollnumber+""
            +gcourse+""+gstatus+""+guniversity+""+grollnumber+""+encodedImage);
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


                String OpenConn = URLEncoder.encode("First_Name", "UTF-8") + "=" + URLEncoder.encode(firstname, "UTF-8") + "&" +
                        URLEncoder.encode("Last_Name", "UTF-8") + "=" + URLEncoder.encode(lastname, "UTF-8") + "&" +
                        URLEncoder.encode("Middle_Name", "UTF-8") + "=" + URLEncoder.encode(middleName, "UTF-8") + "&" +
                        URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8") + "&" +
                        URLEncoder.encode("Adhar_Number", "UTF-8") + "=" + URLEncoder.encode(AdharNumber, "UTF-8") + "&" +
                        URLEncoder.encode("Add_line1", "UTF-8") + "=" + URLEncoder.encode(Addline1, "UTF-8") + "&" +
                        URLEncoder.encode("Add_line2", "UTF-8") + "=" + URLEncoder.encode(Addline2, "UTF-8") + "&" +
                        URLEncoder.encode("Add_line3", "UTF-8") + "=" + URLEncoder.encode(Addline3, "UTF-8") + "&" +
                        URLEncoder.encode("Pincode", "UTF-8") + "=" + URLEncoder.encode(AddPincode, "UTF-8") + "&" +
                        URLEncoder.encode("State", "UTF-8") + "=" + URLEncoder.encode(AddState, "UTF-8") + "&" +
                        URLEncoder.encode("Country", "UTF-8") + "=" + URLEncoder.encode(AddCountry, "UTF-8") + "&" +
                        URLEncoder.encode("Highschool_Board", "UTF-8") + "=" + URLEncoder.encode(hscholl, "UTF-8") + "&" +
                        URLEncoder.encode("Highschool_Roll_Number", "UTF-8") + "=" + URLEncoder.encode(hrollnumber, "UTF-8") + "&" +
                        URLEncoder.encode("Intermediate_Board", "UTF-8") + "=" + URLEncoder.encode(imedite, "UTF-8") + "&" +
                        URLEncoder.encode("Intermediate_Roll_Number", "UTF-8") + "=" + URLEncoder.encode(irollnumber, "UTF-8") + "&" +
                        URLEncoder.encode("Graduation_Course", "UTF-8") + "=" + URLEncoder.encode(gcourse, "UTF-8") + "&" +
                        URLEncoder.encode("Graduation_Status", "UTF-8") + "=" + URLEncoder.encode(gstatus, "UTF-8") + "&" +
                        URLEncoder.encode("Graduation_University", "UTF-8") + "=" + URLEncoder.encode(guniversity, "UTF-8")+ "&" +
                        URLEncoder.encode("Graduation_Roll_Number", "UTF-8") + "=" + URLEncoder.encode(grollnumber, "UTF-8")+ "&" +
                        URLEncoder.encode("Person_Image", "UTF-8") + "=" + URLEncoder.encode(encodedImage, "UTF-8");
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
        Toast.makeText(ctx, "Registered", Toast.LENGTH_SHORT).show();
    }
}