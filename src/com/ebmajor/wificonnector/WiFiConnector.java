package com.ebmajor.wificonnector;

import java.util.List;

import com.ebmajor.wificonector.R;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;

public class WiFiConnector extends Activity {
	private final static String TAG = "WiFiConnector";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wi_fi_connector);
		connectWifi();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_wi_fi_connector, menu);
		return true;
	}

   void connectWifi()
   {
	   String networkSSID = "stonyslp";
	   String networkPass = "ebmajorstonyslp";
	   
	   Log.e(TAG, "connectWifi");
	   WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
       // setup a wifi configuration
       WifiConfiguration wc = new WifiConfiguration();
       wc.SSID = "\"" + networkSSID + "\"";
       wc.preSharedKey = "\"" + networkPass + "\"";
       
       wc.status = WifiConfiguration.Status.ENABLED;
       wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
       wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
       wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
       wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
       wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
       wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

       boolean b = wifiManager.isWifiEnabled();
       Log.e(TAG, "wifiManager.isWifiEnabled returned: " + b );

       int s = wifiManager.getWifiState();
       Log.e(TAG, "wifiManager.getWifiState returned: " + s );
       // connect to and enable the connection
       b = wifiManager.setWifiEnabled(true);
       Log.e(TAG, "wifiManager.setWifiEnabled returned: " + b );
       //wifiManager.disconnect();
       int netId = wifiManager.addNetwork(wc);
       Log.e(TAG, "add Network returned: " + netId);
       //wifiManager.enableNetwork(netId, true);    
       //wifiManager.reconnect();

       b = scanWifi(wifiManager, networkSSID);
       
       List<WifiConfiguration> wfList = wifiManager.getConfiguredNetworks();
       for( WifiConfiguration i : wfList ) {
    	   if(i.SSID!=null)
    	   {
    		   Log.e(TAG, "SSID: "+i.SSID);
    	   }
    	   
           if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
    		   Log.e(TAG, "Found SSID: " + i.SSID);
        	   wifiManager.disconnect();
               b=wifiManager.enableNetwork(i.networkId, true);
               Log.e(TAG, "wifiManager.enableNetwork returned: " + b );
               b=wifiManager.reconnect();
               Log.e(TAG, "wifiManager.reconnect returned: " + b );
               break;
           }           
        }       
       //wifiManager.saveConfiguration();
   }

   boolean scanWifi(WifiManager wifiManager, String networkSSID)
   {
	   Log.e(TAG, "scanWifi starts");
       List<ScanResult> scanList = wifiManager.getScanResults();
       for( ScanResult i : scanList ) {
    	   if(i.SSID!=null)
    	   {
    		   Log.e(TAG, "SSID: "+i.SSID);
    	   }
    	   
           if(i.SSID != null && i.SSID.equals(networkSSID)) {
    		   Log.e(TAG, "Found SSID: " + i.SSID);
               return true;
           }           
       }       
       Log.e(TAG, "No SSID found");
       return false;
   }

   // Adding a WPA or WPA2 network
	void changeNetworkWPA(WifiManager wifiManager, String ssid, String password) {
			WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);   
			WifiConfiguration wc2 = new WifiConfiguration();
		    // This is must be quoted according to the documentation 
		    // http://developer.android.com/reference/android/net/wifi/WifiConfiguration.html#SSID
		    wc2.SSID = "\"zpoint\"";
		    wc2.preSharedKey  = "\"sipisP@ssw0rd!\"";
		    wc2.hiddenSSID = true;
		    wc2.status = WifiConfiguration.Status.ENABLED;        
		    wc2.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		    wc2.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		    wc2.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		    wc2.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		    wc2.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		    wc2.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		    int res = wifi.addNetwork(wc2);
		    Log.d("WifiPreference", "add Network returned " + res );
		    boolean b = wifi.enableNetwork(res, true);        
		    Log.d("WifiPreference", "enableNetwork returned " + b );
	}   
}
