package in.iecindia.www.indianemploymentcard;


import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class News extends android.support.v4.app.Fragment {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private RecyclerView recyclerView;
    NewsAdapter mAdapter;

    public News() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new News.AsyncFetch().execute();
        return inflater.inflate(R.layout.activity_news, container, false);


    }

    class AsyncFetch extends AsyncTask<String, String, String> {
//        ProgressDialog pdLoading = new ProgressDialog(Tab_Activity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://172.28.172.2:8080/IndianEmploymentCard/newsapi.php");

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
                conn.setRequestMethod("GET");

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
            //this method will be running on UI thread
            //   Toast.makeText(MainActivity.this, "result"+result, Toast.LENGTH_SHORT).show();
//            pdLoading.dismiss();
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
                    Log.d("TAG", json_data.getString("title"));
                    Log.d("TAG", json_data.getString("newssource"));
                    Log.d("TAG", json_data.getString("description"));
                    Log.d("TAG", json_data.getString("image"));

                    //Log.d("TAG",json_data.getString("Image"));

                 /*   Toast.makeText(MainActivity.this, "Name"+json_data.getString("Name"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Email"+json_data.getString("Email"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Mobile"+json_data.getString("Mobile"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Query"+json_data.getString("Query"), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "Image"+json_data.getString("Image"), Toast.LENGTH_SHORT).show();
                   */
                    fishData.mtitle = json_data.getString("title");
                    fishData.newssource = json_data.getString("newssource");
                    fishData.description = json_data.getString("description");
                    fishData.image = json_data.getString("image");

                    Log.d("TAG","Server result"+""+json_data.getString("image"));
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                recyclerView = getView().findViewById(R.id.fishPriceList);
                Log.d("TAG", "recycle!!");
                //  Toast.makeText(MainActivity.this, "data pass to adapter" + data, Toast.LENGTH_SHORT).show();
                mAdapter = new NewsAdapter(getActivity(), data);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            } catch (JSONException e) {
//                Toast.makeText(News.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private LayoutInflater inflater;
        List<DataFish> data= Collections.emptyList();
        DataFish current;
        int currentPos=0;
        // create constructor to innitilize context and data sent from MainActivity
        public NewsAdapter(Context context, List<DataFish> data){
            this.context=context;
            inflater= LayoutInflater.from(context);
            this.data=data;
        }
        // Inflate the layout when viewholder created
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=inflater.inflate(R.layout.news_row_data, parent,false);
            MyHolder holder=new MyHolder(view);
            return holder;
        }

        // Bind data
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            MyHolder myHolder= (MyHolder) holder;
            current=data.get(position);

            Picasso.with(context).load(current.image).into(myHolder.imageView);
            myHolder.title.setText(current.mtitle);
            myHolder.description.setText(current.description);
            myHolder.nsource.setText(current.newssource);
            // load image into imageview using glide

//        ((MyHolder) holder).onClick(new View.OnClickListener());
            ((MyHolder) holder).readmore.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),NewsDetails.class);
                    intent.putExtra("image",current.image);
                    intent.putExtra("title",current.mtitle);
                    intent.putExtra("newssource",current.newssource);
                    // Log.d("TAG","name"+current.UserName);
                    intent.putExtra("description",current.description);

                    //  Toast.makeText(context, "check value"+current.UserEmail+" "+current.UserMobile, Toast.LENGTH_SHORT).show();

                    v.getContext().startActivity(intent);
                }
            });
        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            ImageView imageView;

            TextView title,description,nsource,readmore;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.galleryImage);
                title= itemView.findViewById(R.id.title);
                description = itemView.findViewById(R.id.sdetails);
                nsource = itemView.findViewById(R.id.news_source);
                readmore = itemView.findViewById(R.id.ReadMore);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

            }
        }
    }

}