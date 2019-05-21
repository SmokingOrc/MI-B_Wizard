package com.example.mi_b_wizard.Data;

import com.example.mi_b_wizard.Network.Server;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter {
    Deck deck = new Deck();
    List<Card> handCards = new ArrayList<Card>();

    public void resetDeck(){
        deck.resetDeck();
    }

    public byte[] getByteCards(int round){
        System.out.println(round);
        byte[] cards = new byte[round+2];

        handCards = deck.getCards(round);
        System.out.println(handCards);
        cards[0] = Server.CARDS;
        for (int i = 1; i <handCards.size()+1 ; i++) {
            cards[i] = handCards.get(i-1).getId();
        }
        return cards;
    }

    public byte getTrump(){
        return deck.getTrump();
    }


    public String myStringCards(byte[] mybytecards){
        String mycard;
        mycard = deck.getMyCardsToString(mybytecards);
        return mycard;
    }

    public Hand getMyhand(byte[] cards){
        return deck.getMyHand(cards);
    }

    public Card getThisCard(byte card){
        return deck.getThisCard(card);
    }
}
