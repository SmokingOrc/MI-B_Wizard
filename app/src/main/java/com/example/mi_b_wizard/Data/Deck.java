package com.example.mi_b_wizard.Data;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Deck {

    Map<String, Card> card = new HashMap<>();

    public Deck() {
        resetDeck();
    }

    /**
     * resetDeck() initializes and resets all the Cards in the HashMap
     */

    public void resetDeck() {


        for (int rank = 0; rank < 10; rank++)               //give rank IDs from 0 - 9
            for (int color = 0; color < 4; color++) {       //give color IDs from 0 - 3
                Card c = new Card(rank, color);             //create new Cards identified by rank and color
                card.put(c.getId(), c);                     //add new created Cards into HashMap
            }

    }


    /**
     * getCards method takes the input currentRound
     * to generate a List<String> with the number of
     * random cards equal to current Round
     */

    public List<Card> getCards(int currentRound) {
        List<Card> handCards = new ArrayList<Card>();


        int i = 0;

        while (i < currentRound) {
            Object[] rand = card.keySet().toArray();                //keySet() is used to get a view of the keys contained in the HashMap --> get the key entries from the HashMap and "change" into an Array
            Object key = rand[new Random().nextInt(rand.length)];   //get a random key for a card
            Card c = card.get(key);                                 //get ID from the Card with the generated key
            handCards.add(c);                                       //add Card into ArrayList HandCards
            card.remove(key);                                       //remove this Card from the HashMap card, so that the getCards Method gives different Cards every time it is called, until the Method resetDeck() is called
            i++;
        }


        return handCards;
    }


    /**
     * to get Size of Deck - for testing
     */

    public int deckSize(){
        return card.size();
    }


    /**
     * to get Number of Cards which are still available - for testing
     */

    public int availableCards(){
        return card.size();
    }



    /**
     * Getter and Setter for testing
     */

    public Map<String, Card> getCard() {
        return card;
    }

    public void setCard(Map<String, Card> card) {
        this.card = card;
    }


}


