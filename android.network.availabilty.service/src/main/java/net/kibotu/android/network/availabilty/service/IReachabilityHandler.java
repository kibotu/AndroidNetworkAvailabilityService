package net.kibotu.android.network.availabilty.service;

/**
 * Callback when uri reachability has checked.
 */
public interface IReachabilityHandler {

    /**
     * Callback when uri reachability has checked.
     *
      * @param isReachable <code>true</code> if uri is reachable.
     */
    void onReachabilityCheck(boolean isReachable);
}
