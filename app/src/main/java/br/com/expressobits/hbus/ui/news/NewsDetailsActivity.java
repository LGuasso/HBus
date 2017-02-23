package br.com.expressobits.hbus.ui.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.util.TimeUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;

public class NewsDetailsActivity extends AppCompatActivity {

    public static final String ARGS_NEWS_ID = "br.com.expressobits.hbus.ui.news.NewsIdKey";
    private Toolbar toolbar;
    private TextView textViewBody;
    private ImageView imageView;
    private TextView textViewSource;
    private TextView textViewTime;
    private TextView textViewTitle;
    private TextView textViewSubtitle;
    private LinearLayout linearLayoutImages;
    private LinearLayout linearLayoutNewsChips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        initViews();
        String id = getIntent().getStringExtra(ARGS_NEWS_ID);
        pullNews(id);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        imageView = (ImageView) findViewById(R.id.imageView);
        textViewTitle = (TextView) findViewById(R.id.textViewNewsTitle);
        textViewSubtitle = (TextView) findViewById(R.id.textViewNewsSubtitle);
        textViewBody = (TextView) findViewById(R.id.textViewNewsBody);
        textViewTime = (TextView) findViewById(R.id.textViewNewsTime);
        textViewSource = (TextView) findViewById(R.id.textViewNewsSource);
        linearLayoutNewsChips = (LinearLayout) findViewById(R.id.linearLayoutNewsChips);
        linearLayoutImages = (LinearLayout) findViewById(R.id.linearLayoutImages);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Puxa do firebase os dados para retornar a notícia online!
     *
     * @param id ex;
     */
    private void pullNews(final String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News news = dataSnapshot.getValue(News.class);
                if (news != null) {
                    news.setId(id);
                    loadNews(news);
                } else {
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
     *
     * @param news News of activity
     */
    private void loadNews(News news) {

        List<String> urlsActivedImages = new ArrayList<>();
        toolbar.setTitle(getString(R.string.news));
        Picasso.with(this).load(news.getImagesUrls().get(0)).into(imageView);
        textViewTitle.setText(news.getTitle());

        String body = news.getBody();
        for (int i = 0; i < news.getImagesUrls().size(); i++) {
            String url = news.getImagesUrls().get(i);
            if (news.getBody().contains("--" + FirebaseUtils.NEWS_BODY_IMAGE_TAG + i + "--")) {
                urlsActivedImages.add(url);
                body = news.getBody().replace("--" + FirebaseUtils.NEWS_BODY_IMAGE_TAG + i + "--", "");
            }
        }
        textViewBody.setText(body);
        textViewSource.setText(news.getSource());
        textViewTime.setText(TimeUtils.getTimeAgo(news.getTime(), this));
        textViewSubtitle.setText(news.getSubtitle());
        getImageList(urlsActivedImages);
        updateNewsChips(news);
    }

    private void getImageList(List<String> urlsActivedImages) {

        View view;
        view = getLayoutInflater().inflate(R.layout.item_news_image, linearLayoutImages, false);
        for (final String url : urlsActivedImages) {
            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewImage);
            imageView.setOnClickListener(view1 -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            });
            linearLayoutImages.addView(imageView);
            Picasso.with(this).load(url).into(imageView);
        }

    }

    private void updateNewsChips(News news) {
        linearLayoutNewsChips.removeAllViews();
        View viewCity;
        viewCity = getLayoutInflater().inflate(R.layout.item_news_chips, linearLayoutNewsChips, false);
        TextView textViewCity = (TextView) viewCity.findViewById(R.id.textViewNewsChip);
        String city = FirebaseUtils.getNewsCityName(news.getId());
        if (city != null) {

            textViewCity.setText(city);
        } else {
            textViewCity.setText(this.getString(R.string.pref_header_general));
        }
        linearLayoutNewsChips.addView(viewCity);
        List<String> itinerariesIDs = news.getItineraryIds();
        if (itinerariesIDs != null) {
            for (String itineraryId : itinerariesIDs) {
                View view;
                view = getLayoutInflater().inflate(R.layout.item_news_chips, linearLayoutNewsChips, false);
                TextView textView = (TextView) view.findViewById(R.id.textViewNewsChip);
                String itineraryName = FirebaseUtils.getNewsItinerary(itineraryId);
                if (city != null) {


                    textView.setText(itineraryName);
                    textView.setSelected(true);
                    linearLayoutNewsChips.addView(view);

                }

            }
        }

    }
}
