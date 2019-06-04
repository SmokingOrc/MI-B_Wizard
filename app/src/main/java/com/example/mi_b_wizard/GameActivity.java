package com.example.mi_b_wizard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
    Player me = MainActivity.getPlayer();
    private String tag = "gameActivity";
    Button startAndSendCards;
    Button playACard;
    Button predictTricksBtn;
    Button writeTricksBtn;
    Button pointsBtn;
    TextView trumpView;
    TextView pointsTable;
    Hand myHand;
    Card playedcard;
    String card;
    Game game;
    ViewGroup layout;
    Server server;
    Card trump;
    ResultActivity resultActivity;
    byte zero = 0;
    boolean tricks = false;
    MessageHandler messageHandler;
    private boolean firstRound = true;
    private boolean myTurn = false;
    private boolean haveICheated = false;
    private boolean correctPoints = false;
    public String cheatString = "";
    private CheatingDialog cD;

    //For Cards in Hand, Trump and played cards
    ArrayList<ViewCards> handCards = new ArrayList<ViewCards>();
    LinearLayout playedCardsOthers, myPlayedCards;
    Card nextCard;

    private SensorManager mySensorManager;
    private Vibrator myVibrator;
    private long lastUpdate;
   // private AlertDialog.Builder myBuilder;
   // private AlertDialog myDialog;
    public boolean isPopUpActive = false;
    //For SpeechRecognition and manuel tricks input
    private ProgressBar progressBar;
    private TextView tricksTable;
    private TextView myTricksTable;
    public SpeechRecognizer speechRecognizer = null;
    private Intent speechRecognizerIntent;
    static final int REQUEST_PERMISSION_KEY = 1;
    private static final String LOG_TAG = "SpeechActivity";

    private NumberPicker numberPicker;
    private AlertDialog alertDialog;
    private AlertDialog alertDialog2;
    private TextView pointsView;

    public static GameActivity getGameActivity() {
        return gameActivity;
    }

    public void playerMadeAMove(Byte cardPlayed, int playerID){
        game.moveMade(cardPlayed,playerID);
        showMove(cardPlayed);
        messageHandler.sendEventToAllExceptTheSender(Server.MOVE,cardPlayed,zero,zero,playerID);
    }

    public void newRound(){
        me.calculateMyPoints();
        showMyPoints();
        haveICheated = false;
    }

    public void setTrump(byte cardT){
        trump = cardAdapter.getThisCard(cardT);
        RelativeLayout trumpPos = findViewById(R.id.trumpPosition);
        trumpPos.removeAllViews();
        ViewCards cardview = new ViewCards(GameActivity.this,this,trump);
        trumpPos.addView(cardview.view);

        clearView();
    }

    public void showMove(Byte cardPlayed){
        playedcard = cardAdapter.getThisCard(cardPlayed);
        toast("Card : "+ playedcard.getColour()+" "+playedcard.getRank()+" was played ");
        showPlayedCardsforAll();
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
        layout.removeView(startAndSendCards);
        playACard.setVisibility(View.VISIBLE);
        toast("game has started");

    }

    public void showWhoIsTheWinner(){
        toast("You won!");
        messageHandler.write(Server.NOTIFICATION,me.getPlayerName()+" made a trick ");
        myTurn = true;
        me.madeATrick();
    }

    public void takeCards(byte[] cards){
        setMyHand(cards);
        showCardsInHand();

        clearView();
    }

    //To show cards/images in Hand

    public String getPlayerHand(){
        return game.getPlayedCards();
    }

    private void showCardsInHand(){
        LinearLayout cardHand = findViewById(R.id.cardHand1);
        cardHand.removeAllViews();
        handCards.clear();

        List<Card> cards = myHand.getHand();
        for (int i = 0; i<myHand.getHandSize(); i++){
            addImageToScrollView(cardHand, cards.get(i));
        }
    }


    //To show my own played card on screen

    public void showMyPlayedCard(){
        myPlayedCards = findViewById(R.id.myplayedcard);
        myPlayedCards.removeAllViews();

        ViewCards cardview = new ViewCards(GameActivity.this, this, nextCard);
        myPlayedCards.addView(cardview.view);
    }


    //To show cards from other players on screen

    public void showPlayedCardsforAll() {
        playedCardsOthers = findViewById(R.id.playedcardsothers);

        ViewCards cardview = new ViewCards(GameActivity.this, this, playedcard);
        playedCardsOthers.addView(cardview.view);

    }

    //Clears all Views with played cards, because next round has started

    public void clearView(){
        clearPlayedCardsOthers();
        clearMyPlayedCards();
    }


    public void clearPlayedCardsOthers(){
        myPlayedCards = findViewById(R.id.playedcardsothers);
        myPlayedCards.removeAllViews();
    }

    public void clearMyPlayedCards(){
        playedCardsOthers = findViewById(R.id.myplayedcard);
        playedCardsOthers.removeAllViews();
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
                if(!tricks) {
                    toast("Please predict tricks first");
                }else if(!cardview.isActive){
                    cardview.view.setBackgroundColor(Color.WHITE);
                    cardview.view.setPadding(5,5,5,5);
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


    public void sendMyPoints(int points) {
        toast("My points: " + points);
        messageHandler.write(Server.SEND_POINTS, me.getPlayerName() + " has " + me.getPoints() + " points");
    }

    //Methode to show my points in pointsTable and set my points in TextView of the Dialog (CorrectPoints to start with calculation at the end of the first round)
    public void showMyPoints(){
        if (!correctPoints){
            correctPoints = true;
        }
        int p = me.getPoints();
        pointsTable.setText("My actual points: "+ p);
        sendMyPoints(p);
        pointsView = new TextView(GameActivity.this);
        pointsView.setText("My points: "+ p);
    }
    //Methode to set the points of the other players in the Pointsview to show them in the dialog
    public void setPointsInDialog(String points){
        pointsView.append("\n"+points);
    }
    public void PlayersStart(){
        layout.removeView(startAndSendCards);
    }

    public void MyTurn(){
        myTurn = true;
        toast("its your turn");
    }
    public void showPredictedTricks(String tricks){
        toast(tricks);
        //Shows the tricks of the other players
        tricksTable.append("\n"+tricks);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameActivity = this;
        startAndSendCards = findViewById(R.id.StartWithCards);
        trumpView =findViewById(R.id.trump);
        playACard = findViewById(R.id.playacard);
        playACard.setVisibility(View.INVISIBLE);
        predictTricksBtn = findViewById(R.id.predictTricksbtn);
        predictTricksBtn.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        tricksTable = findViewById(R.id.tricksTable);
        myTricksTable = findViewById(R.id.myTricksTable);
        messageHandler = MessageHandler.messageHandler();
        writeTricksBtn = findViewById(R.id.writeTricksbtn);
        writeTricksBtn.setVisibility(View.INVISIBLE);
        pointsTable = findViewById(R.id.pointstable);
        pointsBtn = findViewById(R.id.pointsButton);

        layout = (ViewGroup) startAndSendCards.getParent();
        if (JoinGameActivity.owner) {
            game = new Game();
            game.setMessageHandler(messageHandler);
            game.setIds();
            startAndSendCards.setText("GIVE OUT CARDS"); // for the first round.
        }else{
            server = messageHandler.getServer();
        }

        //For Cheating
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        myVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        cD = new CheatingDialog(GameActivity.this);


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


        //For updating Cards in Hand when Card is played

        playACard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nextCard != null){
                    if (myTurn && JoinGameActivity.owner) {
                    messageHandler.sendEvent(Server.MOVE,nextCard.getId(),zero,zero);
                    notMyTurnAnymore();
                    game.hostMadeAMove(nextCard.getId());
                    myHand.removeCardFromHand(nextCard);
                    showMyPlayedCard();
                    showCardsInHand();
                }else if(myTurn){
                    messageHandler.sendEvent(Server.MOVE,nextCard.getId(),zero,zero);
                    myHand.removeCardFromHand(nextCard);
                    showMyPlayedCard();
                    showCardsInHand();
                    notMyTurnAnymore();
                } else {
                    toast("Its not your turn to play");
                }
            }else {
                    toast("Please select card first");}
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

        numberPicker = new NumberPicker(GameActivity.this);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);

        writeTricksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this);
                alertDialogBuilder.setTitle("Please predict your tricks ");
                //to remove numberpicker from its parents (Otherwise Exception child already has a parent)
                if(numberPicker.getParent() != null){
                    ((ViewGroup)numberPicker.getParent()).removeView(numberPicker);
                    alertDialogBuilder.setView(numberPicker);
                }else {
                    alertDialogBuilder.setView(numberPicker);
                }
                alertDialogBuilder.setMessage("Choose a value")
                        .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                byte predictedTricks = (byte)numberPicker.getValue();
                                me.updatePredictedTricks(predictedTricks);
                                sendPredictedTricks();
                                //resultActivity.setPredictedTricks(predictedTricks);
                                Log.d(LOG_TAG,predictedTricks+"");
                                myTricksTable.append("\n"+"My Tricks: "+me.getPredictedTrick());
                                alertDialog.dismiss();

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //OnClickListener to open Alert Dialog to show the actual points of the players
        pointsView = new TextView(GameActivity.this);

        pointsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(GameActivity.this);
                alertDialogBuilder2.setTitle("Actual points of all palyers ");
                //to remove textView from its parents (Otherwise Exception child already has a parent)
                if(pointsView.getParent() != null){
                    ((ViewGroup)pointsView.getParent()).removeView(pointsView);
                    alertDialogBuilder2.setView(pointsView);
                }else {
                    alertDialogBuilder2.setView(pointsView);
                }
                alertDialogBuilder2.setMessage("")
                        .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog2.dismiss();
                            }
                        });
                alertDialog2 = alertDialogBuilder2.create();
                alertDialog2.show();
            }
        });
    }

    private void setMyHand(byte[] cards) {
        myHand = new Hand();
        myHand = cardAdapter.getMyhand(cards);
        tricks = false;
        predictTricksBtn.setVisibility(View.VISIBLE);
        writeTricksBtn.setVisibility(View.VISIBLE);
        myTricksTable.setText("Predicted Tricks this round");
        tricksTable.setText("");
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
        if (accelationSquareRoot >= 3) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            if(!JoinGameActivity.owner){
                messageHandler.sendEvent(Server.CHEAT,zero,zero,zero);
            } else {
                openCheatPopUp(game.getPlayedCards());
            }
            //openCheatPopUp("");
            //enemyCards = testGame.getCardsOfRandomPlayer();
            //String[] splitted = enemyCards.split(";");
            // String[] cardsFromOtherPlayer = {"2_Blue", "7_Green", "8_Yellow"};

            /*
            if(JoinGameActivity.owner) {
                //cheatString = Game.outHandedCards.get(0);
                cheatString = "hosttest";
                openCheatPopUp(Game.outHandedCards.get(0));
            } else {
                messageHandler.sendEvent(Server.CHEAT,zero,zero,zero);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/


        }
    }

    public void openCheatPopUp(String value) {
        Log.d("In cheatpopup: ", value);
        /*if(!cD.isActive) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                myVibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                myVibrator.vibrate(500);
            }
            cD.isActive = true;
            Hand testHand = new Hand();
            testHand.addCardToHand(new Card(3,3));
            testHand.addCardToHand(new Card(4,3));
            testHand.addCardToHand(new Card(5,3));
            testHand.addCardToHand(new Card(6,3));
            testHand.addCardToHand(new Card(7,1));
            testHand.addCardToHand(new Card(8,1));
            testHand.addCardToHand(new Card(2,2));
            cD.setHandToShow(testHand);
            cD.setTitle(value);
            //cD.show();


        }*/
        if(!isPopUpActive) {
            AlertDialog.Builder myBuilder = new AlertDialog.Builder(GameActivity.this);
            myBuilder.setTitle("Cards handed out: ");
            //List<String> list;
            myBuilder.setMessage(value);
            //myBuilder.setMessage(cheatString);
            myBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //myVibrator.cancel();
                    isPopUpActive = false;
                }
            });
            myBuilder.setIcon(android.R.drawable.ic_dialog_info);
            AlertDialog myDialog = myBuilder.create();
            myDialog.show();
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
        myTricksTable.setText("Predicted Tricks this round"+"\n"+"My Tricks: "+me.getPredictedTrick());
        sendPredictedTricks();
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d(LOG_TAG,"negative click");
    }

    //sends the predicted tricks to the other players
    public void sendPredictedTricks(){

       if(!tricks){
            messageHandler.write(Server.TRICKS,me.getPlayerName()+" has predicted "+me.getPredictedTrick()+" tricks");
            tricks = true;
            predictTricksBtn.setVisibility(View.INVISIBLE);
            writeTricksBtn.setVisibility(View.INVISIBLE);
        }else {
            toast("It's not your turn to predict tricks or you have already predicted");
        }
    }
}