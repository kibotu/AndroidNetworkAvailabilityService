package net.kibotu.android.network.availabilty.service;

/**
 * Callback when network availabilty has changed.
 */
public interface IConnectivityChangeListener {

    /**
     * Callback when network availabilty has changed.
     *
     * @param isConnected <code>true</code> if is device is connected.
     * @param type Connection type.
     */
    void onConnectivityChange(final boolean isConnected, final ConnectivityType type);
}
