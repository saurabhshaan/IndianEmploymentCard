package in.iecindia.www.indianemploymentcard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Qualification extends AppCompatActivity {
    private EditText Hschool,Hrollnumber, Imediate,Irollnumber,Gcourse,Gstatus,Guniversity,Grollnumber,
    Experience,Exp_in_year,com_email;
    private String hscholl,hrollnumber,imedite,irollnumber,gcourse,gstatus,guniversity,grollnumber,experience,fromdate,
            company_email;
    private String firstName,lastName,middleName,Email,AdharNumber,Addline1,Addline2,Addline3,AddPincode,
            AddState,AddCountry;
    Intent intent;

//    DatePickerDialog.OnDateSetListener from_date, to_date;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification);
        Hschool = findViewById(R.id.hboard);
        Hrollnumber = findViewById(R.id.hrollnumber);
        Imediate = findViewById(R.id.iboard);
        Irollnumber = findViewById(R.id.irollnumber);
        Gcourse = findViewById(R.id.gcourse);
        Gstatus = findViewById(R.id.gcourse);
        Guniversity = findViewById(R.id.guniversity);
        Grollnumber = findViewById(R.id.grollnumber);

        Experience = findViewById(R.id.experience);
        com_email = findViewById(R.id.company_email);
        Exp_in_year = findViewById(R.id.experience_in_year);


        firstName = getIntent().getStringExtra("FirstName");
        lastName = getIntent().getStringExtra("LastName");
        middleName = getIntent().getStringExtra("MiddleName");
        Email = getIntent().getStringExtra("Email");
        AdharNumber = getIntent().getStringExtra("AdhaarNumber");
        Addline1 = getIntent().getStringExtra("Address1");
        Addline2 = getIntent().getStringExtra("Address2");
        Addline3 = getIntent().getStringExtra("Address3");
        AddPincode = getIntent().getStringExtra("Pincode");
        AddState = getIntent().getStringExtra("State");
        AddCountry = getIntent().getStringExtra("Country");

    }


    public void UploadPhoto(View view){
        //validation
        if (Hschool.getText().toString().length() ==0){
            Hschool.setError("Fill Board");
            return;
        }if (Hrollnumber.getText().toString().length()==0){
            Hrollnumber.setError("Fill RollNumber");
            return;
        }if (Imediate.getText().toString().length() == 0){
            Imediate.setError("Fill Board");
            return;
        }if (Irollnumber.getText().toString().length()==0){
            Irollnumber.setError("Fill RollNumber");
            return;
        }if (Gcourse.getText().toString().length()==0){
            Gcourse.setError("Fill Course");
            return;
        }if (Gstatus.getText().toString().length()==0){
            Gstatus.setError("Fill Status");
            return;
        }if (Guniversity.getText().toString().length()==0){
            Guniversity.setError("Fill University");
            return;
        }if (Grollnumber.getText().toString().length()==0){
            Grollnumber.setError("Fill RollNumber");
            return;
        }if (Experience.getText().toString().length()==0){
            Experience.setError("Fill company");
            return;
        }if (com_email.getText().toString().length()==0){
            com_email.setError("Fill email");
            return;
        }if (Exp_in_year.getText().toString().length()==0){
            Exp_in_year.setError("Fill date");
            return;
        } else{
            Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show();
        }
        // validation ends here
        hscholl = Hschool.getText().toString().trim();
        hrollnumber = Hrollnumber.getText().toString().trim();
        imedite = Imediate.getText().toString().trim();
        irollnumber = Irollnumber.getText().toString().trim();
        gcourse = Gcourse.getText().toString().trim();
        gstatus = Gstatus.getText().toString().trim();
        guniversity = Guniversity.getText().toString().trim();
        grollnumber = Grollnumber.getText().toString().trim();
        company_email = com_email.getText().toString().trim();
        experience = Experience.getText().toString().trim();
        fromdate = Exp_in_year.getText().toString().trim();


        Intent intent = new Intent(Qualification.this,UploadPicture.class);
        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);
        intent.putExtra("MiddleName", middleName);
        intent.putExtra("Email", Email);
        intent.putExtra("AdhaarNumber", AdharNumber);
        intent.putExtra("Address1", Addline1);
        intent.putExtra("Address2", Addline2);
        intent.putExtra("Address3", Addline3);
        intent.putExtra("Pincode", AddPincode);
        intent.putExtra("State", AddState);
        intent.putExtra("Country", AddCountry);
        intent.putExtra("HighSchool",hscholl);
        intent.putExtra("Hrollnumber",hrollnumber);
        intent.putExtra("Intermediate",imedite);
        intent.putExtra("InterRollnumber",irollnumber);
        intent.putExtra("Gcourse",gcourse);
        intent.putExtra("Gstatus",gstatus);
        intent.putExtra("Guniversity",guniversity);
        intent.putExtra("Grollnumber",grollnumber);
        intent.putExtra("Experience",experience);
        intent.putExtra("Company_Email",company_email);
        intent.putExtra("FromDate",fromdate);
        startActivity(intent);
    }
}