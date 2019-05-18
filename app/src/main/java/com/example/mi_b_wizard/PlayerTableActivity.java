package com.example.mi_b_wizard;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Data.Deck;
import com.example.mi_b_wizard.Data.ViewCards;

import java.util.ArrayList;
import java.util.List;

public class PlayerTableActivity extends AppCompatActivity implements View.OnClickListener {


    ArrayList<ViewCards> handCards;
    ViewCards cardview;
    Card card;
    Deck deck;



    LinearLayout cardHand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_table);


        handCards = new ArrayList<>();
    }

    private void getThoseCards(int currentRound, List<Card> cards1) {




        for (int i = 0; i < currentRound; i++) {
            card = cards1.get(i);
            addImageToScrollView(card);

        }
    }


    /**
     * Is used to add Images to Scroll View
     * Images are getting connected to Cards in "ViewCards" Class
     *
     */
    private void addImageToScrollView(Card card) {
        cardHand = findViewById(R.id.cardHand);
        cardview = new ViewCards(PlayerTableActivity.this,this,card);
        handCards.add(cardview);
        cardHand.addView(cardview.view);

    }




    @Override
    public void onClick(View v) {

        int currentRound = 15;

        List<Card> cards2 = new ArrayList<Card>();



        //TODO insert getCards(currentRound) method instead of creating new Cards manually


        /*cards2.add(new Card(0,0));
        cards2.add(new Card(1,1));
        cards2.add(new Card(2,2));
        cards2.add(new Card(3,3));
        cards2.add(new Card(4,0));
        cards2.add(new Card(5,1));
        cards2.add(new Card(6,2));
        cards2.add(new Card(7,3));
        cards2.add(new Card(8,0));
        cards2.add(new Card(9,1));
        cards2.add(new Card(10,2));
        cards2.add(new Card(11,3));
        cards2.add(new Card(12,0));
        cards2.add(new Card(13,1));
        cards2.add(new Card(14,2));*/



        //cards2 = (new Card(rank.getRank(),colour.getColour()));



        cards2.addAll(deck.getCards(currentRound));





        getThoseCards(currentRound, cards2);


    }


}


