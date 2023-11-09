package com.example.mobisi.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnectionReceiver extends BroadcastReceiver {
    private ConnectionListener listener;

    public InternetConnectionReceiver(ConnectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (listener != null) {
            listener.onNetworkConnectionChanged(isInternetAvailable(context));
        }
    }

    private boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public interface ConnectionListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
