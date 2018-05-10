package in.iecindia.www.indianemploymentcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsDetails extends AppCompatActivity {

    private TextView newstitle, newssource, newsdescription;
    private ImageView newsimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        newsimage = findViewById(R.id.newsimage);
        newstitle = findViewById(R.id.newstitle);
        newssource = findViewById(R.id.newssource);
        newsdescription = findViewById(R.id.newsdescription);

        Picasso.with(this).load(getIntent().getStringExtra("image")).resize(80,80).into(newsimage);
        newstitle.setText(getIntent().getStringExtra("title"));
        newssource.setText(getIntent().getStringExtra("newssource"));
        newsdescription.setText(getIntent().getStringExtra("description"));

    }
}
