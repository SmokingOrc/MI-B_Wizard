package com.example.mi_b_wizard;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Data.Hand;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class HandTest {

    private Hand hand;
    private HashMap<String, Card> hashMap;
    private HashMap<String, Card> hashMap2;
    private Card card1;
    private Card card2;


    @Before
    public void beforeEachTest(){
        hashMap = new HashMap();
        card1 = new Card(0,1);
        card2 = new Card(1,2);
        Card card3 = new Card(2,3);
        Card card4 = new Card(2,2);
        hashMap.put(card1.getId(),card1);
        hashMap.put(card2.getId(),card2);
        hashMap.put(card3.getId(),card3);
        hashMap.put(card4.getId(),card4);

        hashMap2 = new HashMap();
        hashMap2.put(card1.getId(),card1);

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
        hand.addListOfCardsToHand(hashMap);
        assertEquals(hashMap,hand.getHand());
    }
    @Test
    public void testSizeOfHand4(){
        hand.addListOfCardsToHand(hashMap);
        assertEquals(4, hand.getHandSize());
    }
    @Test
    public void testClearHandListCard(){
        hand.addListOfCardsToHand(hashMap);
        assertEquals(4,hand.getHandSize());
        hand.clearHand();
        assertEquals(0,hand.getHandSize());
    }
    @Test
    public void testClearHandTwoCards(){
        hand.addCardToHand(card1.getId(),card1);
        hand.addCardToHand(card2.getId(),card2);
        assertEquals(2, hand.getHandSize());
        hand.clearHand();
        assertEquals(0,hand.getHandSize());
    }
    @Test
    public void testAddCardToHand(){
        hand.addCardToHand(card1.getId(),card1);
        assertEquals(1, hand.getHandSize());
        hand.addCardToHand(card2.getId(), card2);
        assertEquals(2, hand.getHandSize());
    }
    @Test
    public void testRemoveCardFromHand(){
        hand.addListOfCardsToHand(hashMap);
        assertEquals(4,hand.getHandSize());
        hand.removeCardFromHand(card1);
        assertEquals(3, hand.getHandSize());
    }
    @Test
    public void testSetHand(){
        hand.addListOfCardsToHand(hashMap);
        hand.setHand();
        assertEquals(hashMap,hand.getHand());
    }
}
