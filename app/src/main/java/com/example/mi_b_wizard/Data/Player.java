package com.example.mi_b_wizard.Data;

import android.os.Debug;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Player{

    private String playerName;
    private int points;
    private int madeTrick; // tricks, which were made (gemachte Stiche)
    private int predictedTrick; //tricks, which were predicted (angesagte Stiche)
    private Hand hand; //actual Hand of the player
    private Game game;
    private Card actualPlayedCard;

    public Player(String playerName){
        setPlayerName(playerName);


        hand = new Hand();
        game = new Game();

    }

    //Getter-Setter

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
    public void updatePoints(int predictedTrick, int madeTrick){
        points = points + game.calculatePointsForOnePlayer(predictedTrick, madeTrick);

    }

    //Reset for new Round + Get the actual cards from Game and add it to the hand of the player
    public void giveCards(List<Card> playReadyCardsFromGame){
        resetForNewRound();
        hand.setHand(playReadyCardsFromGame);
    }

    //Updates made Tricks per round
    public void updateMadeTricks(){
        if(game.calculateWhoWonTheRound().equals(playerName)){
            madeTrick++;
        }
    }

    //<---------------Action Player-------------------->

    //Method to play Card
    public void playCard(Card card){
        hand.removeCardFromHand(card);
       // game.addCardToCardsPlayed(this.playerName,card); -->needs to be updated
        actualPlayedCard = card;
    }

    //update Set Predicted Tricks (currently per default set to 2 tricks, needs to be updated with speech input)
    public void updatePredictedTricks(){
        predictedTrick = 2;
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
