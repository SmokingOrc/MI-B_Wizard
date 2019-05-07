package com.example.mi_b_wizard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mi_b_wizard.Data.Player;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

public class GameActivity extends AppCompatActivity {
    Button giveMeACard;
    TextView myCard;
    Player me = Instance.player;
    MessageHandler messageHandler = Instance.MH;
    byte zero = 0 ;

    public void takeCards(byte card,byte number){
        myCard.setText("your card is "+card+" with number "+number);
        System.out.println("got cards");
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        giveMeACard = findViewById(R.id.giveMe);
        myCard = findViewById(R.id.mycard);
        Instance.setGameActivity(this);

        giveMeACard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!JoinGameActivity.owner) {
                    messageHandler.sendEvent(Server.GIVE_ME_CARDS, zero, zero, zero);
                }else {
                    // TODO Send cards from Game class.
                    takeCards((byte)1,(byte)1);
                }
            }
        });
    }
}
