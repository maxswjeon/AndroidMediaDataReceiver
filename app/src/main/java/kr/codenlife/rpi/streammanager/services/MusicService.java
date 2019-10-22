package kr.codenlife.rpi.streammanager.services;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;

import kr.codenlife.rpi.streammanager.managers.MediaInfoManager;

public class MusicService extends NotificationListenerService {
    private BroadcastReceiver _receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Notification.Service", "Created Service");

        _receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onBroadcastReceived(context, intent);
            }
        };

        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.playstatechanged");
        registerReceiver(_receiver, iF);
        Log.i("MusicReceiver", "Registered Receiver");

        MediaInfoManager.init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Notification.Service", "Started Service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(_receiver);
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        Notification notification = sbn.getNotification();
        Bundle extras = notification.extras;

        String packageName = sbn.getPackageName();

        if (packageName.equals("com.google.android.youtube")
                || packageName.equals("com.google.android.apps.youtube.music")) {
            Log.d("Notification.Data", "Youtube Notification Found");

            String title;
            MediaController controller;

            Object title_obj = extras.get("android.title");
            if (title_obj == null) {
                Log.d("Youtube.title", "Title was null");
                return;
            }
            if (title_obj.getClass() == SpannableString.class) {
                title = ((SpannableString) title_obj).toString();
            } else if (title_obj.getClass() == String.class) {
                title = (String) title_obj;
            } else {
                return;
            }

            Object token_obj = extras.get("android.mediaSession");
            if (token_obj != null && token_obj.getClass() == MediaSession.Token.class) {
                MediaSession.Token token = (MediaSession.Token) token_obj;
                controller = new MediaController(this, token);
            } else {
                return;
            }

            PlaybackState state = controller.getPlaybackState();
            if (state == null) {
                Log.w("Youtube.Data", "Failed to get Youtube Playback state");
                Log.w("Youtube.Data", "controller.getPlaybackState() returned null");
                return;
            }

            if (state.getState() == PlaybackState.STATE_PLAYING) {
                Log.d("Youtube.Status", "Playing");
                Log.d("Youtube.title", title);
                MediaInfoManager.update(MediaInfoManager.FROM_YOUTUBE, title);
            } else if (state.getState() == PlaybackState.STATE_PAUSED) {
                Log.d("Youtube.Status", "Paused");
                Log.d("Youtube.title", title);
                MediaInfoManager.update(MediaInfoManager.FROM_NONE, "");
            }
        }
    }

    private void onBroadcastReceived(Context context, Intent intent) {
        Log.d("MusicReceiver", "Received Broadcast");
        if (intent.getAction() == null) {
            Log.d("MusicReceiver", "Received null action");
            return;
        }

        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.d("MusicReceiver.Data", "No Extra Data in Intent");
            return;
        }

        String title = intent.getExtras().getString("track");
        if (title == null) {
            return;
        }

        if (extras.getBoolean("playing")) {
            Log.d("MusicReceiver.Status", "Playing");
            Log.d("MusicReceiver.title", title);
            MediaInfoManager.update(MediaInfoManager.FROM_BROADCAST, title);
        } else {
            Log.d("MusicReceiver.Status", "Paused");
            Log.d("MusicReceiver.title", title);
            MediaInfoManager.update(MediaInfoManager.FROM_NONE, "");
        }
    }
}
