package in.iecindia.www.indianemploymentcard;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class IEC_card extends android.support.v4.app.Fragment {

    String EmailIEC;

    private RecyclerView recyclerView;
    IECAdapter mAdapter;


    public IEC_card() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EmailIEC = getActivity().getIntent().getStringExtra("Email");
        return inflater.inflate(R.layout.activity_iec_card, container, false);
    }
    class IEC_Card_Fetch extends AsyncTask<String,Void,String> {
        HttpURLConnection conn;
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url1 = "http://172.28.172.2:8080/IndianEmploymentCard/iec_card_json.php";
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
                    fishData.iec_first_name = json_data.getString("FirstName");
                    fishData.iec_last_name = json_data.getString("LastName");
                    fishData.iec_middle_name = json_data.getString("MiddleName");
                    fishData.iec_adhaar_number = json_data.getString("AdhaarNumber");
                    fishData.iec_number = json_data.getString("IEC_Number");


                    Log.d("TAG","Server result"+""+json_data.getString("image"));
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                recyclerView = getView().findViewById(R.id.fishPriceList);
                Log.d("TAG", "recycle!!");
                //  Toast.makeText(MainActivity.this, "data pass to adapter" + data, Toast.LENGTH_SHORT).show();
                mAdapter = new IECAdapter(getActivity(), data);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            } catch (JSONException e) {
//                Toast.makeText(News.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
    public class IECAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private LayoutInflater inflater;
        List<DataFish> data= Collections.emptyList();
        DataFish current;
        int currentPos=0;
        // create constructor to innitilize context and data sent from MainActivity
        public IECAdapter(Context context, List<DataFish> data){
            this.context=context;
            inflater= LayoutInflater.from(context);
            this.data=data;
        }
        // Inflate the layout when viewholder created
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=inflater.inflate(R.layout.card_view_activity, parent,false);
            MyHolder holder=new IECAdapter.MyHolder(view);
            return holder;
        }

        // Bind data
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            IECAdapter.MyHolder myHolder= (IECAdapter.MyHolder) holder;
            current=data.get(position);

            myHolder.fistname.setText(current.iec_first_name+" "+current.iec_middle_name+" "+current.iec_last_name);

            myHolder.adharnumber.setText(current.iec_adhaar_number);
            myHolder.iec_nimber.setText(current.iec_number);

        }
        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView fistname,adharnumber,iec_nimber;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);

                fistname= itemView.findViewById(R.id.iec_card_full_name);

                adharnumber = itemView.findViewById(R.id.iec_card_adhaar_number);

                iec_nimber = itemView.findViewById(R.id.iec_number);
            }

            @Override
            public void onClick(View v) {

            }
        }
    }

    public class DataFish {
        public String iec_first_name;
        public String iec_last_name;
        public String iec_middle_name;
        public String iec_adhaar_number;
        public String iec_number;
    }
}