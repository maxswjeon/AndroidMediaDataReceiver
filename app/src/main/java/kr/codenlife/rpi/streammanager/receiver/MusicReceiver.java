package kr.codenlife.rpi.streammanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import kr.codenlife.rpi.streammanager.MediaInfoManager;

public class MusicReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
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
            Log.d("GoogleMusic.Status", "Playing");
            Log.d("GoogleMusic.title", title);
            MediaInfoManager.update(MediaInfoManager.FROM_BROADCAST, title);
        } else {
            Log.d("GoogleMusic.Status", "Paused");
            Log.d("GoogleMusic.title", title);
            MediaInfoManager.update(MediaInfoManager.FROM_NONE, "");
        }

    }
}
