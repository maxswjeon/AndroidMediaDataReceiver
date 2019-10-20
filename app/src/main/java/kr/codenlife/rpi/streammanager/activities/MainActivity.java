package kr.codenlife.rpi.streammanager.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import kr.codenlife.rpi.streammanager.MediaInfoManager;
import kr.codenlife.rpi.streammanager.OnMusicStateChangedListener;
import kr.codenlife.rpi.streammanager.R;
import kr.codenlife.rpi.streammanager.receiver.MusicReceiver;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private MusicReceiver _receiver;

    private String infoToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName())) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }

        _receiver = new MusicReceiver();
        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.playstatechanged");
        registerReceiver(_receiver, iF);

        MediaInfoManager.init();
        infoToken = MediaInfoManager.addOnMusicStateChangedListener(new OnMusicStateChangedListener() {
            @Override
            public void onMusicStateChanged(int from, String title) {
                textView.setText(title);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(_receiver);
        super.onDestroy();
    }
}
