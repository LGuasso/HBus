package br.com.expressobits.hbus.messaging;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.ui.notification.NotificationsNews;

public class NewsFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "NewsMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final String newsId = remoteMessage.getNotification().getBody();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newRef = database.getReference(newsId);
        Log.d(TAG,newRef.getRef().toString());
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News news = dataSnapshot.getValue(News.class);
                if(news==null){
                    Log.d(TAG,"news is null!");
                }else {
                    news.setId(newsId);
                    NotificationsNews.notifyBus(NewsFirebaseMessagingService.this,news);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
