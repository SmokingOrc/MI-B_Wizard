package com.example.mi_b_wizard.Data;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

    private Card.Colour trump;

    private int currentRound;
    //will be a KVP with Player and Card after merge
    private Map<String,Integer> cardsPlayed;
    private Map<Player, Card> _cardsPlayed;

    public Game() {
        players = new ArrayList<String>();
        deck = new ArrayList<String>();
        cardsPlayed = new HashMap<String, Integer>();
        _cardsPlayed = new LinkedHashMap<Player, Card>();
        _deck = new Deck();
        _players = new ArrayList<Player>();


    }

    //getter and setter for testing


    public Card.Colour getTrump() {
        return trump;
    }

    public void setTrump(Card.Colour trump) {
        this.trump = trump;
    }

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

    public void removePlayerFromPlayers(Player toRemove) throws Exception {
        if(this._players.contains(toRemove)) {
            this._players.remove(toRemove);
        } else {
            throw new Exception("Player to remove: " + toRemove.getPlayerName() + " is not in Collection");
        }

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
        Card currentHighest;
        Iterator it = _cardsPlayed.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Player,Card> pair = (Map.Entry)it.next();
            if(currentHighestCard == null) {
                currentHighestCard = pair;
                System.out.println("First Card is: " + currentHighestCard.getValue().getId() +
                        " from: " + currentHighestCard.getKey().getPlayerName());
            } else {
                c = pair.getValue();
                currentHighest = currentHighestCard.getValue();
                //check if first card is a wizard
                if(currentHighest.getRank().ordinal() == 9) {
                    currentHighestCard.getKey().setMadeTrick(currentHighestCard.getKey().getMadeTrick() + 1);
                    return currentHighestCard.getKey(); //first wizard wins
                } else if(c.getRank().ordinal() == 9) {
                    currentHighestCard = pair;
                    currentHighestCard.getKey().setMadeTrick(currentHighestCard.getKey().getMadeTrick() + 1);
                    return currentHighestCard.getKey();
                } else if(currentHighest.getRank().ordinal() == 0 && c.getRank().ordinal() > 0) {
                    currentHighestCard = pair;
                } else if (c.getRank().ordinal() == 0) {
                    //do nothing because narr
                } else if(currentHighest.getColour() == this.trump && c.getColour() == trump) {
                    if(c.getRank().ordinal() > currentHighest.getRank().ordinal()) {
                        currentHighestCard = pair;
                    }
                } else if (currentHighest.getColour() != this.trump && c.getColour() == trump) {
                    currentHighestCard = pair;
                } else if (currentHighest.getColour() == c.getColour()) {
                    if(c.getRank().ordinal() > currentHighest.getRank().ordinal()) {
                        currentHighestCard = pair;
                    }
                }
            }
        }

        returnValue = currentHighestCard.getKey();
        returnValue.setMadeTrick(returnValue.getMadeTrick() + 1);

        return returnValue;
    }
    //Network method - send message here
    public void handOutCardsAndSetTrump() {
        /*
        needs to be updated when "Deck" and "Player" are implemented
         */
        for(Player player : _players) {
            player.setHand(_deck.getCards(currentRound));
            //Network must be used here
        }
        trump = _deck.getCards(1).get(0).getColour();
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
