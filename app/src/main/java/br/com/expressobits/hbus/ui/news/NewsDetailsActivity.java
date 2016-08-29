package br.com.expressobits.hbus.ui.news;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.util.TimeUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;

public class NewsDetailsActivity extends AppCompatActivity {

    private static final String TAG = "NewsDetails";
    public static final String ARGS_NEWS_ID = "br.com.expressobits.hbus.ui.news.NewsIdKey";
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView textViewBody;
    private ImageView imageView;
    private TextView textViewSource;
    private TextView textViewTime;
    private TextView textViewTitle;
    private LinearLayout linearLayoutNewsChips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        initViews();
        String id = getIntent().getStringExtra(ARGS_NEWS_ID);
        pullNews(id);
    }

    private void initViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = (ImageView) findViewById(R.id.imageView);
        textViewTitle = (TextView) findViewById(R.id.textViewNewsTitle);
        textViewBody = (TextView) findViewById(R.id.textViewNewsBody);
        textViewTime = (TextView) findViewById(R.id.textViewNewsTime);
        textViewSource = (TextView) findViewById(R.id.textViewNewsSource);
        linearLayoutNewsChips = (LinearLayout) findViewById(R.id.linearLayoutNewsChips);
    }

    /**
     * Puxa do firebase os dados para retornar a notícia online!
     * @param id ex;
     */
    private void pullNews(final String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News news = dataSnapshot.getValue(News.class);
                news.setId(id);
                if(news!=null){
                    loadNews(news);
                }else {
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Carrega uma notícia aos views
     * @param news
     */
    private void loadNews(News news) {
        toolbar.setTitle(getString(R.string.news));
        Picasso.with(this).load(news.getImagesUrls().get(0)).into(imageView);
        textViewTitle.setText(news.getTitle());
        textViewBody.setText(news.getBody());
        textViewSource.setText(news.getSource());
        textViewTime.setText(TimeUtils.getTimeAgo(news.getTime(),this));
        updateNewsChips(news);
    }

    private void updateNewsChips(News news){
        linearLayoutNewsChips.removeAllViews();
        View viewCity = getLayoutInflater().inflate(R.layout.item_news_chips,linearLayoutNewsChips,false);
        TextView textViewCity = (TextView) viewCity.findViewById(R.id.textViewNewsChip);
        String city = FirebaseUtils.getNewsCityName(news.getId());
        if(city!=null){

            textViewCity.setText(city);
        }else {
            textViewCity.setText(this.getString(R.string.pref_header_general));
        }
        linearLayoutNewsChips.addView(viewCity);
        List<String> itinerariesIDs = news.getItineraryIds();
        if(itinerariesIDs!=null){
            for (String itineraryId:itinerariesIDs){
                View view = getLayoutInflater().inflate(R.layout.item_news_chips,linearLayoutNewsChips,false);
                TextView textView = (TextView) view.findViewById(R.id.textViewNewsChip);
                String itineraryName = FirebaseUtils.getNewsItinerary(itineraryId);
                if(city!=null){


                    textView.setText(itineraryName);
                    textView.setSelected(true);
                    linearLayoutNewsChips.addView(view);

                }else {
                    //textView.setText(context.getString(R.string.pref_header_general));
                }

            }
        }

    }
}