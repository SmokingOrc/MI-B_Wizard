package com.example.mi_b_wizard.Data;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Game {
    private String ID;
    //will be a list of player after merge
    private List<String> players;
    //will be a deck after merge
    private List<String> deck;
    //will be a KVP with Player and Card after merge
    private Map<String,Integer> cardsPlayed;

    public Game() {
        players = new ArrayList<String>();
        deck = new ArrayList<String>();
        cardsPlayed = new HashMap<String, Integer>();


    }

    //getter and setter for testing
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public List<String> getDeck() {
        return deck;
    }

    public void setDeck(List<String> deck) {
        this.deck = deck;
    }

    public Map<String, Integer> getCardsPlayed() {
        return cardsPlayed;
    }

    public void setCardsPlayed(Map<String, Integer> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }

    public void addPlayer(String player) {
        this.players.add(player);
    }

    public String removePlayer(String player) {
        String returnValue = null;
        if(this.players.contains(player)) {
            this.players.remove(player);
            returnValue = player;
        }

        return returnValue;
    }

    public void addCardToCardsPlayed(String player, Integer card) {
        this.cardsPlayed.put(player, card);
    }

    public String calculateWhoWonTheRound() {
        String returnValue = "";
        Map.Entry<String, Integer> currentHighestCard = null;
        Iterator it = cardsPlayed.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(currentHighestCard == null) {
                currentHighestCard = pair;
            } else {
                if((Integer)pair.getValue() > (Integer)currentHighestCard.getValue()) {
                    currentHighestCard = pair;
                }
            }
        }

        returnValue = currentHighestCard.getKey();


        return returnValue;
    }

    public void handOutCards() {
        /*
        needs to be updated when "Deck" and "Player" are implemented
         */
        for(String player : players) {
            //player.giveCards(deck.getCards(currentRound));

        }
    }

    public void calculatePointsForAllPlayers() {
        //needs to be implemented when "Player" is implemented
    }

    public int calculatePointsForOnePlayer(int predicted, int made) {
        int points = 0;

        if(predicted == made) {
            points = 20 + made * 10;
        } else if (predicted < made){
            points = (predicted - made) * 10;
        } else {
            points = (made - predicted) * 10;
        }

        return  points;
    }



}
