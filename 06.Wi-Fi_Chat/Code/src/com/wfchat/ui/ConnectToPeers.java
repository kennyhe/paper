package com.wfchat.ui;

import java.util.List;

import android.app.ProgressDialog;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
//import android.net.wifi.p2p.WifiP2pInfo;

public class ConnectToPeers{
	
private int i=0;
private String msg1="Device nearby....";
private String msg2="Device not nearby";
private String msg= "null";
private String givendevicename="null";

//private WifiP2pDevice device;
//private WifiP2pInfo info;
ProgressDialog progressDialog = null;	


public String searchPeers(List<WifiP2pDevice> items, int itemcount) {
 
	givendevicename = PeerDetails.getInstance().getVariable("FriendName");
	for (i = 1; i<=itemcount;i++){
		WifiP2pDevice device = items.get(i);
		if(device.deviceName.equals(givendevicename)) 
        {
        	ConnectToPeer(device.deviceAddress);
            msg = msg1;
        }
		else
			msg = msg2;
	}
	return msg;

}
/*
This function connects to a device with the MAC address (a.k.a. device address) specified via the params
The MAC address is obtained as return value of a function searchPeers(String givendeviceName).
	- searches the Peer list which is constructed after WFDirect discovery
	- Matching the device name. Once a match is found, return the MAC address.
	- Example: if(listitem.deviceName == givendeviceName) then return listitem.deviceAddress
 

*/

public void ConnectToPeer(String deviceMACAddr) {
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = deviceMACAddr;
                config.wps.setup = WpsInfo.PBC;
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
//                progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel",
//                        "Connecting to :" + device.deviceAddress, true, true );
//
//                ((DeviceActionListener) getActivity()).connect(config);

            }

}

