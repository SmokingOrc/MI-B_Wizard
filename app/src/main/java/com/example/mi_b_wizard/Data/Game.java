package com.example.mi_b_wizard.Data;

import android.util.Pair;

import com.example.mi_b_wizard.GameActivity;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private String ID;
    private MessageHandler messageHandler;
    private GameActivity gameActivity = GameActivity.getGameActivity();
    private ArrayList<Integer> ids = new ArrayList<Integer>();
    private List<Player> _players;
    private int round = 1;
    private int roundsToGo = ids.size()+1;
    private int playedRounds = 0;
    private int maxRounds;
    private int turns = 0;
    private byte n = 0;
    private boolean rightNumberOfPlayers = true; // testing
    public static int timeToPlay = 0;

    private Deck _deck;

    private Colour trump;

    private int currentRound;

    private Map<Player, Card> _cardsPlayed;

    public Game() {

        _cardsPlayed = new LinkedHashMap<Player, Card>();
        _deck = new Deck();
        _players = new ArrayList<Player>();


    }

    //getter and setter for testing
    private void setMaxRounds(int numberOfPlayers){
        if(numberOfPlayers < 7 && numberOfPlayers > 0){
            rightNumberOfPlayers = true;
        if(numberOfPlayers == 3){
            maxRounds = 20;}
        else if(numberOfPlayers == 4){
                maxRounds = 15; }
        else if(numberOfPlayers == 5) {
            maxRounds = 12;
        }else{
            maxRounds = 10;
        }
    }}

    public void setIds() {
        this.ids = messageHandler.getId();
    }

    public Colour getTrump() {
        return trump;
    }

    public void setTrump(Colour trump) {
        this.trump = trump;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
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

    public void whoIsNext(){
        System.out.println("turns : "+turns+" ids: "+ids.size());

            if(turns < ids.size()){
                int nextPlayer = ids.get(turns);
            System.out.println(ids.get(turns));
            messageHandler.sendEventToTheSender(Server.YOUR_TURN,n,n,n,nextPlayer);
            System.out.println("other players turn");
        }else if (turns >= ids.size()){
            turns = -1; // its hosts turn
            gameActivity.MyTurn();
            System.out.println("hosts turn");
        }
        turns++;
    }

    public void endOfTheRound(){

    }
    //network method - receiving message from player with card


    public void addCardToCardsPlayed(Player player, Card card) {
        //this.cardsPlayed.put(player, card);
        this._cardsPlayed.put(player, card);

    }

    //network - send
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
            player.getHand().setHand(_deck.getCards(currentRound));
            //Network - send
        }
        //network - send
        trump = _deck.getCards(1).get(0).getColour();
    }

    public void sendcards() {
        byte[] host;// for testing
        playedRounds++;
        if(playedRounds <= roundsToGo && rightNumberOfPlayers){
        int playerId;
            for (int i = 0; i < ids.size(); i++) {
                playerId = ids.get(i);
                byte[] cardsToSend = new byte[21];
                host = cardsToSend; // for testing
                cardsToSend[0] = Server.CARDS;

                for (int j = 1; j < round + 1; j++) {
                    cardsToSend[j] = 1; // random card...
                }
                messageHandler.sendCardsToPlayer(cardsToSend, playerId);
                System.out.println("cards sent to players from game class");
                gameActivity.takeCards(host);   //Host takes the cards from the card class
            }
            round++;
    }else {
            System.out.println("new round");
            playedRounds = 0;
            roundsToGo = (ids.size()+1)*round; // its a new round
            sendcards();
        }}

    public void moveMade(byte cardPlayed, int playerID){
        System.out.println("card "+cardPlayed+" played from player with id : "+playerID);
        whoIsNext();
    }

    public void hostMadeAMove(byte cardPlayed){
        System.out.println("I played a card : "+cardPlayed);
        whoIsNext();
    }

    //network - send
    public void calculatePointsForAllPlayers() {
        //needs to be implemented when "Player" is implemented
        for(Player p : _players) {
            p.setPoints(p.getPoints() + calculatePointsForOnePlayer(p.getPredictedTrick(), p.getMadeTrick()));
            System.out.println("Points for " + p.getPlayerName() + ": " + p.getPoints());
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
