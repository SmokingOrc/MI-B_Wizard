package com.example.mi_b_wizard;

import android.content.SyncStatusObserver;

import com.example.mi_b_wizard.Data.Game;
import com.example.mi_b_wizard.Data.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameTest {
    private Game testGame;

    @Before
    public void init() {
        testGame = new Game();
    }



    @Test
    public void testAddPlayer() {
        testGame.addPlayerToPlayers(new Player("Hanno"));
        assertEquals(1, testGame.get_players().size());
        testGame.addPlayerToPlayers(new Player("Felix"));
        assertEquals(2, testGame.get_players().size());
    }

    @Test
    public void testGiveCardsInFirstRound() {
        Player p = new Player("Hanno");
        Player p1 = new Player("Hugo");

        testGame.addPlayerToPlayers(p);
        testGame.addPlayerToPlayers(p1);
        testGame.setCurrentRound(1);
        testGame.handOutCardsAndSetTrump();
        assertEquals(1, p1.getHand().getHandSize());
        assertEquals(1, p.getHand().getHandSize());
    }

    @Test
    public void testGiveCardsInThirdRound() {
        Player p = new Player("Hanno");
        Player p1 = new Player("Hugo");

        testGame.addPlayerToPlayers(p);
        testGame.addPlayerToPlayers(p1);
        testGame.setCurrentRound(3);
        testGame.handOutCardsAndSetTrump();
        assertEquals(3, p1.getHand().getHandSize());
        assertEquals(3, p.getHand().getHandSize());
    }

    @Test
    public void testCalculateWhoWonTheRoundWithTwoPlayers() {
        Player p = new Player("Anna");
        Player p1 = new Player("Bella");

        testGame.addPlayerToPlayers(p);
        testGame.addPlayerToPlayers(p1);
        testGame.setCurrentRound(3);
        testGame.handOutCardsAndSetTrump();
        //first card check1!!!!
        testGame.addCardToCardsPlayed(p, p.playCardForTesting());
        testGame.addCardToCardsPlayed(p1, p1.playCardForTesting());

        System.out.println("Trump is: " + testGame.getTrump());
        System.out.println("Player who won: " + testGame.calculateWhoWonTheRound().getPlayerName());

    }

    @Test
    public void testCalculateWhoWonTheRoundWithThreePlayers() {
        Player p = new Player("Hanno");
        Player p1 = new Player("Franz");
        Player p2 = new Player("Kain");

        testGame.addPlayerToPlayers(p);
        testGame.addPlayerToPlayers(p1);
        testGame.addPlayerToPlayers(p2);
        testGame.setCurrentRound(1);
        testGame.handOutCardsAndSetTrump();
        //first card check1!!!!
        testGame.addCardToCardsPlayed(p, p.playCardForTesting());
        testGame.addCardToCardsPlayed(p1, p1.playCardForTesting());
        testGame.addCardToCardsPlayed(p2, p2.playCardForTesting());

        System.out.println("Trump is: " + testGame.getTrump());
        System.out.println("Player who won: " + testGame.calculateWhoWonTheRound().getPlayerName());
        System.out.println("Tricks made player " + p.getPlayerName() + ": " + p.getMadeTrick());
        System.out.println("Tricks made player " + p1.getPlayerName() + ": " + p1.getMadeTrick());
        System.out.println("Tricks made player " + p2.getPlayerName() + ": " + p2.getMadeTrick());

    }

    @Test
    public void testCalculateWhoWonTheRoundWithFourPlayers() {
        Player p = new Player("Hanno");
        Player p1 = new Player("Franz");
        Player p2 = new Player("Kain");
        Player p3 = new Player("Abel");

        testGame.addPlayerToPlayers(p);
        testGame.addPlayerToPlayers(p1);
        testGame.addPlayerToPlayers(p2);
        testGame.addPlayerToPlayers(p3);
        testGame.setCurrentRound(5);
        testGame.handOutCardsAndSetTrump();
        //first card check1!!!!
        testGame.addCardToCardsPlayed(p, p.playCardForTesting());
        testGame.addCardToCardsPlayed(p1, p1.playCardForTesting());
        testGame.addCardToCardsPlayed(p2, p2.playCardForTesting());
        testGame.addCardToCardsPlayed(p3, p3.playCardForTesting());
        System.out.println("Trump is: " + testGame.getTrump());
        System.out.println("Player who won: " + testGame.calculateWhoWonTheRound().getPlayerName());
    }

    @Test
    public void testCalculatePoints() {
        Player p = new Player("Hanno");
        Player p1 = new Player("Franz");
        Player p2 = new Player("Kain");
        Player p3 = new Player("Abel");

        p.setMadeTrick(3);
        p.setPredictedTrick((byte)3);
        p1.setMadeTrick(2);
        p1.setPredictedTrick((byte)0);
        p2.setMadeTrick(1);
        p2.setPredictedTrick((byte)1);
        p3.setPredictedTrick((byte)0);
        p3.setMadeTrick(0);

        testGame.addPlayerToPlayers(p);
        testGame.addPlayerToPlayers(p1);
        testGame.addPlayerToPlayers(p2);
        testGame.addPlayerToPlayers(p3);

        testGame.calculatePointsForAllPlayers();


    }

    /*
    @Test
    public void testAddPlayer() {
        testGame.addPlayer("player1");
        assertEquals(1, testGame.getPlayers().size());
        testGame.addPlayer("player2");
        assertEquals(2, testGame.getPlayers().size());
    }

    @Test
    public void testRemovePlayer() {
        testGame.addPlayer("player1");
        testGame.addPlayer("player2");
        assertEquals("player1", testGame.removePlayer("player1"));
        assertEquals(null, testGame.removePlayer("player3"));
    }

    @Test
    public void testCalculateWhoWonTheRoundWithFourPlayers() {
        testGame.addCardToCardsPlayed("player1", 12);
        testGame.addCardToCardsPlayed("player2", 7);
        testGame.addCardToCardsPlayed("player3", 8);
        testGame.addCardToCardsPlayed("player4", 3);
        assertEquals("player1", testGame.calculateWhoWonTheRound());
    }

    @Test
    public void testCalculateWhoWonTheRoundWithThreePlayers() {
        testGame.addCardToCardsPlayed("player1", 12);
        testGame.addCardToCardsPlayed("player2", 7);
        testGame.addCardToCardsPlayed("player3", 13);
        assertEquals("player3", testGame.calculateWhoWonTheRound());
    }

    @Test
    public void testCalculateWhoWonTheRoundWithTwoPlayers() {
        testGame.addCardToCardsPlayed("player1", 12);
        testGame.addCardToCardsPlayed("player2", 13);
        assertEquals("player2", testGame.calculateWhoWonTheRound());
    }

    @Test
    public void testCalculatePointsForOnePlayer() {
        assertEquals(40, testGame.calculatePointsForOnePlayer(2,2));
        assertEquals(70, testGame.calculatePointsForOnePlayer(5, 5));
        assertEquals(-20, testGame.calculatePointsForOnePlayer(2,0));
        assertEquals(-10, testGame.calculatePointsForOnePlayer(2,1));
        assertEquals(-30, testGame.calculatePointsForOnePlayer(3,6));
        assertEquals(20, testGame.calculatePointsForOnePlayer(0,0));
    }*/

    @After
    public void tearDown() {
        testGame = null;
    }

}
