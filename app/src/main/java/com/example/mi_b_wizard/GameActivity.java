package com.example.mi_b_wizard;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mi_b_wizard.Data.Game;
import com.example.mi_b_wizard.Data.Hand;
import com.example.mi_b_wizard.Data.Player;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

public class GameActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static GameActivity gameActivity;
    Button startAndSendCards;
    TextView myCard;
    Hand myHand;
    Game game;
    Player me = MainActivity.getPlayer();
    MessageHandler messageHandler;
    private boolean myTurn = false;
    private boolean haveICheated = false;
    private boolean winnerThisRound = false;
    private boolean canWeStart = false;
    byte zero = 0 ;




    public static GameActivity getGameActivity() {
        return gameActivity;
    }

    public void playerMadeAMove(Byte cardPlayed, int playerID){
        game.moveMade(cardPlayed,playerID);
    }

    public void start(){
        canWeStart = true;
    }
    public void setHaveICheated(){
        haveICheated = true;
    }

    public void showWhoIsTheWinner(){
        toast("You won!");
        messageHandler.write(me.getPlayerName()+" won this round");
    }

    public void takeCards(byte[] cards){
        String s = "";
        setMyHand(cards, s);
        System.out.println("player "+me.getPlayerName() +" got cards");
        // hand.add.....
}

    private void playACard(byte cardPlayed){
        if(myTurn && JoinGameActivity.owner){
            game.hostMadeAMove(cardPlayed);
        }else if (myTurn){
            messageHandler.sendEvent(Server.MOVE,cardPlayed,zero,zero);
            myTurn = false;

        }
    }
    public void showPoints(byte[] playerPoints){
        String s ="";
            getPlayerPoints(playerPoints, s);
            System.out.println("player "+me.getPlayerName() +" got points");
        // pointShow(points).....
    }

    public void PlayersStart(){
        messageHandler.sendEvent(Server.START_GAME,zero,zero,zero);
    }

    public void MyTurn(){
        myTurn = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameActivity = this;

        startAndSendCards = findViewById(R.id.StartWithCards);
        myCard = findViewById(R.id.mycard);

        if (JoinGameActivity.owner) {
            game = new Game();
            messageHandler = MessageHandler.messageHandler();
            game.setMessageHandler(messageHandler);
            game.setIds();
            myTurn = true;
            startAndSendCards.setText("GIVE OUT CARDS");
        }


        startAndSendCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JoinGameActivity.owner) {
                    game.sendcards();
                    PlayersStart();

                } else {
                    toast("You have to wait for host to give out the cards..");
                }
            }
        });


    }

    private void getPlayerPoints(byte[] playerPoints, String s) {
        for (int i = 1; i < 7; i++) { // max 6 players
            if (playerPoints[i] != 0) {
                System.out.println(playerPoints[i]);
                s += playerPoints[i] + ", ";
            } else {
                break;
            }
            myCard.setText(s);
        }
    }

    private void setMyHand(byte[] cards, String s) {
        myHand = new Hand();
        for (int i = 1; i < 21; i++) { // max 20 cards
            if (cards[i] != 0) {
                System.out.println(cards[i]);
                //hand.add..
                s += cards[i] + ", ";
            } else {
                break;
            }
            myCard.setText(s);
        }
    }
    public void toast(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    }

