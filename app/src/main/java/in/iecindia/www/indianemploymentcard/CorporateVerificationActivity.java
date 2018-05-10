package in.iecindia.www.indianemploymentcard;

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

public class CorporateVerificationActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private RecyclerView recyclerView;
    private VerificationAdapter mAdapter;
    private SharedPreferences shared;
    private String Eemail;
    String not_verify="No Data Found";
    String verified="Verified";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_verification);

        recyclerView = findViewById(R.id.fishPriceList);

        shared = getSharedPreferences("Cemail",MODE_PRIVATE);
        Eemail = shared.getString("Email", null);
        Log.d("TAG","SS"+Eemail);

        new CorporateVerificationActivity.AsyncFetch().execute(Eemail);

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(Main2Activity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            //   pdLoading.setMessage("\tLoading...");
            // pdLoading.setCancelable(false);
            //pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            Log.d("TAG", "hp befor url call");
            String url1 = "http://172.28.172.2:8080/problemsolver/jsonreplay.php";
            Log.d("TAG", "hp after url call");
            String email= params[0];
            try {

                Log.d("TAG", "open url connection");
                URL url = new URL(url1);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setReadTimeout(20 * 1000);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                //encode data before send it
                //no space permiteted in equals sign
                String data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") ;

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream input = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                input.close();
                httpURLConnection.disconnect();
                // Pass data to onPostExecute method
                return result.toString().trim();


            } catch (MalformedURLException e) {
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

        @Override
        protected void onPostExecute(String result) {
            List<UserData> data=new ArrayList<>();
            //this method will be running on UI thread

            //   pdLoading.dismiss();
            //   List<DataFish> data=new ArrayList<>();

            //  pdLoading.dismiss();
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
                    ob.Company_Name=json_data.getString("Company_Name");
                    ob.Experience_in_Count=json_data.getString("Experience_in_Count");

                    Log.d("Tag",json_data.getString("replaydata")+"  "+json_data.getString("Email"));

                    data.add(ob);

                }
                // Setup and Handover data to recyclerview
                recyclerView = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new VerificationAdapter(CorporateVerificationActivity.this, data);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(CorporateVerificationActivity.this));

            } catch (JSONException e) {
                Toast.makeText(CorporateVerificationActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
            View view=inflater.inflate(R.layout.corporate_individual_request, parent,false);
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
            myHolder.comapny_name.setText(current.Company_Name);
            myHolder.experience.setText(current.Experience_in_Count);
            // load image into imageview using glide


            ((MyHolder) holder).not_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CorporateVerificationActivity.VerifingTask().execute(not_verify);

                }
            });

            ((MyHolder) holder).verified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CorporateVerificationActivity.VerifingTask().execute(verified);
                }
            });
        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView user_name,email,comapny_name,experience;
            public Button not_verify,verified;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                user_name = findViewById(R.id.user_name);
                email = findViewById(R.id.user_email);
                comapny_name = findViewById(R.id.user_company_name);
                experience = findViewById(R.id.user_experience);
                not_verify = findViewById(R.id.not_verify);
                verified = findViewById(R.id.verify);
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
        public String Company_Name;
        public String Experience_in_Count;
    }
    public class VerifingTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String url1="";
            String url2="http://172.28.172.2:8080/IndianEmploymentCard/experience_verify.php";


            String not_verify=strings[0];
            String verified=strings[1];


            try {
                if (not_verify == "No Data Found") {
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
                    String OpnAcc= URLEncoder.encode("No Data Found","UTF-8")+"="+ URLEncoder.encode(not_verify,"UTF-8");

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
                }if (verified=="Verified"){
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
                    String OpnAcc= URLEncoder.encode("Verified","UTF-8")+"="+ URLEncoder.encode(verified,"UTF-8");

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
                    Toast.makeText(CorporateVerificationActivity.this, "Thanks you", Toast.LENGTH_SHORT).show();
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