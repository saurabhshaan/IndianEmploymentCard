package in.iecindia.www.indianemploymentcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class UploadPicture extends AppCompatActivity {
    private ImageView imageView;
    private Button button;
    private String Hello;
    private EditText pass,cnfrmpass;
    private String password,Confrmpassword;
    String encodedImage, imgDecodableString;
    final int RESULT_LOAD_IMG = 1;
    private String hscholl,hrollnumber,imedite,irollnumber,gcourse,gstatus,guniversity,grollnumber;
    private String firstName,lastName,middleName,Email,AdharNumber,Addline1,Addline2,Addline3,AddPincode,
            AddState,AddCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);

        pass = findViewById(R.id.password);
        cnfrmpass = findViewById(R.id.cnfrmpassword);
        imageView = findViewById(R.id.imageView4);

    }
    public void upload(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPicture.this);
        View view1 =   getLayoutInflater().inflate(R.layout.choose_pic,null);
        builder.setView(view1);
        AlertDialog dialog = builder.create();
        dialog.show();
        Button camera,gallery;
        camera = view1.findViewById(R.id.camera);
        gallery = view1.findViewById(R.id.gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                galleryIntent.putExtra("crop", "true");
                galleryIntent.putExtra("outputX", 200);
                galleryIntent.putExtra("outputY", 260);
                galleryIntent.putExtra("aspectX", 1);
                galleryIntent.putExtra("aspectY", 1);
                galleryIntent.putExtra("scale", true);
                galleryIntent.putExtra("return-data", true);

                startActivityForResult(galleryIntent,RESULT_LOAD_IMG);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 0) {
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Toast.makeText(this, "Picture saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                        Toast.makeText(UploadPicture.this, "ImageSet", Toast.LENGTH_SHORT).show();
                        imageView.setImageBitmap(thumbnail);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        if (thumbnail != null) {
                            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object//0 for low quality
                        }
                        byte[] b = baos.toByteArray();
                        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        Toast.makeText(UploadPicture.this, "Wait for moment ....", Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, "Activity.RESULT_CANCELLED", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;


                }

            }//onActivityCamera-END
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                if (cursor != null) {
                    cursor.moveToFirst();
                }

                int columnIndex = 0;
                if (cursor != null) {
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                }
                if (cursor != null) {
                    imgDecodableString = cursor.getString(columnIndex);
                }
                if (cursor != null) {
                    cursor.close();
                }
                // Set the Image in ImageView after decoding the String
                imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                //imageUploadSTART

                Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 200, baos); //bm is the bitmap object//0 for low quality
                byte[] b = baos.toByteArray();
                 encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                Toast.makeText(UploadPicture.this, "ImageSet", Toast.LENGTH_SHORT).show();
                Toast.makeText(UploadPicture.this, "Wait for moment ....", Toast.LENGTH_SHORT).show();
                Log.d("error", "images" + encodedImage);
                //close
            }
        } catch (Exception e) {
            Toast.makeText(this, "Problem Detected!", Toast.LENGTH_LONG).show();
        }
    }

    public void FinalSubmit(View view){
//        password varification
        if (pass.getText().toString().length()==0){
            pass.setError("Fill Password");
            return;
        }if (cnfrmpass.getText().toString().length()==0){
            cnfrmpass.setError("Conform your password");
            return;
        }else{
            Toast.makeText(this, "Password OK", Toast.LENGTH_SHORT).show();
        }

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

        hscholl= getIntent().getStringExtra("HighSchool");
        hrollnumber = getIntent().getStringExtra("Hrollnumber");
        imedite = getIntent().getStringExtra("Intermediate");
        irollnumber = getIntent().getStringExtra("InterRollnumber");
        gcourse = getIntent().getStringExtra("Gcourse");
        gstatus = getIntent().getStringExtra("Gstatus");
        guniversity = getIntent().getStringExtra("Guniversity");
        grollnumber = getIntent().getStringExtra("Grollnumber");


        password = pass.getText().toString().trim();
        Confrmpassword = cnfrmpass.getText().toString().trim();

        Hello = "Hii";

        Log.d("ASD",firstName+""+lastName+""+middleName+""+Email+""+AdharNumber+""+Addline1+""+
                Addline2+""+Addline3+""+AddPincode+""+AddState+""+AddCountry+""+hscholl+""+hrollnumber+""+
                imedite+""+irollnumber+""+gcourse+""+gstatus+""+guniversity+""+grollnumber+""+password+""+Confrmpassword);


        Log.d("PIC",password+""+Confrmpassword+""+encodedImage);
        if (isOnline()){
            String method = "SENDDATA";

            SignUpBackground signUpBackground = new SignUpBackground(UploadPicture.this);
            signUpBackground.execute(method,firstName,lastName,middleName,Email,AdharNumber,Addline1,Addline2,Addline3,
                    AddPincode,AddState,AddCountry,hscholl,hrollnumber,imedite,irollnumber,gcourse,gstatus,guniversity,
                    grollnumber,encodedImage,password,Confrmpassword);

        }else {
            Toast.makeText(this, "Connection is Offline ", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
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
            String reg_url = "http://172.28.172.2:8080/IndividualSignUp.php";
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
                String password112 = params[21];
                String cnfrmpassword2 = params[22];

                Log.d("SIT", firstname + "" + lastname+""+middleName+""+Email+""+AdharNumber+""+Addline1+""+Addline2+""
                        +Addline3+""+AddPincode+""+AddState+""+AddCountry+""+hscholl+""+hrollnumber+""+imedite+""+irollnumber+""
                        +gcourse+""+gstatus+""+guniversity+""+grollnumber+""+encodedImage+""+password112+""+cnfrmpassword2);
//                Log.d("TAG",password112);
                try {
                    URL url = new URL(reg_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
//                    httpURLConnection.setRequestProperty("Content-Type", "image/jpeg");
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
                            URLEncoder.encode("Person_Image", "UTF-8") + "=" + URLEncoder.encode(encodedImage, "UTF-8")+"&"+
                            URLEncoder.encode("Password","UTF-8")+"="+URLEncoder.encode(password112,"UTF-8")+"&"+
                           // URLEncoder.encode("Password","UTF-8")+"="+URLEncoder.encode(password112,"UTF-8")+"&"+
                            URLEncoder.encode("Confrm_pass","UTF-8")+"="+URLEncoder.encode(cnfrmpassword2,"UTF-8");
                    Log.d("TAG",URLEncoder.encode("Password","UTF-8")+"="+URLEncoder.encode(password112,"UTF-8"));
                    Log.d("TAG",URLEncoder.encode("Confrm_pass","UTF-8")+"="+URLEncoder.encode(cnfrmpassword2,"UTF-8"));
                    Log.d("TAG", "data parameter set");
                    bufferedWriter.write(OpenConn);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    Log.d("TAG", "buffer writer close");
                    os.close();
                    // get Reponce from server
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    Log.d("TAG",""+bufferedReader.read());
                    Log.d("TAG", "debug");
                    is.close();
                    return "Registration Successfull";
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d("TAG","hello_url");
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