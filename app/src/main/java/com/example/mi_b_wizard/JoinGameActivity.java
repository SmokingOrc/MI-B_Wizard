package com.example.mi_b_wizard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mi_b_wizard.Network.WiFiDirectBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;

public class JoinGameActivity extends AppCompatActivity {
    private String[] availableGames =null ; // show devices/Games in List View
    private ListView lvAvailableGames = null;
    ArrayAdapter adapter = null;
    Button btnDiscover, btnStartGame;
    public TextView wifi;
    ArrayAdapter<String> wifiP2PAdapter;

    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;

    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    List<WifiP2pDevice> peers=new ArrayList<WifiP2pDevice>();
    WifiP2pDevice[] deviceArray;   //connect to devices

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
       // availableGames = new String[]{"Game 1", "Game 2", "Game 3", "Game 4"};
        lvAvailableGames = (ListView)findViewById(R.id.lvAvailableGames);
      // adapter = new ArrayAdapter<String>(this, R.layout.layoutfile, this.availableGames);
       // lvAvailableGames.setAdapter(adapter);
        btnStartGame=findViewById(R.id.btnStartGame);
        btnDiscover=findViewById(R.id.btnDiscover);

        initiatWifi();



    }

    private void initiatWifi() {

        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction((WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION));
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction((WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION));
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    public void displayPeers(WifiP2pDeviceList peerList) {

        wifiP2PAdapter.clear();

        for (WifiP2pDevice peer : peerList.getDeviceList()) {
            wifiP2PAdapter.add(peer.deviceName + "\n" + peer.deviceAddress);
        }
    }









  /*  public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if (!peerList.getDeviceList().equals(peers)){

                peers.clear();
                peers.addAll(peerList.getDeviceList());
                displayPeers(peerList);

                for (int i = 0; i < peerList.getDeviceList().size(); i++) {
                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = myPeers.get(i).deviceAddress;
                    myConfigs.add(config);
                }
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
    };*/

    @Override
    protected void onResume() {
        super.onResume();
        mManager=null;
        mReceiver=null;
        mChannel=null;
        wifiP2PAdapter=null;
        lvAvailableGames=null;
        wifi=null;

        wifi=findViewById(R.id.wifi);

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

        wifiP2PAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lvAvailableGames.setAdapter(wifiP2PAdapter);
       /* lvAvailableGames.setOnItemClickListener(new lvAvailableGames.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myReceiver.connect(position);
            }
        });*/

        //Wifi Direct aufbau
        wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=mManager.initialize(this,getMainLooper(),null);
        mReceiver=new WiFiDirectBroadcastReceiver(mManager,mChannel,this);
        registerReceiver(mReceiver,mIntentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);

    }
}
