package br.com.expressobits.hbusgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.utils.FirebaseUtils;

public class NewsListActivity extends AppCompatActivity  implements View.OnClickListener,ChildEventListener,AdapterView.OnItemClickListener{

    private List<News> newses = new ArrayList<>();

    private ImageButton imageButton;
    private EditText editText;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(this);

        imageButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        final String cityId = editText.getText().toString();
        if(cityId.isEmpty()){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference newsRef = database.getReference().child(FirebaseUtils.NEWS_TABLE);
            DatabaseReference generalRef = newsRef.child(FirebaseUtils.GENERAL);
            generalRef.addChildEventListener(this);
        }else{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference newsRef = database.getReference().child(FirebaseUtils.NEWS_TABLE);
            DatabaseReference cityTable = newsRef.child(FirebaseUtils.CITY_TABLE);
            DatabaseReference countryRef = cityTable.child(FirebaseUtils.getCountry(cityId));
            DatabaseReference cityRef = countryRef.child(FirebaseUtils.getCityName(cityId));
            cityRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    News news = dataSnapshot.getValue(News.class);
                    news.setId(FirebaseUtils.getIdNewsGeneral(
                            String.valueOf(news.getTime()),FirebaseUtils.getCountry(cityId),FirebaseUtils.getCityName(cityId)));
                    addNews(news);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    private void addNews(News news) {
        newses.add(news);
        ArrayAdapter<News> simple  = new ArrayAdapter<News>(this,android.R.layout.simple_list_item_1,newses);
        listView.setAdapter(simple);

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        News news = newses.get(i);
        Intent intent = new Intent(this,NewsEditorActivity.class);
        intent.putExtra(NewsEditorActivity.ID_ARGUMENT,news.getId());
        startActivity(intent);
    }
}
