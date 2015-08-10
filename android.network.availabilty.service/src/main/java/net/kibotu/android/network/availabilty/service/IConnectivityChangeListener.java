package net.kibotu.android.network.availabilty.service;

/**
 * Created by Jan Rabe on 10/08/15.
 */
public interface IConnectivityChangeListener {

    void onConnectivityChange(final boolean isConnected, final ConnectivityType type);
}
