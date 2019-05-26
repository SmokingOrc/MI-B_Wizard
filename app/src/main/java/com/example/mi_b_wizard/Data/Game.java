package com.example.mi_b_wizard.Data;

import com.example.mi_b_wizard.GameActivity;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Game {
    private int turnsCount = 0;
    private byte[] magicians = {30, 45, 60, 75};
    private int playersPlayedThisRound = 0;
    private MessageHandler messageHandler;
    private CardAdapter cardAdapter = new CardAdapter();
    private Deck deck = new Deck();
    private GameActivity gameActivity = GameActivity.getGameActivity();
    private ArrayList<Integer> ids = new ArrayList<Integer>();
    private byte firstCardThisTurn = 0;
    private byte trumpThisRound;
    private int round = 1;
    private int turnsToGo = ids.size() + 1;
    private int playedRounds = 0;
    private int maxRounds;
    private int turns = 0;
    private byte n = 0;
    private int host = 0;
    private int minPlayers = 3;  // when testing with 2 devices change the value of minPlayers.
    private int maxPlayers = 6;
    private boolean rightNumberOfPlayers = false;
    private Map<Byte, Integer> playedCards = new HashMap<>();
    public static Map<Integer, String> outHandedCards = new HashMap<>();

    // Setters&Getters for tests
    public void setIdsTest(ArrayList<Integer> id) {
        ids = id;
    }

    public void setTrumpThisRound(byte trumpThisRound) {
        this.trumpThisRound = trumpThisRound;
    }

    public Map<Byte, Integer> getPlayedCards() {
        return playedCards;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void setIds() {
        this.ids = messageHandler.getId();
    }


    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
    public boolean isRightNumberOfPlayers(){
        return rightNumberOfPlayers;
    }


    private void setMaximalRounds(int numberOfPlayers) {
        if (numberOfPlayers <= maxPlayers && numberOfPlayers >= minPlayers) {
            rightNumberOfPlayers = true;
            if (numberOfPlayers == 3) {
                maxRounds = 20;
            } else if (numberOfPlayers == 4) {
                maxRounds = 15;
            } else if (numberOfPlayers == 5) {
                maxRounds = 12;
            } else {
                maxRounds = 10;
            }
        }
    }

    private void whoIsNext() {
        if (turnsCount == ((ids.size() + 1) * round) && round < maxRounds) {
            nextRound();
            outHandedCards.clear();
        } else if (playersPlayedThisRound == ids.size() + 1) {
            nextTurn();
        } else {
            findNext();
        }
    }

    private void nextRound() {
        turnsCount = 0;
        round++;
        playersPlayedThisRound = 0;
        whoWonThisRound();
        waitALittleBit();
        sendCards();
        gameActivity.showMyPoints();
    }

    private void waitALittleBit() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void nextTurn() {
        whoWonThisRound();
        waitALittleBit();
        playedCards.clear();
        //gameActivity.clearView();
        playersPlayedThisRound = 0;
        firstCardThisTurn = 0;
    }

    private void findNext() {
        if (turns < ids.size()) {
            messageHandler.sendEventToTheSender(Server.YOUR_TURN, n, n, n, ids.get(turns));
            System.out.println("other players turn");
            turns++;
        } else {
            gameActivity.MyTurn();
            turns = 0;
        }
    }

    public void sendCards() {
        setMaxRounds();
        if (rightNumberOfPlayers) {
            playedRounds++;
            findAndSendTrump();

            waitALittleBit();

            int playerId;
            for (int i = 0; i < ids.size(); i++) {
                playerId = ids.get(i);
                byte[] cardsToSend;
                cardsToSend = cardAdapter.getByteCards(round);
                outHandedCards.put(playerId, cardsToSend.toString());
                messageHandler.sendCardsToPlayer(cardsToSend, playerId);
                System.out.println("cards sent to players from game class");
            }
            gameActivity.takeCards(cardAdapter.getByteCards(round));

        } else if (round != 1) {
            turnsToGo = (ids.size() + 1) * round;
            sendCards();
        } else {
            System.out.println("Wrong number of players");
            throw new IllegalStateException();
        }
    }

    private void setMaxRounds() {
        if (maxRounds == 0) {
            setMaximalRounds(ids.size() + 1);
        }
    }

    private void findAndSendTrump() {
        trumpThisRound = cardAdapter.getTrump();
        gameActivity.setTrump(trumpThisRound);
        messageHandler.sendEvent(Server.TRUMP, trumpThisRound, n, n);
    }

    public void moveMade(byte cardPlayed, int playerID) {
        System.out.println("card " + cardPlayed + " played from player with id : " + playerID);
        playedCards.put(cardPlayed, playerID);
        setFirstCard(cardPlayed);
        findNextPlayer();
    }

    private void setFirstCard(byte cardPlayed) {
        if (playersPlayedThisRound == 0) {
            if(cardPlayed != (byte)31 || cardPlayed != (byte)46 || cardPlayed != (byte)61 || cardPlayed != (byte)16 ){
            firstCardThisTurn = cardPlayed;
            System.out.println("new first/high card");}
        }
    }

    public void hostMadeAMove(byte cardPlayed) {
        System.out.println("I played a card : " + cardPlayed);
        playedCards.put(cardPlayed, host);
        setFirstCard(cardPlayed);
        findNextPlayer();
    }

    private void findNextPlayer() {
        playersPlayedThisRound++;
        turnsCount++;
        whoIsNext();
    }

    private void whoWonThisRound() {
        int id = 0;
        byte highTrump = 0;
        byte highCard = 0;
        int color = gameActivity.getColor(trumpThisRound);
        int otherCardColor = gameActivity.getColor(firstCardThisTurn);
        boolean winner = false;
        boolean playedTrump = false;
        boolean highCardPlayed = false;
        int playedFirstCard =0;

        Iterator it = playedCards.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Byte, Integer> nextCard = (Map.Entry) it.next();
            System.out.println(playedCards);

            for (int i = 0; i < magicians.length; i++) { // is the card a magician
                if (nextCard.getKey() == magicians[i]) {
                    id = playedCards.get(magicians[i]);
                    System.out.println(id);
                    winner = true;
                    System.out.println("magician");
                }
            }
            if (!winner) {
                if (nextCard.getKey() > ((color + 1) * 15 + 2) && nextCard.getKey() < (((color + 1) * 15 + 1) + 15)) {
                    if (highTrump < nextCard.getKey()) {
                        highTrump = nextCard.getKey();
                        id = nextCard.getValue();
                        playedTrump = true;
                        System.out.println("trump");
                    }
                } else if (nextCard.getKey() > ((otherCardColor + 1) * 15 + 2) && nextCard.getKey() < ((((otherCardColor + 1) * 15 + 1) + 15)) && !playedTrump) {
                    if (highCard < nextCard.getKey()) {
                        highCard = nextCard.getKey();
                        id = nextCard.getValue();
                        System.out.println("high");
                        highCardPlayed = true;
                    }
                }
            }
        }
        if (!playedTrump && !highCardPlayed && !winner){
            id = playedFirstCard;
            System.out.println("jester");
        }
        winner(id);
        playedCards.clear();
        //gameActivity.clearView();
    }

    private void winner(int id) {
        if (id != 0) {
            messageHandler.sendEventToTheSender(Server.WINNER, n, n, n, id);
            setTurnCounter(id);
        } else {
            gameActivity.showWhoIsTheWinner();
            gameActivity.madeTrickUpdate();
            turns = 0;
        }
    }

    private void setTurnCounter(int id) {
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == id) {
                turns = i + 1;
            }
        }
    }

    /********************************* OLD ******************************************************
     *+++++++++++++++++++++++++++++++ NOT IN USE ************************************************/
/*
    private Deck _deck;
    private String ID;
    private List<Player> _players;
    private Colour trump;

    private int currentRound;

    private Map<Player, Card> _cardsPlayed;

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


    public Game() {

        _cardsPlayed = new LinkedHashMap<Player, Card>();
        _deck = new Deck();
        _players = new ArrayList<Player>();

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

        for(Player player : _players) {
            player.getHand().setHand(_deck.getCards(currentRound));
            //Network - send
        }
        //network - send
        trump = _deck.getCards(1).get(0).getColour();
    }
    public String getCardsOfRandomPlayer() {
        String returnValue = "";
        Random r = new Random();

        returnValue = "Hans;Blue_5,Yellow_9,Blue_0,Red_3";
        return returnValue;
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

*/
}
