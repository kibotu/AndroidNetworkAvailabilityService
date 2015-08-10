package net.kibotu.android.network.availabilty.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static net.kibotu.android.network.availabilty.service.ConnectivityType.TYPE_NONE;
import static net.kibotu.android.network.availabilty.service.ConnectivityType.getTypeForNetworkInfo;


public class NetworkAvailabilityService {

    private static final String TAG = NetworkAvailabilityService.class.getSimpleName();
    private static NetworkAvailabilityService instance;
    private final ConnectivityManager connectivityManager;
    List<IConnectivityChangeListener> connectivityChangeListener;
    private BroadcastReceiver receiver;
    private Context context;

    private NetworkAvailabilityService(final Context context) {
        this.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityChangeListener = new ArrayList<IConnectivityChangeListener>(0);
    }

    /**
     * Requires
     * <p><code>android.permission.ACCESS_NETWORK_STATE</code></p>
     * <code>android.permission.INTERNET</code>
     */
    public static void initialize(final Context context) {
        if (instance != null)
            return;

        synchronized (NetworkAvailabilityService.class) {
            instance = new NetworkAvailabilityService(context);
            instance.init();
        }
    }

    private static boolean isInit() {
        final boolean isInit = instance != null;
        if (!isInit)
            Log.w(TAG, "Not initialized.");
        return isInit;
    }

    public static NetworkInfo getActiveNetworkInfo() {
        if (!isInit())
            return null;

        return instance.getConnectivityManager().getActiveNetworkInfo();
    }

    public static boolean isConnected() {
        if (!isInit())
            return false;

        final NetworkInfo activeNetworkInfo = instance.getConnectivityManager().getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void isReachable(final IReachabilityHandler handler) {
        isReachable("http://www.google.com", handler);
    }

    public static void isReachable(final String uri, final IReachabilityHandler handler) {
        if (!isInit())
            return;

        if (handler == null)
            return;

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(final Void... params) {
                return isReachable(uri);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                handler.onReachabilityCheck(result);
            }
        }.execute();
    }

    private static boolean isReachable(final String uri) {
        // First, check we have any sort of connectivity
        boolean isReachable = false;

        final NetworkInfo activeNetworkInfo = instance.getConnectivityManager().getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            // Some sort of connection is open, check if server is reachable
            try {
                final URL url = new URL(uri);
                final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Android Application");
                urlConnection.setRequestProperty("Connection", "close");
                urlConnection.setConnectTimeout(30 * 1000);
                urlConnection.connect();

                // http://www.w3.org/Protocols/HTTP/HTRESP.html
                if (urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() > 400) {
                    // Requested site is available
                    isReachable = true;
                }
            } catch (final IOException e) {
                // Error while trying to connect
                Log.e(TAG, e.getMessage());
            }
        }

        return isReachable;
    }

    public static ConnectivityType getType() {
        return isConnected()
                ? getTypeForNetworkInfo(getActiveNetworkInfo())
                : TYPE_NONE;
    }

    public static void unregister() {
        if (!isInit())
            return;

        if (instance.receiver == null)
            return;

        try {
            instance.context.unregisterReceiver(instance.receiver);
        } catch (final Exception e) {
            Log.e(TAG, "Error unregistering network receiver: " + e.getMessage(), e);
        } finally {
            instance.receiver = null;
            instance.connectivityChangeListener.clear();
        }
    }

    public static void addIConnectivityChangeListener(final IConnectivityChangeListener listener) {
        instance.connectivityChangeListener.add(listener);
    }

    public static void removeIConnectivityChangeListener(final IConnectivityChangeListener listener) {
        instance.connectivityChangeListener.remove(listener);
    }

    private void init() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(final Context context, final Intent intent) {
                    notifyConnectivityChangeListener(isConnected(), getType());
                }
            };
            context.registerReceiver(receiver, intentFilter);
        }
    }

    private void notifyConnectivityChangeListener(final boolean isConnected, final ConnectivityType type) {
        for (final IConnectivityChangeListener listener : connectivityChangeListener)
            listener.onConnectivityChange(isConnected, type);
    }

    private ConnectivityManager getConnectivityManager() {
        return connectivityManager;
    }
}