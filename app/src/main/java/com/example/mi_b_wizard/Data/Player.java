package com.example.mi_b_wizard.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        this.playerName = playerName;
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
        points = game.calculatePointsForOnePlayer(predictedTrick, madeTrick);
    }

    //Reset for new Round + Get the actual cards from Game and add it to the hand of the player
    public void giveCards(HashMap<String,Card> playReadyCardsFromGame){
        resetForNewRound();
        hand.addListOfCardsToHand(playReadyCardsFromGame);
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
       // game.addCardToCardsPlayed(this.playerName,card); -->needs to be updated to give over card as Card
        actualPlayedCard = card;
    }

    //update Set Predicted Tricks (currently per default set to 2 tricks, needs to be updated with speech input)
    public void updatePredictedTricks(){
        predictedTrick = 2;
    }

    //Main Methode to test the functionality
  /*public static void main(String[] args) {

        Card c1 = new Card(0,1); Card c2 = new Card(1,2); Card c3 = new Card(5,1); Card c4 = new Card(8,2);

        HashMap<String, Card> playReadyCardsFromGame = new HashMap<>();
        playReadyCardsFromGame.put(c1.getId(),c1); playReadyCardsFromGame.put(c2.getId(),c2); playReadyCardsFromGame.put(c3.getId(),c3); playReadyCardsFromGame.put(c4.getId(),c4);

        Player player = new Player ("Julia");
        player.giveCards(playReadyCardsFromGame);
        player.showHand();
        int madeTricks = 1; int predictedTricks = 2;
        player.updatePoints(predictedTricks, madeTricks);
        player.showActualPoints();
        player.playCard(c2);
        System.out.println(player.actualPlayedCard);
        player.showHand();
    }*/
}
