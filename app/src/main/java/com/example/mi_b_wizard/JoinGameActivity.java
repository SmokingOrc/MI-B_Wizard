package com.example.mi_b_wizard;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Network.Client;
import com.example.mi_b_wizard.Network.GroupOwnerSocketHandler;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;
import com.example.mi_b_wizard.Network.WiFiDirectBroadcastReceiver;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class JoinGameActivity extends AppCompatActivity implements ChannelListener {
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;
    Handler handler;
    Notifications notifications = new Notifications(this);
    MessageHandler messageHandler;
    ListView lvAvailableGames;
    Button btnDiscover, btnStartGame, btnSend;

    public TextView wifi;
    EditText message;
    public WifiP2pManager mManager;
    public WifiP2pManager.Channel mChannel;
    Server mServer;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    String user;
    public static boolean owner = false;


    public void setUser(String user) {
        this.user = user;
    }

    public Context getNotifications() {
        return this;
    }

    public Notifications getNotification() {
        return notifications;
    }

    public void setmServer(Server obj) {
        mServer = obj;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED && requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            System.out.println("Coarse location permission is not granted!");
            finish();
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        wifi = findViewById(R.id.wifi);
        lvAvailableGames = (ListView) findViewById(R.id.lvAvailableGames);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnDiscover = findViewById(R.id.btnDiscover);
        btnSend = findViewById(R.id.send);
        message = findViewById(R.id.message);
        messageHandler = new MessageHandler();
        handler = new Handler();
        handler = messageHandler.getHandler();
        messageHandler.setJoingameContext(this);
        setUser(MainActivity.user);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    JoinGameActivity.PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            // After this point you wait for callback in
            // onRequestPermissionsResult(int, String[], int[]) overridden method
        }

        mIntentFilter = new IntentFilter();
        //  Indicates a change in the Wi-Fi P2P status.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // Indicates a change in the list of available peers.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Indicates the state of Wi-Fi P2P connectivity has changed.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Indicates this device's details have changed.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(getApplicationContext(), "Discovery failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!owner) {
                    if (mServer == null) {
                        mServer = messageHandler.getServer();
                    } else {
                        mServer.write(user + " says: " + message.getText().toString());
                        message.setText("");
                    }
                } else {
                    messageHandler.write(user + " says: " + message.getText().toString());
                    message.setText("");

                }

                if (mServer == null && !owner) {
                    Toast.makeText(getApplicationContext(), "Please reconnect..", Toast.LENGTH_SHORT).show();
                    System.out.println("Server is null....");
                }
            }
        });

        lvAvailableGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WifiP2pDevice device = deviceArray[position];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;

                mManager.connect(mChannel, config, new ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(getApplicationContext(), "Can't connect to device: " + device, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        // Testing for Move Card network method
        btnStartGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Card c = new Card(1,1);

                if (!owner) {
                    if (mServer == null) {
                        mServer = messageHandler.getServer();
                    } else {
                        mServer.sendCard(c);
                        message.setText("");
                    }
                } else {
                    messageHandler.sendCard(c);
                    message.setText("");

                }

                if (mServer == null && !owner) {
                    Toast.makeText(getApplicationContext(), "Please reconnect..", Toast.LENGTH_SHORT).show();
                    System.out.println("Server is null....");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }


    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if (!peerList.getDeviceList().equals(peers)) {

                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
                int index = 0;

                for (WifiP2pDevice device : peerList.getDeviceList()) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceNameArray);
                lvAvailableGames.setAdapter(adapter);
            }
            if (peers.size() == 0) {
                System.out.println("no devices found");
                Toast.makeText(getApplicationContext(), "No devices found", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };


    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            Thread handler = null;
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner) {
                owner = true;
                Toast.makeText(getApplicationContext(), "you are the host of the game", Toast.LENGTH_SHORT).show();
                try {
                    handler = new GroupOwnerSocketHandler(messageHandler.getHandler());
                    handler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //   mServer = new Server();
                //   mServer.start();
            } else if (info.groupFormed) {
                handler = new Client(messageHandler.getHandler(), groupOwnerAddress);
                handler.start();
                Toast.makeText(getApplicationContext(), "you are a client of the game", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onChannelDisconnected() {
        if (mManager != null) {
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            mManager.initialize(this, getMainLooper(), this);
        } else {
            Toast.makeText(this,
                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }
    }

}


