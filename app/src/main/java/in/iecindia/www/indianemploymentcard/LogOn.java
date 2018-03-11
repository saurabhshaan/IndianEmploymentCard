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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}