package kr.codenlife.rpi.streammanager;

import android.util.Log;

import java.util.HashMap;

public class MediaInfoManager {
    public static final int FROM_NONE = -1;
    public static final int FROM_YOUTUBE = 0;
    public static final int FROM_BROADCAST = 1;
    private static final int TOKEN_LEN = 16;
    private static HashMap<String, OnMusicStateChangedListener> _listeners;
    private static String currentTitle;
    private static int currentFrom;

    public static void init() {
        currentTitle = "";
        currentFrom = FROM_NONE;
        _listeners = new HashMap<>();
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
            token = generateToken(TOKEN_LEN);
        } while (_listeners.containsKey(token));

        _listeners.put(token, listener);
        return token;
    }

    public static void removeOnMusicStateChangedListener(String token) {
        _listeners.remove(token);
    }

    private static String generateToken(int len) {
        String s = "abcdefghijkpmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; ++i) {
            int index = (int) (s.length() * Math.random());
            sb.append(s.charAt(index));
        }

        return sb.toString();
    }
}
