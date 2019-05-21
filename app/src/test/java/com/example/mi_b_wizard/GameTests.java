package com.example.mi_b_wizard;

import com.example.mi_b_wizard.Data.Game;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class GameTests {

    Map<Byte,Integer> playedCards = new HashMap<>();
    ArrayList<Integer> ids = new ArrayList<Integer>();
    Game game;
    @Mock
    private GameActivity gameActivity;

    @Mock
    private MessageHandler messageHandler;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        game = new Game();
    }


    @After
    public void TearDown(){
        game = null;
        ids = null;
    }


    @Test(expected = IllegalStateException.class)
    public void testStartGameFalseNotEnoughPlayers() {
        generatePlayers(2);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.sendCards();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartGameFalseToManyPlayers() {
        generatePlayers(7);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.sendCards();
    }


    @Test
    public void testIfPlayerGetTheirCards3(){
        generatePlayers(3);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        doNothing().when(gameActivity).setTrump(any(byte.class));
        doNothing().when(messageHandler).sendEvent(any(byte.class),any(byte.class),any(byte.class),any(byte.class));
        game.sendCards();
        verify(messageHandler, times(1)).sendEvent(eq(Server.TRUMP),any(byte.class),any(byte.class),any(byte.class));
        verify(messageHandler, times(2)).sendCardsToPlayer(any(byte[].class),any(Integer.class));
        verify(gameActivity,times(1)).takeCards(any(byte[].class));
    }

    @Test
    public void testIfPlayerGetTheirCards4(){
        generatePlayers(4);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        doNothing().when(gameActivity).setTrump(any(byte.class));
        doNothing().when(messageHandler).sendEvent(any(byte.class),any(byte.class),any(byte.class),any(byte.class));
        game.sendCards();
        verify(messageHandler, times(1)).sendEvent(eq(Server.TRUMP),any(byte.class),any(byte.class),any(byte.class));
        verify(messageHandler, times(3)).sendCardsToPlayer(any(byte[].class),any(Integer.class));
        verify(gameActivity,times(1)).takeCards(any(byte[].class));
    }

    @Test
    public void testIfPlayerGetTheirCards5(){
        generatePlayers(5);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        doNothing().when(gameActivity).setTrump(any(byte.class));
        doNothing().when(messageHandler).sendEvent(any(byte.class),any(byte.class),any(byte.class),any(byte.class));
        game.sendCards();
        verify(messageHandler, times(1)).sendEvent(eq(Server.TRUMP),any(byte.class),any(byte.class),any(byte.class));
        verify(messageHandler, times(4)).sendCardsToPlayer(any(byte[].class),any(Integer.class));
        verify(gameActivity,times(1)).takeCards(any(byte[].class));
    }

    @Test
    public void testIfPlayerGetTheirCards6(){
        generatePlayers(6);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        doNothing().when(gameActivity).setTrump(any(byte.class));
        doNothing().when(messageHandler).sendEvent(eq(Server.TRUMP),any(byte.class),any(byte.class),any(byte.class));
        game.sendCards();
        verify(messageHandler, times(1)).sendEvent(eq(Server.TRUMP),any(byte.class),any(byte.class),any(byte.class));
        verify(messageHandler, times(5)).sendCardsToPlayer(any(byte[].class),any(Integer.class));
        verify(gameActivity,times(1)).takeCards(any(byte[].class));
    }


    @Test
    public void hostPlayedACardFindNext(){
        generatePlayers(6);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        doNothing().when(messageHandler).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        game.hostMadeAMove((byte)0);
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
    }

    @Test
    public void playerPlayedACardFindNext(){
        generatePlayers(6);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        doNothing().when(messageHandler).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        game.moveMade((byte)0,1);
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
    }

    @Test
    public void wizardWon(){
        generatePlayers(3);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        doNothing().when(messageHandler).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        doNothing().when(messageHandler).sendEventToTheSender(eq(Server.WINNER),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        game.hostMadeAMove((byte)14);
        game.moveMade((byte)30,1);
        game.moveMade((byte)20,2);
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.WINNER),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
    }


    @Test
    public void firstWizardWon(){
        generatePlayers(3);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        doNothing().when(messageHandler).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        game.hostMadeAMove((byte)30);
        game.moveMade((byte)45,1);
        game.moveMade((byte)60,2);
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(2));
        verify(gameActivity, atLeastOnce()).showWhoIsTheWinner();
    }

    @Test
    public void firstJesterWon(){
        generatePlayers(3);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        game.setTrumpThisRound((byte)50);
        doNothing().when(messageHandler).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        doReturn(1).when(gameActivity).getColor(eq((byte)50));
        doReturn(1).when(gameActivity).getColor(eq((byte)31));
        game.hostMadeAMove((byte)31);
        game.moveMade((byte)46,1);
        game.moveMade((byte)61,2);
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(2));
        verify(gameActivity, atLeastOnce()).showWhoIsTheWinner();
    }
    @Test
    public void firstHighCardWon(){
        generatePlayers(3);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        game.setTrumpThisRound((byte)50);
        doNothing().when(messageHandler).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        doReturn(1).when(gameActivity).getColor(eq((byte)50));
        doReturn(0).when(gameActivity).getColor(eq((byte)20));
        game.hostMadeAMove((byte)20);
        game.moveMade((byte)25,1);
        game.moveMade((byte)26,2);
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(2));
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.WINNER),eq((byte)0),eq((byte)0),eq((byte)0),eq(2));
    }

    @Test
    public void nextRound(){
        generatePlayers(3);
        game.setIdsTest(ids);
        game.setMessageHandler(messageHandler);
        game.setGameActivity(gameActivity);
        game.sendCards();
        doNothing().when(messageHandler).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        game.hostMadeAMove((byte)44);
        game.moveMade((byte)47,1);
        game.moveMade((byte)68,2);
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(2));
        verify(gameActivity, atLeastOnce()).showWhoIsTheWinner();
        game.hostMadeAMove((byte)40);
        verify(messageHandler, atLeastOnce()).sendEventToTheSender(eq(Server.YOUR_TURN),eq((byte)0),eq((byte)0),eq((byte)0),eq(1));

    }

    private void generatePlayers(int numOfPlayers) {
        for (int i = 1; i <numOfPlayers ; i++) {
            ids.add(i);
        }
    }
}
