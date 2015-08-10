# Reliable Android Network Reachability Service

## Introduction

Easy way to check reachability access on demand and to handle network reachability changes and . 

## How to install


### Required permissions
 
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	<uses-permission android:name="android.permission.INTERNET"/>
	<uses-permission android:name="android.permission.READ_PHONE_STATE"/>

## How to use

### Initialize

	@Override
	public void onResume() {
	    super.onResume();
	    NetworkAvailabilityService.initialize(this);
	}
	
### unregister internal broadcast receiver

    @Override
    public void onPause() {
        super.onPause();
        NetworkAvailabilityService.unregister();
    }

## On Demand

### Network Connectivity Type

	NetworkAvailabilityService.getType();
	
### Network is connected
    
    NetworkAvailabilityService.isConnected();
    
### Google is reachable
    
    NetworkAvailabilityService.isReachable(new IReachabilityHandler() {
        @Override
        public void onReachabilityCheck(final boolean isReachable) {
            Log.v(TAG, "Google is reachable: " + isReachable);
        }
    });
    
### Custom uri is reachable
    
    NetworkAvailabilityService.isReachable("http://www.google.com", new IReachabilityHandler() {
        @Override
        public void onReachabilityCheck(final boolean isReachable) {
            Log.v(TAG, "Internet is reachable: " + isReachable);    
        }
    });
    
## Network change

### Add listener

    NetworkAvailabilityService.addIConnectivityChangeListener(new IConnectivityChangeListener() {
        @Override
        public void onConnectivityChange(final boolean isConnected, final ConnectivityType type) {
            Log.v(TAG, "onConnectivityChange: isConnected= " + isConnected + " typ= " + type);
        }
    });