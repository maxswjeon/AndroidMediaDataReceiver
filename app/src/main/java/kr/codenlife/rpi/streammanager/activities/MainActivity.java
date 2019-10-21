package kr.codenlife.rpi.streammanager.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import kr.codenlife.rpi.streammanager.MediaInfoManager;
import kr.codenlife.rpi.streammanager.OnMusicStateChangedListener;
import kr.codenlife.rpi.streammanager.R;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private String infoToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);

        infoToken = MediaInfoManager.addOnMusicStateChangedListener(new OnMusicStateChangedListener() {
            @Override
            public void onMusicStateChanged(int from, String title) {
                textView.setText(title);
            }
        });
        textView.setText(MediaInfoManager.getCurrentTitle());

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

}
