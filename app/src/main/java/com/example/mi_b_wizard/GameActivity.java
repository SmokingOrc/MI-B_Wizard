package com.example.mi_b_wizard;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Data.CardAdapter;
import com.example.mi_b_wizard.Data.Game;
import com.example.mi_b_wizard.Data.Hand;
import com.example.mi_b_wizard.Data.Player;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    @SuppressLint("StaticFieldLeak")
    private static GameActivity gameActivity;
    private CardAdapter cardAdapter = new CardAdapter() ;
    Button startAndSendCards, playACard;
    TextView myCard, trumpView;
    Hand myHand;
    Card playedcard;
    String card;
    Game game;
    ViewGroup layout;
    Server server;
    Card trump;
    byte zero = 0;
    Player me = MainActivity.getPlayer();
    MessageHandler messageHandler;
    private byte predictedTricks;
    private boolean firstRound = true;
    private boolean myTurn = false;
    private boolean haveICheated = false;
    private boolean winnerThisRound = false;
    private boolean canWeStart = false;
    private SensorManager mySensorManager;
    private Vibrator myVibrator;
    private long lastUpdate;
    private AlertDialog.Builder myBuilder;
    private AlertDialog myDialog;
    private boolean isPopUpActive = false;

    public static GameActivity getGameActivity() {
        return gameActivity;
    }

    public void playerMadeAMove(Byte cardPlayed, int playerID){
        game.moveMade(cardPlayed,playerID);
        showMove(cardPlayed);
        messageHandler.sendEventToAllExceptTheSender(Server.MOVE,cardPlayed,zero,zero,playerID);
    }
    public void setTrump(byte cardT){
        trump = cardAdapter.getThisCard(cardT);
        trumpView.setText("TRUMP IS : "+trump.getColour()+" "+trump.getRank());
    }

    public void showMove(Byte cardPlayed){
        playedcard = cardAdapter.getThisCard(cardPlayed);
        toast("Card : "+ playedcard.getColour()+" "+playedcard.getRank()+" was played ");
    }
    public void isFirstRound(){
        if(firstRound && JoinGameActivity.owner){
            myTurn = true;
            firstRound = false;
        }else{
            firstRound = false;
        }
    }
    public void start(){
        canWeStart = true;
        layout.removeView(startAndSendCards);
        playACard.setVisibility(View.VISIBLE);
        toast("game has started");

    }
    public void setHaveICheated(){
        haveICheated = true;
    }

    public void showWhoIsTheWinner(){
        toast("You won!");
        messageHandler.write(me.getPlayerName()+" won this round");
        me.madeATrick();
        myTurn = true;
    }

    public void takeCards(byte[] cards){
        setMyHand(cards);
        myCard.setText(myHand.getHand().toString());
        if(JoinGameActivity.owner){
            System.out.println("host got his cards");
        }else{
            System.out.println("player "+me.getPlayerName() +" got his cards");}
    }

    public int getColor(byte card){
        return cardAdapter.getThisCard(card).getColour().ordinal();
    }

    private void notMyTurnAnymore(){
        myTurn =false;
    }

    private void playACard(byte cardPlayed){
        if(myTurn && JoinGameActivity.owner){
            myHand.removeCardFromHand(cardAdapter.getThisCard(cardPlayed));
            game.hostMadeAMove(cardPlayed);
            messageHandler.sendEvent(Server.MOVE,cardPlayed,zero,zero);
            myTurn = false;
        }else if (myTurn){
            myHand.removeCardFromHand(cardAdapter.getThisCard(cardPlayed));
            messageHandler.sendEvent(Server.MOVE,cardPlayed,zero,zero);
            myTurn = false;
        }
    }
    public void showPoints(byte[] playerPoints){
        String s ="";
        getPlayerPoints(playerPoints, s);
        System.out.println("player "+me.getPlayerName() +" got points");

    }

    public void showMyPoints(){

    }
    public void PlayersStart(){
        layout.removeView(startAndSendCards);

    }
    public void MyTurn(){
        myTurn = true;
        toast("its your turn");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameActivity = this;
        startAndSendCards = findViewById(R.id.StartWithCards);
        myCard = findViewById(R.id.mycard);
        trumpView =findViewById(R.id.trump);
        playACard = findViewById(R.id.playacard);
        playACard.setVisibility(View.INVISIBLE);
        messageHandler = MessageHandler.messageHandler();
        layout = (ViewGroup) startAndSendCards.getParent();
        if (JoinGameActivity.owner) {
            game = new Game();
            game.setMessageHandler(messageHandler);
            game.setIds();
            startAndSendCards.setText("GIVE OUT CARDS"); // for the first round.
        }else{
            server = messageHandler.getServer();
        }
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        myVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        startAndSendCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JoinGameActivity.owner) {
                    PlayersStart();
                    messageHandler.sendEvent(Server.START_GAME,zero,zero,zero);
                    try{
                    game.sendCards();}
                    catch (IllegalStateException e){
                        toast("Wrong number of players");
                    }
                    layout.removeView(startAndSendCards);
                    playACard.setVisibility(View.VISIBLE);
                    isFirstRound();
                } else {
                    toast("You have to wait for host to give out the cards..");
                }
            }
        });


        playACard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myTurn && JoinGameActivity.owner && !myHand.getHand().isEmpty()) {
                    notMyTurnAnymore();
                    byte card = myHand.getFirstCardInHand();
                    myHand.removeFirstCard();
                    messageHandler.sendEvent(Server.MOVE,card,zero,zero);
                    game.hostMadeAMove(card);
                    myCard.setText(myHand.getHand().toString());
                }else if(myTurn  && !myHand.getHand().isEmpty()){
                    notMyTurnAnymore();
                    messageHandler.sendEvent(Server.MOVE,myHand.getFirstCardInHand(),zero,zero);
                    myHand.removeFirstCard();
                    myCard.setText(myHand.getHand().toString());
                } else {
                    toast("Its not your turn to play");
                }
            }
        });

    }

    private void getPlayerPoints(byte[] playerPoints, String s) {
        for (int i = 1; i < 6; i++) { // max 5 players + host
            if (playerPoints[i] != 0) {
                s += playerPoints[i] + ", ";
            } else {
                break;
            }
            s += me.getPoints();
            System.out.println(s);
        }
    }

    private void setMyHand(byte[] cards) {
        myHand = new Hand();
        myHand = cardAdapter.getMyhand(cards);
    }
    public void toast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        haveICheated = true;
       // System.out.println("cheat");
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            //enemyCards = testGame.getCardsOfRandomPlayer();
            //String[] splitted = enemyCards.split(";");

            /*Camera cam = Camera.open();
            Camera.Parameters parameters = cam.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            cam.setParameters(parameters);
            cam.startPreview();*/

            if(!isPopUpActive) {
                isPopUpActive = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    myVibrator.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    myVibrator.vibrate(1000);
                    //myVibrator.cancel();
                }
                myBuilder = new AlertDialog.Builder(GameActivity.this);
                myBuilder.setTitle("Cards from: ");
                myBuilder.setMessage("some cards");
                myBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    /*if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                        Camera cam = Camera.open();
                        Camera.Parameters parameters = cam.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        cam.setParameters(parameters);
                        cam.stopPreview();
                        cam.release();
                    }*/
                        myVibrator.cancel();
                        isPopUpActive = false;
                    }
                });
                myBuilder.setIcon(android.R.drawable.ic_dialog_info);
                myDialog = myBuilder.create();
                myDialog.show();
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        mySensorManager.registerListener(this,
                mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        mySensorManager.unregisterListener(this);
    }
}