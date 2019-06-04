package com.example.mi_b_wizard;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Data.Deck;
import com.example.mi_b_wizard.Data.Hand;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


public class DeckTest {

    Deck deck;

    @Before
    public void setUp(){
        deck = new Deck();
        deck.resetDeck();
    }

    @After
    public void tearDown(){
        deck.resetDeck();
    }


    //Test if Deck is full
    @Test
    public void fullDeck(){
        assertEquals(60,deck.availableCards());
    }


    //Test if Deck is empty
    @Test
    public void deckIsEmpty(){
        deck.getCards(60);
        assertEquals(0, deck.availableCards());
    }


    //Test if ArrayList has 5 entries
    @Test
    public void getFiveCards(){
        assertEquals(5, deck.getCards(5).size());
    }



    //Test if 5 cards get removed from Deck
    @Test
    public void removeFiveCardsFromDeck(){
        System.out.println("Size of Deck: " + deck.deckSize());
        System.out.println("Cards: " + deck.getCards(5));
        System.out.println("Size of Deck: " + deck.availableCards());
        assertEquals(deck.deckSize(),55);
    }



    //Test if resetDeck() method is working
    @Test
    public void removeFiveCardsAndResetDeck(){
        System.out.println("Size of Deck: " + deck.deckSize());
        System.out.println("Cards: " + deck.getCards(5));
        System.out.println("Size of Deck: " + deck.availableCards());
        assertEquals(deck.deckSize(),55);
        System.out.println("----- Reset Deck -----");
        deck.resetDeck();
        System.out.println("Size of Deck: " + deck.deckSize());
        assertEquals(deck.deckSize(),60);
    }


    //Test if Current Round number is too large

    @Test
    public void currentRoundNumberTooLarge() {
        try{
        deck.getCards(41);
    }catch (IllegalArgumentException ile){
            ile.printStackTrace();}
    }

    @Test
    public void getMyHandFromByteArrayTest(){
        byte[] ba = {0,50};
        Card c = new Card(4,2);
        Hand myhand = new Hand();
        myhand.addCardToHand(c);
        Hand hand = new Hand();
        hand = deck.getMyHand(ba);
        assertEquals(myhand.getHand().toString(),myhand.getHand().toString());


    }

    @Test
    public void getByteCardToStringTest(){
        byte[] ba = {0,50};
        String cardString = deck.getMyCardsToString(ba);
        assertEquals("YELLOW FOUR, ", cardString );


    }

    @Test
    public void getByteCardTest(){
        byte card = 50;
        Card c = deck.getThisCard(card);
       assertEquals("FOUR in YELLOW",c.toString());


    }

    @Test
    public void getMyCardToStringByte(){
        byte bytecard = 32;
        assertEquals("GREEN ONE, ", deck.getMyCardToString(bytecard));
    }

}
