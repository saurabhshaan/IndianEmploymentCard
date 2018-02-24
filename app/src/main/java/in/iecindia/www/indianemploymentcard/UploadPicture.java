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
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class UploadPicture extends AppCompatActivity {
    private ImageView imageView;
    private Button button;
    String encodedImage, imgDecodableString;
    final int RESULT_LOAD_IMG = 1;
    private String hscholl,hrollnumber,imedite,irollnumber,gcourse,gstatus,guniversity,grollnumber;
    private String firstName,lastName,middleName,Email,AdharNumber,Addline1,Addline2,Addline3,AddPincode,
            AddState,AddCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);

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

        Log.d("ASD",firstName+""+lastName+""+middleName+""+Email+""+AdharNumber+""+Addline1+""+
                Addline2+""+Addline3+""+AddPincode+""+AddState+""+AddCountry+""+hscholl+""+hrollnumber+""+imedite+""+irollnumber+""+gcourse+""+gstatus+""+guniversity+""+grollnumber);



        if (isOnline()){
            String method = "SENDDATA";

            SignUpBackground signUpBackground = new SignUpBackground(UploadPicture.this);
            signUpBackground.execute(method,firstName,lastName,middleName,Email,AdharNumber,Addline1,Addline2,Addline3,
                    AddPincode,AddState,AddCountry,hscholl,hrollnumber,imedite,irollnumber,gcourse,gstatus,guniversity,
                    grollnumber,grollnumber, encodedImage);

        }else {
            Toast.makeText(this, "Connection is Offline ", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}