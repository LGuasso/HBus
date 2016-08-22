package br.com.expressobits.hbus.messaging;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.ui.notification.NotificationsNews;

public class NewsFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "NewsMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        News news = new News();
        news.setBody(remoteMessage.getNotification().getBody());
        news.setTitle(remoteMessage.getNotification().getTitle());
        NotificationsNews.notifyBus(this,news);
    }
}
