package kr.codenlife.rpi.streammanager.services;

import android.app.Notification;
import android.content.Intent;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;

import kr.codenlife.rpi.streammanager.MediaInfoManager;

public class NotificationService extends NotificationListenerService {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("NotiService", "Created Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NotiService", "Started Service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        Notification notificatin = sbn.getNotification();
        Bundle extras = notificatin.extras;

        String packageName = sbn.getPackageName();
        if (packageName.equals("com.google.android.youtube")) {
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
        } else if (packageName.equals("tv.twitch.android.app")) {
            Log.d("Notification.Data", "Twitch Notification Found");
        }
    }
}
