package com.example.mi_b_wizard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Data.CardAdapter;
import com.example.mi_b_wizard.Data.Game;
import com.example.mi_b_wizard.Data.Hand;
import com.example.mi_b_wizard.Data.Player;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

import java.util.ArrayList;


public class GameActivity extends AppCompatActivity implements RecognitionListener, PredictedTricksDialogFragment.NoticeDialogListener {
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
    }

    public void showMove(Byte cardPlayed){
        playedcard = cardAdapter.getThisCard(cardPlayed);
        toast("Card : "+ playedcard.getRank()+" in "+playedcard.getColour()+" was played ");
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
    }

    public void takeCards(byte[] cards){
        String s = cardAdapter.myStringCards(cards);
        setMyHand(cards);
        myCard.setText(s);
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


        startAndSendCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JoinGameActivity.owner) {
                    PlayersStart();
                    messageHandler.sendEvent(Server.START_GAME,zero,zero,zero);
                    game.sendcards();
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
                if (myTurn && JoinGameActivity.owner) {
                  messageHandler.sendEvent(Server.MOVE,myHand.getFirstCardInHand(),zero,zero);
                  notMyTurnAnymore();
                  game.hostMadeAMove(myHand.getFirstCardInHand());
                  myHand.removeFristcard();

                }else if(myTurn){
                    messageHandler.sendEvent(Server.MOVE,myHand.getFirstCardInHand(),zero,zero);
                    myHand.removeFristcard();
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

