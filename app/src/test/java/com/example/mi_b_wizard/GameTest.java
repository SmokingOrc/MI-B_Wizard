package com.example.mi_b_wizard;

import com.example.mi_b_wizard.Data.Game;

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
    }

    @After
    public void tearDown() {
        testGame = null;
    }

}
