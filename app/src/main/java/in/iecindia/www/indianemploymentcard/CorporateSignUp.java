package in.iecindia.www.indianemploymentcard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class CorporateSignUp extends AppCompatActivity {
    private EditText Cname, Cemail,Caddress,Pannumber,nbrbrnch,TinNumber,ceoname;
    private String cname, cemail,caddress,pannumber,NBRbrnch,tinNumber,ceoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_sign_up);

        Cname = findViewById(R.id.companyname);
        Cemail = findViewById(R.id.companyemail);
        Caddress = findViewById(R.id.companyaddress);
        Pannumber = findViewById(R.id.pannumber);
        nbrbrnch = findViewById(R.id.noofbrnch);
        TinNumber = findViewById(R.id.tinnumber);
        ceoname = findViewById(R.id.ceoname);

    }
    public void CorporateSignUp(View view){
        if (Cname.getText().toString().length() == 0){
         Cname.setError("Please fill");
         return;
        }if (Cemail.getText().toString().length()==0) {
            Cemail.setError("Please fill");
            return;
        }if (Caddress.getText().toString().length()==0){
                Caddress.setError("Please fill");
                return;
        }if (Pannumber.getText().toString().length()==0){
            Pannumber.setError("Please fill");
            return;
        }if (nbrbrnch.getText().toString().length()==0){
            nbrbrnch.setError("Please fill");
            return;
        }if (TinNumber.getText().toString().length()==0){
            TinNumber.setError("Please fill");
            return;
        }if (ceoname.getText().toString().length()==0){
            ceoname.setError("Please fill");
            return;
        }
        cname = Cname.getText().toString().trim();
        cemail = Cemail.getText().toString().trim();
        caddress = Caddress.getText().toString().trim();
        pannumber = Pannumber.getText().toString().trim();
        NBRbrnch = nbrbrnch.getText().toString().trim();
        tinNumber = TinNumber.getText().toString().trim();
        ceoName = ceoname.getText().toString().trim();

        if (isOnline()){
            String method = "CorporateRegister";
            CorporateBackgrndTask corporateBackgrndTask = new CorporateBackgrndTask(CorporateSignUp.this);
            corporateBackgrndTask.execute(method,cname,cemail,caddress,pannumber,NBRbrnch,tinNumber,ceoName);
        }

    }
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    public class CorporateBackgrndTask extends AsyncTask<String,Void,String> {
        Context ctx;

        public CorporateBackgrndTask(Context ctx1){
            this.ctx = ctx1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://172.28.172.2:8080/CorporateSectorPeople.php";
            Log.d("TAG", "attempt to register");

            String method = params[0];
            if (method.equals("CorporateRegister")) {
                String CName = params[1];
                String Cemail = params[2];
                String CAddress = params[3];
                String panNuber = params[4];
                String NumBrnch = params[5];
                String TinNum = params[6];
                String Ceoname = params[7];


                Log.d("SIT", CName + "" + Cemail+""+CAddress+""+panNuber+""+NumBrnch+""+TinNum+""+Ceoname);
                try {
                    URL url = new URL(reg_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.d("QWE", "open url connection");
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    Log.d("QWE", "buffered writer");
                    // encode data


                    String OpenConn = URLEncoder.encode("CName", "UTF-8") + "=" + URLEncoder.encode(CName, "UTF-8") + "&" +
                            URLEncoder.encode("Cemail", "UTF-8") + "=" + URLEncoder.encode(Cemail, "UTF-8") + "&" +
                            URLEncoder.encode("CAddress", "UTF-8") + "=" + URLEncoder.encode(CAddress, "UTF-8") + "&" +
                            URLEncoder.encode("panNuber", "UTF-8") + "=" + URLEncoder.encode(panNuber, "UTF-8") + "&" +
                            URLEncoder.encode("NumBrnch", "UTF-8") + "=" + URLEncoder.encode(NumBrnch, "UTF-8") + "&" +
                            URLEncoder.encode("TinNum", "UTF-8") + "=" + URLEncoder.encode(TinNum, "UTF-8") + "&" +
                            URLEncoder.encode("Ceoname", "UTF-8") + "=" + URLEncoder.encode(Ceoname, "UTF-8");
                    Log.d("QWE", "data parameter set");
                    bufferedWriter.write(OpenConn);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    Log.d("QWE", "buffer writer close");
                    os.close();
                    // get Reponce from server
                    InputStream is = httpURLConnection.getInputStream();
                    Log.d("QWE", "debug"+is);
                    is.close();
                    return "Registration Successfull";
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
}
