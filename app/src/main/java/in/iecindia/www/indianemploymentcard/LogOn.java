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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

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

public class LogOn extends AppCompatActivity{
    private LogOnPrefManager logOnPrefManager;
    private EditText emailoriec,mpassword;
    private String emailice, pass;
    private Button signinbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_on);

        emailoriec = findViewById(R.id.EmailorIEC);
        mpassword = findViewById(R.id.password);

        logOnPrefManager = new LogOnPrefManager(this);
        if (!logOnPrefManager.IsLogOnFirstTimeLaunch()){

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
            }
        }
    }
    private void launchHomeScreen(){
        logOnPrefManager.setFirstLaunch(false);
        startActivity(new Intent(LogOn.this,MainActivity.class));
        finish();
    }

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

    class LoginFetch extends AsyncTask<String,Void,String>{
        HttpURLConnection conn;
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url1 = "";
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}