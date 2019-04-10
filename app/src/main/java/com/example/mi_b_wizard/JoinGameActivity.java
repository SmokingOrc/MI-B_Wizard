package com.example.mi_b_wizard;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.net.wifi.p2p.WifiP2pManager.ChannelListener;

import com.example.mi_b_wizard.Network.Client;
import com.example.mi_b_wizard.Network.SendReceive;
import com.example.mi_b_wizard.Network.Server;
import com.example.mi_b_wizard.Network.WiFiDirectBroadcastReceiver;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class JoinGameActivity extends AppCompatActivity implements ChannelListener {
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;
    ListView lvAvailableGames;
    Button btnDiscover, btnStartGame;
    public TextView wifi;
    ArrayAdapter<String> wifiP2PAdapter;
    public WifiP2pManager mManager;
    public WifiP2pManager.Channel mChannel;
    static final int MSG = 1;
    static final int MOVE = 2;
    Server mServer;
    Client mClient;

    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    SendReceive sendReceive;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION:
                if  (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Coarse location permission is not granted!");
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager,mChannel,this);
        wifi=findViewById(R.id.wifi);
        lvAvailableGames = (ListView)findViewById(R.id.lvAvailableGames);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnDiscover = findViewById(R.id.btnDiscover);

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

        btnStartGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(sendReceive != null){
                String mes = "play....";
                    sendReceive.write("play....".getBytes());}
                    else{ Toast.makeText(getApplicationContext(), "Please reconnect...", Toast.LENGTH_SHORT).show(); }
            }
        });
        lvAvailableGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WifiP2pDevice device = deviceArray[position];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                mManager.connect(mChannel, config, new ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Connected to "+device.deviceName,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(getApplicationContext(),"Can't connect to device: "+device,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
   /* public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG:
                    byte[] readBuff = (byte[]) msg.obj;
                    String temp = new String(readBuff,0,msg.arg1);
                    Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
                    break;

                case MOVE:
                // do something..
            }
            return true;
        }
    });
/*
    private class SendReceive extends Thread {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        @Override
        public void run() {
            super.run();
            byte[] buffer = new byte[1024];
            int bytes;

            while (socket!=null){
                try {
                    bytes = inputStream.read();
                    if(bytes>0){
                        handler.obtainMessage(MSG,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public SendReceive(Socket socket1){
            socket = socket1;
            try {
                inputStream = socket.getInputStream();
                outputStream =  socket.getOutputStream();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    } */

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

 public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
       @Override
       public void onPeersAvailable(WifiP2pDeviceList peerList) {
           if(!peerList.getDeviceList().equals(peers)) {

            peers.clear();
            peers.addAll(peerList.getDeviceList());

            deviceNameArray = new String[peerList.getDeviceList().size()];
            deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
            int index = 0;

            for(WifiP2pDevice device : peerList.getDeviceList()){
                deviceNameArray[index]=device.deviceName;
                deviceArray[index]=device;
                index++;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceNameArray);
            lvAvailableGames.setAdapter(adapter);
           }
           if(peers.size() == 0){
               System.out.println("no devices found");
               Toast.makeText(getApplicationContext(),"No devices found",Toast.LENGTH_SHORT).show();
               return;
           }
       }
   };
/*
    public class Server extends Thread {
        Socket socket;
        ServerSocket serverSocket;


        @Override
        public void run() {
            super.run();
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
/*
    public class Client extends Thread {
        Socket socket;
        String host;


        public Client(InetAddress hostAddress) {
            host = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            super.run();

            try {
                socket.connect(new InetSocketAddress(host, 8888), 1000);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/
    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner){
                Toast.makeText(getApplicationContext(),"you are the host of the game",Toast.LENGTH_SHORT).show();
                mServer = new Server();
                mServer.start();
            }else if(info.groupFormed){
                mClient = new Client(groupOwnerAddress);
                mClient.start();
                Toast.makeText(getApplicationContext(),"you are a client of the game",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onChannelDisconnected() {

    }

}
