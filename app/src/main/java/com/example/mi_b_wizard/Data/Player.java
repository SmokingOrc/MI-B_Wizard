package com.example.mi_b_wizard.Data;

import android.os.Debug;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Player{

    private String playerName;
    private int points = 0;
    private int madeTrick = 0; // tricks, which were made (gemachte Stiche)
    private int predictedTrick; //tricks, which were predicted (angesagte Stiche)
    private Hand hand; //actual Hand of the player
    private Game game;
    private Card actualPlayedCard;
    private int playerId;

    public Player(String playerName){
        setPlayerName(playerName);


        hand = new Hand();
        game = new Game();

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
        System.out.println("You made a trick");
    }
    public int getPredictedTrick() {
        return predictedTrick;
    }

    public void setPredictedTrick(int predictedTrick) {
        this.predictedTrick = predictedTrick;
    }

    public Hand getHand() {
        return hand;
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

   // <------------------------------------------->

    public void showHand(){
        hand.showHand();
    }
    public void showActualPoints(){
        System.out.println("Your actual points are: "+points);
    }

    //Methode to reset for new round
    public void resetForNewRound(){
        hand.clearHand();
        actualPlayedCard = null;
        predictedTrick = 0;
        madeTrick = 0;
    }


    //<------------Interaction with Game-------------->


    //updates the points from a player
 /*   public void updatePoints(int predictedTrick, int madeTrick){
      points = points + game.calculatePointsForOnePlayer(predictedTrick, madeTrick);

    } */

    public void calculateMyPoints(){

        if(predictedTrick == madeTrick){
            points += 20 + madeTrick * 10;
        }else{
            points -= Math.abs(madeTrick - predictedTrick)*10;
        }
        resetForNewRound();
    }

    public Hand getMyCards(){
        return hand;
    }

    //Reset for new Round + Get the actual cards from Game and add it to the hand of the player
    public void giveCards(List<Card> playReadyCardsFromGame){
        resetForNewRound();
        hand.setHand(playReadyCardsFromGame);
    }
/*
    //Updates made Tricks per round
    public void updateMadeTricks(){
        if(game.calculateWhoWonTheRound().equals(playerName)){
            madeTrick++;
        }
    }*/

    //<---------------Action Player-------------------->

    //Method to play Card
    public void playCard(Card card){
        hand.removeCardFromHand(card);
       // game.addCardToCardsPlayed(this.playerName,card); -->needs to be updated
        actualPlayedCard = card;
        //server.write(bytes)

    }

    //update Set Predicted Tricks (currently per default set to 2 tricks, needs to be updated with speech input)
    public void updatePredictedTricks(String predictedTrickS){

        //checks the String speech input and turn it into an int
        //contains is need if the input is for e.g. 9 Uhr
        if (predictedTrickS.contains("0")|| predictedTrickS.equals("null")){
            predictedTrick = 0;
        }
        if(predictedTrickS.contains("1") || predictedTrickS.equals("eins")){
            predictedTrick = 1;
        }
        if(predictedTrickS.contains("2") || predictedTrickS.equals("zwei")){
            predictedTrick = 2;
        }
        if(predictedTrickS.contains("3") || predictedTrickS.equals("drei")){
            predictedTrick = 3;
        }
        if(predictedTrickS.contains("4") || predictedTrickS.equals("vier")){
            predictedTrick = 4;
        }
        if(predictedTrickS.contains("5") || predictedTrickS.equals("fünf")){
            predictedTrick = 5;
        }
        if(predictedTrickS.contains("6") || predictedTrickS.equals("sechs")){
            predictedTrick = 6;
        }
        if(predictedTrickS.contains("7") || predictedTrickS.equals("sieben")){
            predictedTrick = 7;
        }
        if(predictedTrickS.contains("8") || predictedTrickS.equals("acht")){
            predictedTrick = 8;
        }
        if(predictedTrickS.contains("9") || predictedTrickS.equals("neun")){
            predictedTrick = 9;
        }
        if(predictedTrickS.contains("10") || predictedTrickS.equals("zehn")){
            predictedTrick = 10;
        }
        if(predictedTrickS.contains("11") || predictedTrickS.equals("elf")){
            predictedTrick = 11;
        }
        if(predictedTrickS.contains("12") || predictedTrickS.equals("zwölf")){
            predictedTrick = 12;
        }
        if(predictedTrickS.contains("13") || predictedTrickS.equals("dreizehn")){
            predictedTrick = 13;
        }
        if(predictedTrickS.contains("14") || predictedTrickS.equals("vierzehn")){
            predictedTrick = 14;
        }
        if(predictedTrickS.contains("15") || predictedTrickS.equals("fünfzehn")){
            predictedTrick = 15;
        }
        if(predictedTrickS.contains("16") || predictedTrickS.equals("sechzehn")){
            predictedTrick = 16;
        }
        if(predictedTrickS.contains("17") || predictedTrickS.equals("siebzehn")){
            predictedTrick = 17;
        }
        if(predictedTrickS.contains("18") || predictedTrickS.equals("achtzehn")){
            predictedTrick = 18;
        }
        if(predictedTrickS.contains("19") || predictedTrickS.equals("neunzehn")){
            predictedTrick = 19;
        }
        if(predictedTrickS.contains("20") || predictedTrickS.equals("zwanzig")){
            predictedTrick = 20;
        }
    }

    //Only for testing Game Class!!!!
    public Card playCardForTesting() {
        Random r = new Random();
        int index = r.nextInt(hand.getHandSize());
        Card item = (Card)hand.getHand().get(index);
        //Log.d("Info", "Player: " + this.playerName + " plays: " + item.getId());
        System.out.println("Player: " + this.playerName + " plays: " + item.getId());
        return item;
    }
}
