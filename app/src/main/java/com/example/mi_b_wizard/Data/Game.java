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
    private int minPlayers = 1;  // when testing with 2 devices change the value of minPlayers.
    private int maxPlayers = 6;
    private boolean rightNumberOfPlayers = false;
    private Map<Byte, Integer> playedCards = new HashMap<>();

    // Setters&Getters for tests
    public void setIdsTest(ArrayList<Integer> id) {
        ids = id;
    }

    public void setTrumpThisRound(byte trumpThisRound) {
        this.trumpThisRound = trumpThisRound;
    }

    public String getPlayedCards() {
        return cardAdapter.getPlayerCards();
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

    public boolean isRightNumberOfPlayers() {
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
            gameActivity.newRound();
            messageHandler.sendEvent(Server.NEW_ROUND, n, n, n);
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
            if (cardPlayed != (byte) 31 || cardPlayed != (byte) 46 || cardPlayed != (byte) 61 || cardPlayed != (byte) 16) {
                firstCardThisTurn = cardPlayed;
                System.out.println("new first/high card");
            }
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
        int playedFirstCard = 0;

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
        if (!playedTrump && !highCardPlayed && !winner) {
            id = playedFirstCard;
            System.out.println("jester");
        }
        winner(id);
        playedCards.clear();
    }

    private void winner(int id) {
        if (id != 0) {
            messageHandler.sendEventToTheSender(Server.WINNER, n, n, n, id);
            setTurnCounter(id);
            System.out.println("win player");
        } else {
            gameActivity.showWhoIsTheWinner();
            turns = 0;
            System.out.println("win host");
        }
    }

    private void setTurnCounter(int id) {
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == id) {
                turns = i + 1;
            }
        }
    }
}