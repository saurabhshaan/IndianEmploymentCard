package in.iecindia.www.indianemploymentcard;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class Profile extends android.support.v4.app.Fragment {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private RecyclerView recyclerView;
    ProfileAdapter mAdapter;

    String EmailIEC;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        EmailIEC = getActivity().getIntent().getStringExtra("Email");
        new Profile.LoginFetch().execute(EmailIEC);

        return inflater.inflate(R.layout.activity_profile, container, false);
    }
    public void Add_More_Details(View view){
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
            String url1 = "http://172.28.172.2:8080/IndianEmploymentCard/profilejson.php";
            String EmailorIEC = strings[0];
            try{
                url = new URL(url1);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setReadTimeout(20*1000);

                OutputStream os= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data = URLEncoder.encode("Email","UTF-8") + "=" + URLEncoder.encode(EmailorIEC,"UTF-8");

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
            return "Welcome";
        }

        @Override
        protected void onPostExecute(String result) {
            List<DataFish> data = new ArrayList<>();

//            pdLoading.dismiss();
            try {
                Log.d("TAG", result);
                JSONArray jArray = new JSONArray(result);
                //  Toast.makeText(MainActivity.this, "json result" + result, Toast.LENGTH_SHORT).show();
                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject json_data = jArray.getJSONObject(i);
                    DataFish fishData = new DataFish();
                    /*Log.d("TAG", json_data.getString("title"));
                    Log.d("TAG", json_data.getString("newssource"));
                    Log.d("TAG", json_data.getString("description"));
                    Log.d("TAG", json_data.getString("image"));*/

                    //Log.d("TAG",json_data.getString("Image"));

                 /*   Toast.makeText(MainActivity.this, "Name"+json_data.getString("Name"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Email"+json_data.getString("Email"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Mobile"+json_data.getString("Mobile"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Query"+json_data.getString("Query"), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "Image"+json_data.getString("Image"), Toast.LENGTH_SHORT).show();
                   */
                    fishData.img = json_data.getString("galleryImage");
                    fishData.fi = json_data.getString("FirstName");
                    fishData.last = json_data.getString("LastName");
                    fishData.middle = json_data.getString("MiddleName");
                    fishData.memail = json_data.getString("Email");
                    fishData.adhar = json_data.getString("AdhaarNumber");
                    fishData.hs =json_data.getString("hs_verification_status");
                    fishData.im=json_data.getString("im_verification_status");
                    fishData.gr=json_data.getString("gr_verification_status");
                    fishData.exp=json_data.getString("Experience_Status");

                    Log.d("TAG","Server result"+""+json_data.getString("image"));
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                recyclerView = getView().findViewById(R.id.fishPriceList);
                Log.d("TAG", "recycle!!");
                //  Toast.makeText(MainActivity.this, "data pass to adapter" + data, Toast.LENGTH_SHORT).show();
                mAdapter = new ProfileAdapter(getActivity(), data);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            } catch (JSONException e) {
//                Toast.makeText(News.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
    public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private LayoutInflater inflater;
        List<DataFish> data= Collections.emptyList();
        DataFish current;
        int currentPos=0;
        // create constructor to innitilize context and data sent from MainActivity
        public ProfileAdapter(Context context, List<DataFish> data){
            this.context=context;
            inflater= LayoutInflater.from(context);
            this.data=data;
        }
        // Inflate the layout when viewholder created
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=inflater.inflate(R.layout.user_profile_data, parent,false);
            MyHolder holder=new MyHolder(view);
            return holder;
        }

        // Bind data
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            MyHolder myHolder= (MyHolder) holder;
            current=data.get(position);

            Picasso.with(context).load(current.img).into(myHolder.imageView);
            myHolder.fistname.setText(current.fi+" "+current.middle+" "+current.last);
            myHolder.email_from.setText(current.memail);
            myHolder.adharnumber.setText(current.adhar);
            myHolder.hs.setText(current.hs);
            myHolder.im.setText(current.im);
            myHolder.gr.setText(current.gr);
            myHolder.exp.setText(current.exp);
        }
        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            ImageView imageView;

            TextView fistname,email_from,adharnumber,hs,im,gr,exp;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.galleryImage);
                fistname= itemView.findViewById(R.id.full_name);
                email_from = itemView.findViewById(R.id.profile_email);
                adharnumber = itemView.findViewById(R.id.adhaar_number);
                hs = itemView.findViewById(R.id.hs_verification_status);
                im = itemView.findViewById(R.id.im_verification_status);
                gr = itemView.findViewById(R.id.gr_verification_status);
                exp = itemView.findViewById(R.id.experience_status);
            }

            @Override
            public void onClick(View v) {

            }
        }
    }

    public class DataFish {
        public String img;
        public String fi;
        public String last;
        public String middle;
        public String memail;
        public String adhar;
        public String hs;
        public String im;
        public String gr;
        public String exp;
    }
}