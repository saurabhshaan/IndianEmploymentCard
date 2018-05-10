package in.iecindia.www.indianemploymentcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LogOn extends AppCompatActivity{
    private LogOnPrefManager logOnPrefManager;
    private EditText emailoriec,mpassword;
    private String emailice, pass;
    private Button signinbtn;
    SharedPreferences.Editor editor;
    String mFristName,mLastName,mMiddleName,mEmail,mAdhaarNumber,mQualification_Status,mExperience_Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_on);

        emailoriec = findViewById(R.id.EmailorIEC);
        mpassword = findViewById(R.id.password);

        logOnPrefManager = new LogOnPrefManager(this);
        if (!logOnPrefManager.IsLogOnFirstTimeLaunch()){
//        launchHomeScreen();
        }
    }
    public void letsgo(View view){
        if (emailoriec.getText().toString().isEmpty() && mpassword.getText().toString().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(LogOn.this);
            View view1 = getLayoutInflater().inflate(R.layout.sign_up_option,null);

            builder.setView(view1);
            AlertDialog dialog = builder.create();
            dialog.show();

        }else {
            //validation
            if (emailoriec.getText().toString().length() == 0){
                emailoriec.setError("Required field");
                return;
            }if (mpassword.getText().toString().length() == 0){
                mpassword.setError("Required field");
            }else {
                Toast.makeText(this, "Fields Validated", Toast.LENGTH_SHORT).show();
            }
            //validation end here
            emailice = emailoriec.getText().toString().trim();
            pass = mpassword.getText().toString().trim();
            if (isOnline()) {
                new LoginFetch().execute(emailice, pass);
            }else{
                Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show();
            }
        }
    }

  /*  private void launchHomeScreen(){
        logOnPrefManager.setFirstLaunch(false);
        startActivity(new Intent(LogOn.this,MainActivity.class));
        finish();
    }*/

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
    public void ClickMe(View view){
       boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.invidual:
                if (checked)
                    startActivity(new Intent(LogOn.this, IndividualSignUp.class));
                break;
            case R.id.education:
                if (checked)
                    startActivity(new Intent(LogOn.this, EducationalSectorSignUp.class));
                    break;
            case R.id.corporate:
                if (checked)
                    startActivity(new Intent(LogOn.this, CorporateSignUp.class));
                    break;
        }
    }

    public void COR_LOGIN(View view) {
        startActivity(new Intent(this,CorporateSignIn.class));
    }

    public void UNI_LOGIN(View view) {
        startActivity(new Intent(this,CorporateSignIn.class));
    }

    class LoginFetch extends AsyncTask<String,Void,String>{
        HttpURLConnection conn;
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url1 = "http://172.28.172.2:8080/IndianEmploymentCard/iec_login.php";
            String EmailorIEC = strings[0];
            String IEC_Passwprd = strings[1];
            try{
                url = new URL(url1);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setReadTimeout(20*1000);

                OutputStream os= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data = URLEncoder.encode("Email","UTF-8") + "=" + URLEncoder.encode(EmailorIEC,"UTF-8")+"&"+
                URLEncoder.encode("Password","UTF-8")+ "="+ URLEncoder.encode(IEC_Passwprd,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine())!= null) {
                    result.append(line);
                }
                reader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result.toString().trim();

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }catch (ProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return "Login Success...Welcome";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(LogOn.this, "res"+result, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LogOn.this,Tab_Activity.class);
                intent.putExtra(emailice,"Email");
                startActivity(intent);
//                startActivity(new Intent(LogOn.this,Tab_Activity.class));
        }
    }
}