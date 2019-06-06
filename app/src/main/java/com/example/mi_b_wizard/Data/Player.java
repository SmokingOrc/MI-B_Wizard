package com.example.mi_b_wizard.Data;


import android.util.Log;


public class Player{

    private String playerName;
    private int points = 0;
    private int madeTrick = 0; // tricks, which were made (gemachte Stiche)
    private byte predictedTrick; //tricks, which were predicted (angesagte Stiche)
    private Hand hand; //actual Hand of the player
    private Card actualPlayedCard;
    private int playerId;
    private byte checkedPredictedTricks;

    public Player(String playerName){
        setPlayerName(playerName);
        hand = new Hand();
    }

    //Getter-Setter


    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerName(String name){
        this.playerName = name;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getMadeTrick() {
        return madeTrick;
    }

    public void setMadeTrick(int madeTrick) {
        this.madeTrick = madeTrick;
    }

    public void madeATrick(){ madeTrick ++;
        Log.d("Tag","You made a trick");
    }
    public byte getPredictedTrick() {
        return predictedTrick;
    }

    public void setPredictedTrick(byte predictedTrick) {
        this.predictedTrick = predictedTrick;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Card getActualPlayedCard() {
        return actualPlayedCard;
    }

    public void setActualPlayedCard(Card actualPlayedCard) {
        this.actualPlayedCard = actualPlayedCard;
    }
    public byte getCheckedPredictedTricks(){
        return checkedPredictedTricks;
    }

   // <------------------------------------------->

    //Methode to reset for new round
    public void resetForNewRound(){
        hand.clearHand();
        actualPlayedCard = null;
        predictedTrick = 0;
        madeTrick = 0;
    }


    //<------------Interaction with Game-------------->

    public void calculateMyPoints(){

        if(predictedTrick == madeTrick){
            points += 20 + madeTrick * 10;
        }else{
            points -= Math.abs(madeTrick - predictedTrick)*10;
        }
        resetForNewRound();
    }

    //<---------------Action Player-------------------->

    //Method to play Card
    public void playCard(Card card){
        hand.removeCardFromHand(card);
        actualPlayedCard = card;
    }

    //update Predicted Tricks
    public void updatePredictedTricks(Byte predictedTrickS){
        predictedTrick = predictedTrickS;
    }
    //checks the String speech input and turn it into an int
    //contains is need if the input is for e.g. 9 Uhr
    public void checkPredictedTricks(String predictedTrickS) {
        if (predictedTrickS.contains("0") || predictedTrickS.equals("null")) {
            checkedPredictedTricks = 0;
        }
        if (predictedTrickS.contains("1") || predictedTrickS.equals("eins")) {
            checkedPredictedTricks = 1;
        }
        if (predictedTrickS.contains("2") || predictedTrickS.equals("zwei")) {
            checkedPredictedTricks = 2;
        }
        if (predictedTrickS.contains("3") || predictedTrickS.equals("drei")) {
            checkedPredictedTricks = 3;
        }
        if (predictedTrickS.contains("4") || predictedTrickS.equals("vier")) {
            checkedPredictedTricks = 4;
        }
        if (predictedTrickS.contains("5") || predictedTrickS.equals("fünf")) {
            checkedPredictedTricks = 5;
        }
        if (predictedTrickS.contains("6") || predictedTrickS.equals("sechs")) {
            checkedPredictedTricks = 6;
        }
        if (predictedTrickS.contains("7") || predictedTrickS.equals("sieben")) {
            checkedPredictedTricks = 7;
        }
        if (predictedTrickS.contains("8") || predictedTrickS.equals("acht")) {
            checkedPredictedTricks = 8;
        }
        if (predictedTrickS.contains("9") || predictedTrickS.equals("neun")) {
            checkedPredictedTricks = 9;
        }
        if (predictedTrickS.contains("10") || predictedTrickS.equals("zehn")) {
            checkedPredictedTricks = 10;
        }
        if (predictedTrickS.contains("11") || predictedTrickS.equals("elf")) {
            checkedPredictedTricks = 11;
        }
        if (predictedTrickS.contains("12") || predictedTrickS.equals("zwölf")) {
            checkedPredictedTricks = 12;
        }
        if (predictedTrickS.contains("13") || predictedTrickS.equals("dreizehn")) {
            checkedPredictedTricks = 13;
        }
        if (predictedTrickS.contains("14") || predictedTrickS.equals("vierzehn")) {
            checkedPredictedTricks = 14;
        }
        if (predictedTrickS.contains("15") || predictedTrickS.equals("fünfzehn")) {
            checkedPredictedTricks = 15;
        }
        if (predictedTrickS.contains("16") || predictedTrickS.equals("sechzehn")) {
            checkedPredictedTricks = 16;
        }
        if (predictedTrickS.contains("17") || predictedTrickS.equals("siebzehn")) {
            checkedPredictedTricks = 17;
        }
        if (predictedTrickS.contains("18") || predictedTrickS.equals("achtzehn")) {
            checkedPredictedTricks = 18;
        }
        if (predictedTrickS.contains("19") || predictedTrickS.equals("neunzehn")) {
            checkedPredictedTricks = 19;
        }
        if (predictedTrickS.contains("20") || predictedTrickS.equals("zwanzig")) {
            checkedPredictedTricks = 20;
        }
    }
}
