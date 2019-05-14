package com.example.mi_b_wizard;

import android.annotation.SuppressLint;
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

public class GameActivity extends AppCompatActivity {
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
}