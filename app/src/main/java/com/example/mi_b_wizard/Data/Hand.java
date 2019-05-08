package com.example.mi_b_wizard.Data;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Set;

public class Hand {


    public List<Card> hand;

    public Hand() {
        hand = new ArrayList<Card>();
    }



    //adds a card to the hand
    public void addCardToHand(byte id, Card card){
        hand.add(card);
    }

    //show all cards in the hand
    public void showHand(){
        /*Set keys = hand.keySet();
        for(Object key: keys){
            System.out.println(key +": "+ hand.get(key));
        }
        //Methode only print the keys
        /*System.out.println("Keys: "+keys);*/
    }
    public void clearHand(){
        hand.clear();
    }
    public int getHandSize(){
        return hand.size();
    }
    public void removeCardFromHand(Card playedcard){
        hand.remove(playedcard);
    }
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }
    public List<Card> getHand(){
        return hand;
    }
}