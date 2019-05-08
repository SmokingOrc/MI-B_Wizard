package com.example.mi_b_wizard;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Data.Colour;
import com.example.mi_b_wizard.Data.Rank;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CardTest {
    private Card card;
    private Card card1;
    private Card card2;
    private Card card3;

    @Before
    public void before(){
        card = new Card(0,1);
        card1 = new Card(14,2);
        card2 = new Card(0,1);
        card3 = new Card(5,3);
    }
    @After
    public void after(){
        card = null;
        card1 = null;
        card2 = null;
    }
    @Test
    public void testGetRank(){
        assertEquals(Rank.JESTER,card.getRank());
        assertEquals(Rank.MAGICIAN, card1.getRank());
        assertEquals(Rank.FIVE, card3.getRank());

    }
    @Test
    public void testSetRankFailed(){
        try{
            card.setRank(15);
            fail();
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"Rank must be between 0 and 14");

        }
    }
    @Test
    public void testSetRankFailed2(){
        try{
            card.setRank(-2);
            fail();
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"Rank must be between 0 and 14");

        }
    }
    @Test
    public void testSetRankValide(){
        card.setRank(2);
        assertEquals(Rank.TWO,card.getRank());
    }
    @Test
    public void testGetColour(){
        assertEquals(Colour.GREEN, card.getColour());
        assertEquals(Colour.YELLOW, card1.getColour());
        assertEquals(Colour.RED, card3.getColour());
    }
    @Test
    public void testSetColourValide(){
        card.setColour(2);
        assertEquals(Colour.YELLOW, card.getColour());
    }
    @Test
    public void testSetClourFailed(){
        try{
            card.setColour(5);
            fail();
        }catch(RuntimeException e){
            assertEquals(e.getMessage(), "Colour must be between 0 and 3");
        }
    }
    @Test
    public void testisMagicianTrue(){
        assertTrue(card1.isMagician());
    }
    @Test
    public void testisMagicionFalse(){
        assertFalse(card.isMagician());
        assertFalse(card3.isMagician());
        assertFalse(card2.isMagician());
    }
    @Test
    public void testisNarrTrue(){
        assertTrue(card.isJester());
        assertTrue(card2.isJester());
    }
    @Test
    public void testisNarrFalse(){
        assertFalse(card1.isJester());
        assertFalse(card3.isJester());
    }
    @Test
    public void testGetId(){
        assertEquals(1,card.getId());
    }
    @Test
    public void testSetIdValide1(){
        card.setId(2,1);
        assertEquals(21,card.getId());
    }
    @Test
    public void testSetIdValide2(){
        card.setId(11,1);
        assertEquals(111,card.getId());
    }
    @Test
    public void testEqualToOtherCardTrue(){
        assertTrue(card.equalToOtherCard(card2));
    }
    @Test
    public void testEqualToOtherCardFalse(){
        assertFalse(card.equalToOtherCard(card1));
    }
    @Test
    public void testToString(){
        assertEquals("JESTER in GREEN",card.toString());
    }
}

