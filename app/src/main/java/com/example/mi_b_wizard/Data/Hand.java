package com.example.mi_b_wizard.Data;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Hand{
    //public int count=0;
    private String tag = "Hand";

    public List<Card> handCards;

    public Hand() {
        handCards = new ArrayList<>();
    }

    //adds a card to the hand
    public void addCardToHand(Card card){
        Log.i(tag,"card "+card.getRank()+" in "+card.getColour()+" added to your hand");
        handCards.add(card);
    }

    //show all cards in the hand

    public void removeFirstCard(){
        handCards.remove(0);
    }

    public void clearHand(){
        handCards.clear();
    }
    public int getHandSize(){
        return handCards.size();
    }
    public void removeCardFromHand(Card playedcard){
        handCards.remove(playedcard);
    }
    public void setHand(List<Card> hand) {
        this.handCards = hand;
    }
    public List<Card> getHand(){
        return handCards;
    }

    public byte getCardInHand(int i){
        return handCards.get(i).getId();
    }
}