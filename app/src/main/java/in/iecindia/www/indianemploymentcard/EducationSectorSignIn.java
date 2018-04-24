package in.iecindia.www.indianemploymentcard;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class EducationSectorSignIn extends AppCompatActivity {

    private EditText email, password;
    private String Eemail,Epassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_sector_sign_in);

        email = findViewById(R.id.EduTrustEmail);
        password = findViewById(R.id.EduTrustpassword);

    }

    public void EDULogin(View view) {
        if (email.getText().toString().length()==0){
            email.setError("required");
            return;
        }if (password.getText().toString().length()==0){
            password.setError("required");
            return;
        }else {
            Toast.makeText(this, "Thanks You", Toast.LENGTH_SHORT).show();
        }

        Eemail = email.getText().toString().trim();
        Epassword = password.getText().toString().trim();

        if (isOnline()) {
            new EducationSectorSignIn.EducationLoginFetch().execute(Eemail, Epassword);
        }else{
            Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show();
        }
    }
    class EducationLoginFetch extends AsyncTask<String,Void,String> {
        HttpURLConnection conn;
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url1 = "http://172.28.172.2:8080/EducationalSectorSignIn.php";
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
            Toast.makeText(EducationSectorSignIn.this, "res"+result, Toast.LENGTH_SHORT).show();

            if (result.equals("success")){
                startActivity(new Intent(EducationSectorSignIn.this,Tab_Activity.class));
            }
            else {
                Toast.makeText(EducationSectorSignIn.this, "Email or Password is Incorrect", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}
