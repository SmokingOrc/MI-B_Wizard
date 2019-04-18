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
    private List<Player> _players;
    //will be a deck after merge
    private List<String> deck;
    private Deck _deck;

    private int currentRound;
    //will be a KVP with Player and Card after merge
    private Map<String,Integer> cardsPlayed;
    private Map<Player, Card> _cardsPlayed;

    public Game() {
        players = new ArrayList<String>();
        deck = new ArrayList<String>();
        cardsPlayed = new HashMap<String, Integer>();
        _cardsPlayed = new HashMap<Player, Card>();
        _deck = new Deck();
        _players = new ArrayList<Player>();


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

    public List<Player> get_players() {
        return _players;
    }

    public void set_players(List<Player> _players) {
        this._players = _players;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    //end of Getter/Setter
    public void addPlayer(String player) {
        this.players.add(player);
    }

    public void addPlayerToPlayers(Player toAdd) {
        this._players.add(toAdd);
    }

    public String removePlayer(String player) {
        String returnValue = null;
        if(this.players.contains(player)) {
            this.players.remove(player);
            returnValue = player;
        }

        return returnValue;
    }


    //network method - receiving message from player with card
    public void addCardToCardsPlayed(Player player, Card card) {
        //this.cardsPlayed.put(player, card);
        this._cardsPlayed.put(player, card);

    }

    public Player calculateWhoWonTheRound() {
        Player returnValue;
        Map.Entry<Player, Card> currentHighestCard = null;
        Card c;
        Card c1;
        Iterator it = _cardsPlayed.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(currentHighestCard == null) {
                currentHighestCard = pair;
            } else {
                c = (Card)pair.getValue();
                c1 = (Card)currentHighestCard.getValue();
                if(c.getRank().ordinal() > c1.getRank().ordinal()) {
                    currentHighestCard = pair;
                }
            }
        }

        returnValue = currentHighestCard.getKey();


        return returnValue;
    }
    //Network method - send message here
    public void handOutCards() {
        /*
        needs to be updated when "Deck" and "Player" are implemented
         */
        for(Player player : _players) {
            player.setHand(_deck.getCards(currentRound));
            //Network must be used here
        }
    }

    public void calculatePointsForAllPlayers() {
        //needs to be implemented when "Player" is implemented
        for(Player p : _players) {
            p.setPoints(p.getPoints() + calculatePointsForOnePlayer(p.getPredictedTrick(), p.getMadeTrick()));
        }
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
