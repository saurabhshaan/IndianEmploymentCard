package in.iecindia.www.indianemploymentcard;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class IndividualSignUp extends AppCompatActivity {
    private EditText fname, lname, mname, email, adhaarNumber,
            addLine1, addLine2, addLine3 , addPincode, addState, addCountry;
    private String finame, laName, miName, eMail, adhNumber, line1, line2, line3,addPin,addSta,addCou;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fname = findViewById(R.id.firstname);
        lname = findViewById(R.id.lastname);
        mname = findViewById(R.id.middlename);
        email = findViewById(R.id.email);
        adhaarNumber = findViewById(R.id.adhaarNumber);
        addLine1 = findViewById(R.id.addLine1);
        addLine2 = findViewById(R.id.addLine2);
        addLine3 = findViewById(R.id.addLine3);
        addPincode = findViewById(R.id.addpin);
        addState = findViewById(R.id.addState);
        addCountry  = findViewById(R.id.addCountry);

        mname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mname.setText(" ");
            }
        });
        addLine3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLine3.setText(" ");
            }
        });
    }
    public void GoForQualification(View view){
        // validation
        if (fname.getText().toString().length() == 0){
            fname.setError("Fill First Name");
            return;
        }if (lname.getText().toString().length() ==0){
            lname.setError("Fill Last Name");
            return;
        }if (email.getText().toString().length() ==0){
            email.setError("Fill email");
            return;
        }if (adhaarNumber.getText().toString().length() == 0){
            adhaarNumber.setError("Fill Adhaar Number");
            return;
        }if (addLine1.getText().toString().length() == 0){
            addLine1.setError("Fill Address");
            return;
        }if (addLine2.getText().toString().length() ==0){
            addLine2.setError("Fill Address");
            return;
        }if (addPincode.getText().toString().length()==0){
            addPincode.setError("Fill Pincode");
            return;
        }if (addState.getText().toString().length()==0){
            addState.setError("Fill State");
            return;
        }if (addCountry.getText().toString().length()==0){
            addCountry.setError("Fill Country");
        }else {
            Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show();
        }
        // validation ends here
        finame = fname.getText().toString().trim();
        laName = lname.getText().toString().trim();
        miName = mname.getText().toString().trim();
        eMail = email.getText().toString().trim();
        adhNumber = adhaarNumber.getText().toString().trim();
        line1 = addLine1.getText().toString().trim();
        line2 = addLine2.getText().toString().trim();
        line3 = addLine3.getText().toString().trim();
        addPin = addPincode.getText().toString().trim();
        addSta = addState.getText().toString().trim();
        addCou = addCountry.getText().toString().trim();

        if (isOnline()) {
            Intent intent = new Intent(IndividualSignUp.this, Qualification.class);
            intent.putExtra("FirstName", finame);
            intent.putExtra("LastName", laName);
            intent.putExtra("MiddleName", miName);
            intent.putExtra("Email", eMail);
            intent.putExtra("AdhaarNumber", adhNumber);
            intent.putExtra("Address1", line1);
            intent.putExtra("Address2", line2);
            intent.putExtra("Address3", line3);
            intent.putExtra("Pincode", addPin);
            intent.putExtra("State", addSta);
            intent.putExtra("Country", addCou);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Please Connect to network", Toast.LENGTH_SHORT).show();
        }

    }
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

}
