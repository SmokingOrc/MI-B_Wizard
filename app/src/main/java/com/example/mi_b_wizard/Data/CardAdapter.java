package com.example.mi_b_wizard.Data;

import com.example.mi_b_wizard.Network.Server;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter {
    Deck deck = new Deck();
    List<Card> handCards = new ArrayList<Card>();
    List<String> playercards = new ArrayList<String>();

    public String getPlayerCards(){
        return playercards.toString();
    }

    public void resetDeck(){
        deck.resetDeck();
    }

    public byte[] getByteCards(int round){
        byte[] cards = new byte[round+2];
        handCards = deck.getCards(round);
        playercards.add(handCards.toString());
        cards[0] = Server.CARDS;
        for (int i = 1; i <handCards.size()+1 ; i++) {
            cards[i] = handCards.get(i-1).getId();
        }
        return cards;
    }

    public byte getTrump(){
         playercards.clear();
        return deck.getTrump();
    }


    public String myStringCards(byte[] mybytecards){
        String mycard;
        mycard = deck.getMyCardsToString(mybytecards);
        return mycard;
    }

    public String myStringCard(byte myByteCard){
        String myCard;
        myCard= deck.getMyCardToString(myByteCard);
        return myCard;

    }

    public Hand getMyhand(byte[] cards){
        return deck.getMyHand(cards);
    }

    public Card getThisCard(byte card){
        return deck.getThisCard(card);
    }
}
