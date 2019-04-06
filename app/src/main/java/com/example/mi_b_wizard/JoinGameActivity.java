package com.example.mi_b_wizard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mi_b_wizard.Network.WiFiDirectBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;

public class JoinGameActivity extends AppCompatActivity {
    private String[] availableGames = null;
    private ListView lvAvailableGames = null;
    ArrayAdapter adapter = null;
    Button btnDiscover;

    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;

    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    List<WifiP2pDevice> peers=new ArrayList<WifiP2pDevice>();
    WifiP2pDevice[] deviceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
       // availableGames = new String[]{"Game 1", "Game 2", "Game 3", "Game 4"};
        // lvAvailableGames = (ListView)findViewById(R.id.lvAvailableGames);
       // adapter = new ArrayAdapter<String>(this, R.layout.layoutfile, this.availableGames);
       // lvAvailableGames.setAdapter(adapter);

        exqListener();

        //Wifi Direct aufbau
        mManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=mManager.initialize(this,getMainLooper(),null);

        mReceiver=new WiFiDirectBroadcastReceiver(mManager,mChannel,this);

        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction((WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION));
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction((WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION));
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    private void exqListener(){
        btnDiscover=findViewById(R.id.btnDiscover);
        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Discovery started",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(getApplicationContext(),"Discovery failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }


    public WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if (!peerList.getDeviceList().equals(peers)){

                peers.clear();
                peers.addAll(peerList.getDeviceList());

                availableGames=new String[peerList.getDeviceList().size()];
                deviceArray=new WifiP2pDevice[peerList.getDeviceList().size()];
                int index=0;

                for (WifiP2pDevice device:peerList.getDeviceList())
                {
                    availableGames[index]=device.deviceName;
                    deviceArray[index]=device;
                    index++;

                }

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,availableGames);
                lvAvailableGames.setAdapter(adapter);

            }

            if (peers.size()==0)
            {
                Toast.makeText(getApplicationContext(),"No Device Found",Toast.LENGTH_SHORT).show();
                return;
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver,mIntentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);

    }
}
