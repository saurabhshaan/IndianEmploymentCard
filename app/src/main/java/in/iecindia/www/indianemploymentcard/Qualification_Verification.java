package in.iecindia.www.indianemploymentcard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.Collections;
import java.util.List;

public class Qualification_Verification extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerificationAdapter mAdapter;
    private SharedPreferences shared;
    private String Qemail;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String hs_not_verify="No Data Found";
    String hs_verified="Verified";
    String im_not_verify="No Data Found";
    String im_verified="Verified";
    String gr_not_verify="No Data Found";
    String gr_verified="Verified";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification__verification);


        shared = getSharedPreferences("Cemail",MODE_PRIVATE);
        Qemail = shared.getString("Email", null);
        Log.d("TAG","SS"+Qemail);

        new Qualification_Verification.AsyncFetch().execute();

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Qualification_Verification.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://172.28.172:8080/IndianEmploymentCard/IndividualsQualificationRequest.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            List<UserData> data=new ArrayList<>();
            //this method will be running on UI thread
               pdLoading.dismiss();

            Log.d("TAG",result);
            try {
                Log.d("TAG",result);
                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){

                    JSONObject json_data = jArray.getJSONObject(i);
                    UserData ob=new UserData();

                    ob.FirstName=json_data.getString("FirstName");
                    ob.LastName=json_data.getString("LastName");
                    ob.MiddleName=json_data.getString("MiddleName");
                    ob.Email= json_data.getString("Email");
                    ob.HighSchoolBoard=json_data.getString("HighSchoolBoard");
                    ob.HighSchoolRollNumber=json_data.getString("HighSchoolRollNumber");
                    ob.IntermediateBoard=json_data.getString("IntermediateBoard");
                    ob.IntermediateRollNumber=json_data.getString("IntermediateRollNumber");
                    ob.GraduationCourse=json_data.getString("GraduationCourse");
                    ob.GraduationStatus=json_data.getString("GraduationRollNumber");
                    ob.GraduationUniversity=json_data.getString("GraduationUniversity");
                    ob.GraduationRollNumber=json_data.getString("GraduationRollNumber");

                    Log.d("Tag",json_data.getString("replaydata")+"  "+json_data.getString("Email"));

                    data.add(ob);

                }
                // Setup and Handover data to recyclerview
                recyclerView = findViewById(R.id.quali_verification);
                mAdapter = new VerificationAdapter(Qualification_Verification.this, data);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Qualification_Verification.this));

            } catch (JSONException e) {
                Toast.makeText(Qualification_Verification.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

    public class VerificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private LayoutInflater inflater;
        List<UserData> data= Collections.emptyList();
        UserData current;
        int currentPos=0;
        // create constructor to innitilize context and data sent from MainActivity
        public VerificationAdapter(Context context, List<UserData> data){
            this.context=context;
            inflater= LayoutInflater.from(context);
            this.data=data;
        }
        // Inflate the layout when viewholder created
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=inflater.inflate(R.layout.qualification_individual_request, parent,false);
            MyHolder holder=new MyHolder(view);
            return holder;
        }

        // Bind data
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            MyHolder myHolder= (MyHolder) holder;
            current=data.get(position);
            myHolder.user_name.setText(current.FirstName+" "+current.LastName+" "+current.MiddleName);
            myHolder.email.setText(current.Email);
            myHolder.hs_board_name.setText(current.HighSchoolBoard);
            myHolder.hs_roll_number.setText(current.HighSchoolRollNumber);

            myHolder.im_board_name.setText(current.IntermediateBoard);
            myHolder.im_roll_number.setText(current.IntermediateRollNumber);

            myHolder.gr_board_name.setText(current.GraduationUniversity);
            myHolder.gr_course_name.setText(current.GraduationCourse);
            myHolder.gr_roll_number.setText(current.GraduationRollNumber);
            // load image into imageview using glide


            ((MyHolder) holder).hs_not_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Qualification_Verification.VerifingTask().execute(hs_not_verify);
                }
            });

            ((MyHolder) holder).hs_verified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Qualification_Verification.VerifingTask().execute(hs_verified);
                }
            });

            ((MyHolder) holder).im_not_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Qualification_Verification.VerifingTask().execute(im_not_verify);} });

            ((MyHolder) holder).im_verified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Qualification_Verification.VerifingTask().execute(im_verified);} });

            ((MyHolder) holder).gr_not_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Qualification_Verification.VerifingTask().execute(gr_not_verify);
                }
            });

            ((MyHolder) holder).gr_verified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Qualification_Verification.VerifingTask().execute(gr_verified);
                }
            });
        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView user_name,email,hs_board_name,hs_roll_number,im_board_name,im_roll_number,gr_board_name,gr_course_name,gr_roll_number;
            private Button hs_not_verify,hs_verified,im_not_verify,im_verified,gr_not_verify,gr_verified;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                user_name = findViewById(R.id.user_name);
                email = findViewById(R.id.user_email);
                hs_board_name = findViewById(R.id.hs_board_name);
                hs_roll_number = findViewById(R.id.hs_roll_number);
                hs_not_verify = findViewById(R.id.hs_not_verify);
                hs_verified = findViewById(R.id.hs_verify);

                im_board_name = findViewById(R.id.im_board_name);
                im_roll_number = findViewById(R.id.im_roll_number);
                im_not_verify = findViewById(R.id.im_not_verify);
                im_verified = findViewById(R.id.im_verify);

                gr_board_name = findViewById(R.id.gr_board_name);
                gr_roll_number = findViewById(R.id.gr_roll_number);
                gr_course_name = findViewById(R.id.gr_course_name);
                gr_not_verify = findViewById(R.id.gr_not_verify);
                gr_verified = findViewById(R.id.gr_verify);
            }
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
            }
        }
    }
    public class UserData {
        public String FirstName;
        public String LastName;
        public String MiddleName;
        public String Email;
        public String HighSchoolBoard;
        public String HighSchoolRollNumber;
        public String IntermediateBoard;
        public String IntermediateRollNumber;
        public String GraduationCourse;
        public String GraduationStatus;
        public String GraduationUniversity;
        public String GraduationRollNumber;
    }

    public class VerifingTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String url1="";
            String url2="http://172.28.172:8080/IndianEmploymentCard/highschool_verification.php";
            String url3="";
            String url4="http://172.28.172:8080/IndianEmploymentCard/intermediate_verification.php";
            String url5="";
            String url6="http://172.28.172:8080/IndianEmploymentCard/graduation_verification.php";

            String hs_not_verify=strings[0];
            String hs_verified=strings[1];
            String im_not_verify=strings[2];
            String im_verified=strings[3];
            String gr_not_verify=strings[4];
            String gr_verified=strings[5];

            try {
                if (hs_not_verify == "No Data Found") {
                    URL url=new URL(url1);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.d("TAG", "open url connnection ");
                    OutputStream os=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    Log.d("TAG","buffered writer");
                    //encode data before send it
                    //no space permiteted in equals sign
                    String OpnAcc= URLEncoder.encode("No Data Found","UTF-8")+"="+ URLEncoder.encode(hs_not_verify,"UTF-8");

                    Log.d("TAG", "data parameter set ");
                    bufferedWriter.write(OpnAcc);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    Log.d("TAG", "buffer writer close");
                    os.close();
                    //get response from server
                    InputStream is=httpURLConnection.getInputStream();
                    Log.d("TAG", "debug");
                    is.close();
                    return "Success";
                }if (hs_verified=="Verified"){
                    URL url=new URL(url2);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.d("TAG", "open url connnection ");
                    OutputStream os=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    Log.d("TAG","buffered writer");
                    //encode data before send it
                    //no space permiteted in equals sign
                    String OpnAcc= URLEncoder.encode("Verified","UTF-8")+"="+ URLEncoder.encode(hs_verified,"UTF-8");

                    Log.d("TAG", "data parameter set ");
                    bufferedWriter.write(OpnAcc);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    Log.d("TAG", "buffer writer close");
                    os.close();
                    //get response from server
                    InputStream is=httpURLConnection.getInputStream();
                    Log.d("TAG", "debug");
                    is.close();
                    return "Success";
                }if (im_not_verify=="No Data Found"){
                    URL url=new URL(url3);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.d("TAG", "open url connnection ");
                    OutputStream os=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    Log.d("TAG","buffered writer");
                    //encode data before send it
                    //no space permiteted in equals sign
                    String OpnAcc= URLEncoder.encode("No Data Found","UTF-8")+"="+ URLEncoder.encode(im_not_verify,"UTF-8");

                    Log.d("TAG", "data parameter set ");
                    bufferedWriter.write(OpnAcc);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    Log.d("TAG", "buffer writer close");
                    os.close();
                    //get response from server
                    InputStream is=httpURLConnection.getInputStream();
                    Log.d("TAG", "debug");
                    is.close();
                    return "Success";
                }if (im_verified=="Verified"){
                    URL url=new URL(url4);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.d("TAG", "open url connnection ");
                    OutputStream os=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    Log.d("TAG","buffered writer");
                    //encode data before send it
                    //no space permiteted in equals sign
                    String OpnAcc= URLEncoder.encode("Verified","UTF-8")+"="+ URLEncoder.encode(im_verified,"UTF-8");

                    Log.d("TAG", "data parameter set ");
                    bufferedWriter.write(OpnAcc);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    Log.d("TAG", "buffer writer close");
                    os.close();
                    //get response from server
                    InputStream is=httpURLConnection.getInputStream();
                    Log.d("TAG", "debug");
                    is.close();
                    return "Success";
                }if (gr_not_verify=="No Data Found"){
                    URL url=new URL(url5);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.d("TAG", "open url connnection ");
                    OutputStream os=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    Log.d("TAG","buffered writer");
                    //encode data before send it
                    //no space permiteted in equals sign
                    String OpnAcc= URLEncoder.encode("No Data Found","UTF-8")+"="+ URLEncoder.encode(gr_not_verify,"UTF-8");

                    Log.d("TAG", "data parameter set ");
                    bufferedWriter.write(OpnAcc);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    Log.d("TAG", "buffer writer close");
                    os.close();
                    //get response from server
                    InputStream is=httpURLConnection.getInputStream();
                    Log.d("TAG", "debug");
                    is.close();
                    return "Success";
                }if (gr_verified=="Verified"){
                    URL url=new URL(url6);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.d("TAG", "open url connnection ");
                    OutputStream os=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    Log.d("TAG","buffered writer");
                    //encode data before send it
                    //no space permiteted in equals sign
                    String OpnAcc= URLEncoder.encode("Verified","UTF-8")+"="+ URLEncoder.encode(gr_verified,"UTF-8");

                    Log.d("TAG", "data parameter set ");
                    bufferedWriter.write(OpnAcc);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    Log.d("TAG", "buffer writer close");
                    os.close();
                    //get response from server
                    InputStream is=httpURLConnection.getInputStream();
                    Log.d("TAG", "debug");
                    is.close();
                    return "Success";
                }else {
                    Toast.makeText(Qualification_Verification.this, "Thanks you", Toast.LENGTH_SHORT).show();
                }
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}