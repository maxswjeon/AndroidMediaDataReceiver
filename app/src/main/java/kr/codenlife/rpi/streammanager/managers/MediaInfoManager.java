package kr.codenlife.rpi.streammanager.managers;

import android.util.Log;

import java.util.HashMap;

import kr.codenlife.rpi.streammanager.listeners.OnMusicStateChangedListener;
import kr.codenlife.rpi.streammanager.utilities.TokenGenerator;

public class MediaInfoManager {
    public static final int FROM_NONE = -1;
    public static final int FROM_YOUTUBE = 0;
    public static final int FROM_BROADCAST = 1;
    private static final int TOKEN_LEN = 16;
    private static HashMap<String, OnMusicStateChangedListener> _listeners = new HashMap<>();
    private static String currentTitle;
    private static int currentFrom;

    public static void init() {
        currentTitle = "";
        currentFrom = FROM_NONE;
        Log.i("MediaInfoManager", "Initialized Manager");
    }

    public static String getCurrentTitle() {
        return currentTitle;
    }

    public static int getCurrentFrom() {
        return currentFrom;
    }

    public static void update(int from, String title) {
        if (title.equals(currentTitle) && from == currentFrom) {
            return;
        }
        currentTitle = title;
        currentFrom = from;
        for (String key : _listeners.keySet()) {
            OnMusicStateChangedListener listener = _listeners.get(key);

            if (listener == null) {
                continue;
            }
            listener.onMusicStateChanged(from, title);
        }
    }

    public static String addOnMusicStateChangedListener(OnMusicStateChangedListener listener) {
        String token;
        do {
            token = TokenGenerator.generate(TOKEN_LEN);
        } while (_listeners.containsKey(token));

        _listeners.put(token, listener);
        return token;
    }

    public static void removeOnMusicStateChangedListener(String token) {
        _listeners.remove(token);
    }
}
