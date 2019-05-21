package com.example.mi_b_wizard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Data.CardAdapter;
import com.example.mi_b_wizard.Data.Game;
import com.example.mi_b_wizard.Data.Hand;
import com.example.mi_b_wizard.Data.Player;
import com.example.mi_b_wizard.Data.ViewCards;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

import java.util.ArrayList;
import java.util.List;


public class GameActivity extends AppCompatActivity implements SensorEventListener, RecognitionListener, PredictedTricksDialogFragment.NoticeDialogListener {
    @SuppressLint("StaticFieldLeak")
    private static GameActivity gameActivity;
    private CardAdapter cardAdapter = new CardAdapter() ;
    Button startAndSendCards, playACard, predictTricksBtn;
    TextView myCard, trumpView;
    Hand myHand;
    Card playedcard;
    String card;
    Game game;
    ViewGroup layout;
    Server server;
    Card trump;
    byte zero = 0;
    boolean tricks = false;
    Player me = MainActivity.getPlayer();
    MessageHandler messageHandler;
    private boolean firstRound = true;
    private boolean myTurn = false;
    private boolean haveICheated = false;
    private boolean winnerThisRound = false;
    private boolean canWeStart = false;

    ArrayList<ViewCards> handCards = new ArrayList<ViewCards>();
    Card nextCard;


    private SensorManager mySensorManager;
    private Vibrator myVibrator;
    private long lastUpdate;
    private AlertDialog.Builder myBuilder;
    private AlertDialog myDialog;
    private boolean isPopUpActive = false;
    //For SpeechRecognition
    private ProgressBar progressBar;
    private TextView tricksTable, myTricksTable;
    private SpeechRecognizer speechRecognizer = null;
    private Intent speechRecognizerIntent;
    static final int REQUEST_PERMISSION_KEY = 1;
    private static final String LOG_TAG = "SpeechActivity";
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

        LinearLayout trumpPos = findViewById(R.id.trumpPosition);
        trumpPos.removeAllViews();
        ViewCards cardview = new ViewCards(GameActivity.this,this,trump);
        trumpPos.addView(cardview.view);
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
        setTricks();

        showCardsInHand();
    }

    public void takeCards(byte[] cards){
        setMyHand(cards);

        showCardsInHand();

        if(JoinGameActivity.owner){
            System.out.println("host got his cards");
        }else{
            System.out.println("player "+me.getPlayerName() +" got his cards");}
    }


    //To show cards/images in Hand

    private void showCardsInHand(){
        LinearLayout cardHand = findViewById(R.id.cardHand1);
        cardHand.removeAllViews();
        handCards.clear();

        List<Card> cards = myHand.getHand();
        for (int i = 0; i<myHand.getHandSize(); i++){
            addImageToScrollView(cardHand, cards.get(i));
        }
    }

    //To add images in ScrollView and give them a border, when a card is chosen

    private void addImageToScrollView(LinearLayout cardHandView, Card card) {
        ViewCards cardview = new ViewCards(GameActivity.this,this,card);
        handCards.add(cardview);
        final int index = handCards.indexOf(cardview);
        cardview.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCards cardview = GameActivity.this.handCards.get(index);

                if(!cardview.isActive){
                    cardview.view.setBackgroundColor(Color.WHITE);
                    cardview.view.setPadding(3,3,3,3);
                    GameActivity.this.setNextCard(cardview.card);
                    cardview.isActive = true;
                }
                else {
                    cardview.view.setPadding(0,0,0,0);
                    cardview.view.setBackgroundColor(Color.TRANSPARENT);
                    GameActivity.this.setNextCard(null);
                    cardview.isActive = false;
                }
            }
        });
        cardHandView.addView(cardview.view);

    }

    private void setNextCard(Card card){
        nextCard = card;
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
    public void showPredictedTricks(byte trick, int id){
        toast("Player with ID: "+id+ "has predicted "+trick+ "tricks");
        //Shows the tricks of the other players
        String messageTricks = "Tricks Player "+id+": "+trick;
        tricksTable.append("\n"+messageTricks);
    }
    public void setTricks(){
        tricks = false;
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
        predictTricksBtn = findViewById(R.id.predictTricksbtn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        tricksTable = findViewById(R.id.tricksTable);
        myTricksTable = findViewById(R.id.myTricksTable);
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
                if(nextCard == null){
                    toast("Please select card first");

                } else if (myTurn && JoinGameActivity.owner) {
                    messageHandler.sendEvent(Server.MOVE,nextCard.getId(),zero,zero);
                    notMyTurnAnymore();
                    game.hostMadeAMove(myHand.getFirstCardInHand());
                    myHand.removeCardFromHand(nextCard);
                    showCardsInHand();

                } else if(myTurn){
                    messageHandler.sendEvent(Server.MOVE,nextCard.getId(),zero,zero);
                    myHand.removeCardFromHand(nextCard);
                    showCardsInHand();
                    notMyTurnAnymore();
                } else {
                    toast("Its not your turn to play");
                }
            }
        });

        //Checks the permission for SpeechRecognition- User has to accept the permission by the first use
        String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO};
        if(!checkForPermission(GameActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(GameActivity.this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        }


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        predictTricksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecognitionActivity();
                //Start listening
                progressBar.setVisibility(View.VISIBLE);
                predictTricksBtn.setVisibility(View.INVISIBLE);
                speechRecognizer.startListening(speechRecognizerIntent);
                //Stop listening
                progressBar.setVisibility(View.INVISIBLE);
                predictTricksBtn.setVisibility(View.VISIBLE);
                speechRecognizer.stopListening();
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
    //----SpeechRecognition----
    //Methods need to be override, because of the implementation of RecognitionListener(Abstract)
    @Override
    public void onBeginningOfSpeech() {
        Log.d(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setVisibility(View.VISIBLE);
        predictTricksBtn.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(LOG_TAG, "onBufferReceived: " + buffer);
    }
    @Override
    public void onEndOfSpeech() {
        Log.d(LOG_TAG, "onEndOfSpeech");
        progressBar.setVisibility(View.INVISIBLE);
        predictTricksBtn.setVisibility(View.VISIBLE);
    }
    @Override
    public void onEvent(int event, Bundle params) {
        Log.d(LOG_TAG, "onEvent" + event);
    }
    @Override
    public void onReadyForSpeech(Bundle args) {
        Log.d(LOG_TAG, "onReadyForSpeech");
    }
    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d(LOG_TAG, "onRmsChanged");
    }
    @Override
    public void onPartialResults(Bundle partialR) {
        Log.d(LOG_TAG, "onPartialResults");
    }
    @Override
    public void onResults(Bundle results) {
        Log.d(LOG_TAG, "onResults");
        //Saves the results of the speechrecognition and checks them ( 9 Uhr ->9 )
        ArrayList<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String result = resultList.get(0);
        me.checkPredictedTricks(result);
        builtDialog();

    }

    private void builtDialog() {
        String predictedTricksS = ""+me.getCheckedPredictedTricks();
        //To create an instance of the PredictedTricksDialogFragment, and sets the bundle with the checkedPredictedTricks
        DialogFragment dialogFragment = new PredictedTricksDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PREDICTEDTRICKS", predictedTricksS);
        dialogFragment.setArguments(bundle);
        //shows the dialog Fragment
        dialogFragment.show(getSupportFragmentManager(), "PredictedTricksDialogFragment");

    }

    //If an error occurs it will show a toast message with the error
    @Override
    public void onError(int error) {
        Log.d(LOG_TAG, "onError " + error);
        String errorM = getErrorMessage(error);
        progressBar.setVisibility(View.INVISIBLE);
        predictTricksBtn.setVisibility(View.VISIBLE);
        toast(errorM);
    }

    public static String getErrorMessage(int error) {
        String errorMessage;
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                errorMessage = "Audio error";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                errorMessage = "Network error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                errorMessage = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                errorMessage = "Client error";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                errorMessage = "Didn't found a match";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                errorMessage = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                errorMessage = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                errorMessage = "Missing speech input";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                errorMessage = "Server error";
                break;
            default:
                errorMessage = "Please try again.";
                break;
        }
        return errorMessage;
    }

    private void startRecognitionActivity() {
        //Intent to start the speechRecognition Activity
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //Set the Callingpackage for identification of the application
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());

        //To inform the recognizer which speech model is prefered
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        //To request partial results
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
    }

    //Methode to check the permission
    private static boolean checkForPermission(Context context, String... permissions){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    //Through the .onAttach() callback methode in the Fragment,
    //the dialog fragement gets a reference to this activity.
    //to call these methodes defined in the .NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //On positive click tricks will be confirmed and updated, show the tricks of the current player in his gameActivity
        Byte confirmedPredictedTricks = me.getCheckedPredictedTricks();
        me.updatePredictedTricks(confirmedPredictedTricks);
        Log.d(LOG_TAG,"positive click: " + confirmedPredictedTricks);
        myTricksTable.append("\n"+"My Tricks: "+me.getPredictedTrick());
        sendPredictedTricks();
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d(LOG_TAG,"negative click");
    }

    //sends the predicted tricks to the other players
    public void sendPredictedTricks(){
        if(JoinGameActivity.owner ){
            messageHandler.sendEvent(Server.TRICKS,me.getPredictedTrick(),zero,zero);
            tricks = true;
        }else if(!tricks){
            messageHandler.sendEvent(Server.TRICKS,me.getPredictedTrick(),zero,zero);
            tricks = true;
        }else {
            toast("It's not your turn to predict tricks or you have already predicted");
        }
    }

}