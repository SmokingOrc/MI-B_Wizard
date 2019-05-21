package com.example.mi_b_wizard;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Data.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PlayerTest {
    Player player;
    Player player2;
    @Before
    public void before(){
        player =  new Player("Tom");
        player2 = new Player("Lisa");
    }
    @After
    public void after(){
        player = null;
    }

    @Test
    public void testGetName(){
        assertEquals("Tom",player.getPlayerName());
        assertEquals("Lisa", player2.getPlayerName());
    }
    @Test
    public void testSetName(){
        assertEquals("Tom", player.getPlayerName());
        player.setPlayerName("Max");
        assertEquals("Max", player.getPlayerName());
    }
    @Test
    public void testSetMadeTrick(){
        player.setMadeTrick(3);
        assertEquals(3, player.getMadeTrick());
        player.setMadeTrick(5);
        assertEquals(5, player.getMadeTrick());
    }
    @Test
    public void testGetPoints(){
        assertEquals(0, player.getPoints());
    }
    @Test
    public void testSetPoints(){
        player.setPoints(5);
        assertEquals(5, player.getPoints());
    }
    @Test
    public void testSetActualPlayedCard(){
        assertNull(player.getActualPlayedCard());
        Card card = new Card(0,0);
        player.setActualPlayedCard(card);
        assertEquals(card, player.getActualPlayedCard());
    }
    @Test
    public void testResetForNewRound(){
        player.setActualPlayedCard(new Card(1,2));
        player.setPredictedTrick((byte)5);
        player.setMadeTrick(2);
        player.resetForNewRound();

        assertNull(player.getActualPlayedCard());
        assertEquals(0, player.getPredictedTrick());
        assertEquals(0, player.getMadeTrick());
    }
    /*
    @Test
    public void testUpdatePoints(){
        player.updatePoints(5,2);
        assertEquals(-30, player.getPoints());
        player.updatePoints(3,3);
        assertEquals(20,player.getPoints());
    } */
    @Test
    public void testPlayCard(){
        Card card = new Card(1,2);
        player.playCard(card);
        assertEquals(card, player.getActualPlayedCard());
    }
    @Test
    public void testCheckPredictedtricks1(){
        player.checkPredictedTricks("eins");
        assertEquals(1, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("1 Uhr");
        assertEquals(1, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("hundert und 1");
        assertEquals(1, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks2(){
        player.checkPredictedTricks("zwei");
        assertEquals(2, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("2 Uhr");
        assertEquals(2, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks3(){
        player.checkPredictedTricks("drei");
        assertEquals(3, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("3 Uhr");
        assertEquals(3, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("3 Uhr hallo");
        assertEquals(3, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks4(){
        player.checkPredictedTricks("vier");
        assertEquals(4, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("4 Uhr");
        assertEquals(4, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("Hallo wie gehts 4 ");
        assertEquals(4, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks5(){
        player.checkPredictedTricks("fünf");
        assertEquals(5, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("5 Uhr");
        assertEquals(5, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("Ist es 5");
        assertEquals(5, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks6(){
        player.checkPredictedTricks("sechs");
        assertEquals(6, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("6 Uhr");
        assertEquals(6, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("have you already predicted 6 ");
        assertEquals(6, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks7(){
        player.checkPredictedTricks("sieben");
        assertEquals(7, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("7 Uhr");
        assertEquals(7, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("7 and nine ");
        assertEquals(7, player.getCheckedPredictedTricks());
    }

    @Test
    public void testCheckPredictedtricks8(){
        player.checkPredictedTricks("acht");
        assertEquals(8, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("8 Uhr");
        assertEquals(8, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("8 and hallo ");
        assertEquals(8, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks9(){
        player.checkPredictedTricks("neun");
        assertEquals(9, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("9 Uhr");
        assertEquals(9, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("it is 9 and nine ");
        assertEquals(9, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks10(){
        player.checkPredictedTricks("zehn");
        assertEquals(10, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("10 Uhr");
        assertEquals(10, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("10 hallo how and nine ");
        assertEquals(10, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks11(){
        player.checkPredictedTricks("elf");
        assertEquals(11, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("11 Uhr");
        assertEquals(11, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("11 hallo how and nine ");
        assertEquals(11, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks12(){
        player.checkPredictedTricks("zwölf");
        assertEquals(12, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("12 Uhr");
        assertEquals(12, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("12 hallo how and nine ");
        assertEquals(12, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks13(){
        player.checkPredictedTricks("dreizehn");
        assertEquals(13, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("13 Uhr");
        assertEquals(13, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("13 its time to play ");
        assertEquals(13, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks14(){
        player.checkPredictedTricks("vierzehn");
        assertEquals(14, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("14 Uhr");
        assertEquals(14, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("14 hallo");
        assertEquals(14, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks15(){
        player.checkPredictedTricks("fünfzehn");
        assertEquals(15, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("15 Uhr");
        assertEquals(15, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("15 and nine ");
        assertEquals(15, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks16(){
        player.checkPredictedTricks("sechzehn");
        assertEquals(16, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("16 Uhr");
        assertEquals(16, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("16 how and nine ");
        assertEquals(16, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks17(){
        player.checkPredictedTricks("siebzehn");
        assertEquals(17, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("17 Uhr");
        assertEquals(17, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("17 and nine ");
        assertEquals(17, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks18(){
        player.checkPredictedTricks("achtzehn");
        assertEquals(18, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("18 Uhr");
        assertEquals(18, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("hi i will predicht 18 ");
        assertEquals(18, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks19(){
        player.checkPredictedTricks("neunzehn");
        assertEquals(19, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("19 Uhr");
        assertEquals(19, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("predict tricks 19");
        assertEquals(19, player.getCheckedPredictedTricks());
    }
    @Test
    public void testCheckPredictedtricks20(){
        player.checkPredictedTricks("zwanzig");
        assertEquals(20, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("20 Uhr");
        assertEquals(20, player.getCheckedPredictedTricks());
        player.checkPredictedTricks("my tricks are 20");
        assertEquals(20, player.getCheckedPredictedTricks());
    }
    @Test
    public void updatedPredictedTricks(){
        player.updatePredictedTricks((byte)5);
        assertEquals((byte)5, player.getPredictedTrick());
    }

}
