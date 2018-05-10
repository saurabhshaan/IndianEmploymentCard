package in.iecindia.www.indianemploymentcard;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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

public class EducationalSectorSignUp extends AppCompatActivity {
    FinalSignInPrefManager finalSignInPrefManager;
    private EditText Uname,Uemail, Uaddress,UGCnumber,nbrClz,regisNumber,vcname;
    private String uname,uemail, uaddress,ugCnumber,NBRClz,RegisNumber,VCname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational_sector_sign_up);

        Uname = findViewById(R.id.universityname);
        Uemail = findViewById(R.id.universityemail);
        Uaddress = findViewById(R.id.universityaddress);
        UGCnumber = findViewById(R.id.ugcnumber);
        nbrClz = findViewById(R.id.noofcollege);
        regisNumber = findViewById(R.id.registrationnumber);
        vcname = findViewById(R.id.vcname);

        finalSignInPrefManager = new FinalSignInPrefManager(this);
        if (!finalSignInPrefManager.IsLogOnFirstTimeLaunch()){

        }


    }
    public void EducationSignUp(View view){
        if (Uname.getText().toString().length() == 0){
            Uname.setError("Please fill it");
            return;
        }if (Uemail.getText().toString().length()==0){
            Uemail.setError("Please fill");
            return;
        }if (Uaddress.getText().toString().length() ==0){
            Uaddress.setError("Please fill it");
            return;
        }if (UGCnumber.getText().toString().length() == 0){
            UGCnumber.setError("Please fill it");
            return;
        }if (nbrClz.getText().toString().length() == 0){
            nbrClz.setError("Please Fill it");
            return;
        }if (regisNumber.getText().toString().length()==0){
            regisNumber.setError("Please fill it");
            return;
        }if (vcname.getText().toString().length() == 0){
            vcname.setText("Please fill it");
            return;
        }else {
            Toast.makeText(this, "Thanks You", Toast.LENGTH_SHORT).show();
        }
        uname = Uname.getText().toString().trim();
        uemail = Uemail.getText().toString().trim();
        uaddress = Uaddress.getText().toString().trim();
        ugCnumber = UGCnumber.getText().toString().trim();
        NBRClz = nbrClz.getText().toString().trim();
        RegisNumber = regisNumber.getText().toString().trim();
        VCname = vcname.getText().toString().trim();

        if (isOnline()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view1 = getLayoutInflater().inflate(R.layout.message,null);
            builder.setView(view1);
            AlertDialog dialog = builder.create();
            dialog.show();

            String method = "EducationRegistration";
            EducationalSectorBackgroundTask task = new EducationalSectorBackgroundTask(EducationalSectorSignUp.this);
            task.execute(method,uname,uemail,uaddress,ugCnumber,NBRClz,RegisNumber,VCname);
            launchHomeScreen();
        }
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
    private void launchHomeScreen(){
        finalSignInPrefManager.setFirstLaunch(false);
        startActivity(new Intent(EducationalSectorSignUp.this,LogOn.class));
        finish();
    }
    public class EducationalSectorBackgroundTask extends AsyncTask<String,Void,String> {
        Context ctx;

        public EducationalSectorBackgroundTask(Context ctx1){
            this.ctx = ctx1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://172.28.172.2:8080/IndianEmploymentCard/EducationalSectorPeople.php";
            Log.d("TAG", "attempt to register");

            String method = params[0];
            if (method.equals("EducationRegistration")) {
                String UniName = params[1];
                String UEmail = params[2];
                String UniAddress = params[3];
                String UniUGCNuber = params[4];
                String NumClz = params[5];
                String UniRegNum = params[6];
                String UniVCname = params[7];


                Log.d("SIT", UniName + "" + UEmail+""+UniAddress+""+UniUGCNuber+""+NumClz+""+UniRegNum+""+UniVCname);
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


                    String OpenConn = URLEncoder.encode("UniName", "UTF-8") + "=" + URLEncoder.encode(UniName, "UTF-8") + "&" +
                            URLEncoder.encode("UEmail", "UTF-8") + "=" + URLEncoder.encode(UEmail, "UTF-8") + "&" +
                            URLEncoder.encode("UniAddress", "UTF-8") + "=" + URLEncoder.encode(UniAddress, "UTF-8") + "&" +
                            URLEncoder.encode("UniUGCNuber", "UTF-8") + "=" + URLEncoder.encode(UniUGCNuber, "UTF-8") + "&" +
                            URLEncoder.encode("NumClz", "UTF-8") + "=" + URLEncoder.encode(NumClz, "UTF-8") + "&" +
                            URLEncoder.encode("UniRegNum", "UTF-8") + "=" + URLEncoder.encode(UniRegNum, "UTF-8") + "&" +
                            URLEncoder.encode("UniVCname", "UTF-8") + "=" + URLEncoder.encode(UniVCname, "UTF-8");
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
}