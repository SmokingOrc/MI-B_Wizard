package com.example.mi_b_wizard.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import com.example.mi_b_wizard.JoinGameActivity;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private JoinGameActivity mActivity;
    private List<WifiP2pDevice> myPeers;
    private List<WifiP2pConfig> myConfigs;
    private WifiP2pDevice mDevice;

    public WiFiDirectBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, JoinGameActivity mActivity){
        super();
        this.mManager=mManager;
        this.mActivity=mActivity;
        this.mChannel=mChannel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            // Determine if Wifi P2P mode is enabled or not, alert
            int state=intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if (state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                mActivity.wifi.setText("Wifi-Direct: Enabled");

            }else {
                mActivity.wifi.setText("Wifi-Direct: Disabled");
            }
        }else  if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            //Peer List has changed
            myPeers = new ArrayList<>();
            myConfigs = new ArrayList<>();

            if (mManager!=null)
            {
                WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {

                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peerList) {

                        // Out with the old, in with the new.

                        myPeers.clear();
                        myPeers.addAll(peerList.getDeviceList());

                        mActivity.displayPeers(peerList);

                        for (int i = 0; i < peerList.getDeviceList().size(); i++) {
                            WifiP2pConfig config = new WifiP2pConfig();
                            config.deviceAddress = myPeers.get(i).deviceAddress;
                            myConfigs.add(config);
                        }

                    }
                };

                mManager.requestPeers(mChannel,peerListListener);
            }
        }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            //do something
            if (mManager == null) {
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // we are connected with the other device, request connection
                // info to find group owner IP
                mManager.requestConnectionInfo(mChannel, infoListener);
            } else {
                // It's a disconnect
            }
        }else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            //do Something
            WifiP2pDevice device = (WifiP2pDevice) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
        }

    }

    public void connect(int position) {

        //uses postion to obtain the name and the address od the device to connect to
        WifiP2pConfig config = myConfigs.get(position);
        mDevice = myPeers.get(position);

        //connects the two and shows message that it worked
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mActivity, "Connected!", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(mActivity, "Connection Fail: " + reason, Toast.LENGTH_LONG).show();


            }

        });

    }

    public WifiP2pManager.ConnectionInfoListener infoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            InetAddress groupOwnerAddress = info.groupOwnerAddress;
            if (info.groupFormed) {
                if (info.isGroupOwner) {
                    Toast.makeText(mActivity, "Host", Toast.LENGTH_LONG).show();////////////////////////Debugging
                   // mActivity.play(groupOwnerAddress, true);

                } else {
                    Toast.makeText(mActivity, "Client", Toast.LENGTH_LONG).show();////////////////////////Debugging
                   // mActivity.play(groupOwnerAddress, false);


                }
            }
        }
    };




}
