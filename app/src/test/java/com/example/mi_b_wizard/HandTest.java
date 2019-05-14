package com.example.mi_b_wizard;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Data.Hand;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HandTest {

    private Hand hand;
    private List<Card> hashMap;
    private List<Card> hashMap2;
    private Card card1;
    private Card card2;


    @Before
    public void beforeEachTest(){
        hashMap = new ArrayList<Card>();
        card1 = new Card(0,1);
        card2 = new Card(1,2);
        Card card3 = new Card(2,3);
        Card card4 = new Card(2,2);
        hashMap.add(card1);
        hashMap.add(card2);
        hashMap.add(card3);
        hashMap.add(card4);

        hashMap2 = new ArrayList<Card>();
        hashMap2.add(card1);

        hand = new Hand();
    }
    @After
    public void afterEachtTest(){
        hashMap = null;
        hand = null;
    }

    @Test
    public void testHandSize0(){
        assertEquals(0, hand.getHandSize());
    }
    @Test
    public void testAddListOfCards(){
        hand.setHand(hashMap);
        assertEquals(hashMap,hand.getHand());
    }
    @Test
    public void testSizeOfHand4(){
        hand.setHand(hashMap);
        assertEquals(4, hand.getHandSize());
    }
    @Test
    public void testClearHandListCard(){
        hand.setHand(hashMap);
        assertEquals(4,hand.getHandSize());
        hand.clearHand();
        assertEquals(0,hand.getHandSize());
    }
    @Test
    public void testClearHandTwoCards(){
        hand.addCardToHand(card1);
        hand.addCardToHand(card2);
        assertEquals(2, hand.getHandSize());
        hand.clearHand();
        assertEquals(0,hand.getHandSize());
    }
    @Test
    public void testAddCardToHand(){
        hand.addCardToHand(card1);
        assertEquals(1, hand.getHandSize());
        hand.addCardToHand(card2);
        assertEquals(2, hand.getHandSize());
    }
    @Test
    public void testRemoveCardFromHand(){
        hand.setHand(hashMap);
        assertEquals(4,hand.getHandSize());
        hand.removeCardFromHand(card1);
        assertEquals(3, hand.getHandSize());
    }
    @Test
    public void testSetHand(){
        hand.setHand(hashMap);

        assertEquals(hashMap,hand.getHand());
    }
}
