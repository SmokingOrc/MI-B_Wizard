package com.example.mi_b_wizard.Data;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Deck {

    Map<String, Card> card = new HashMap<>();


    private void resetDeck() {                                   //resetDeck() initializes and resets all the Cards in the HashMap


        //   <---- Create new Cards ---->

        //Blue
        Card blue1 = new Card(1, 0);
        Card blue2 = new Card(2, 0);
        Card blue3 = new Card(3, 0);
        Card blue4 = new Card(4, 0);
        Card blue5 = new Card(5, 0);
        Card blue6 = new Card(6, 0);
        Card blue7 = new Card(7, 0);
        Card blue8 = new Card(8, 0);
        Card narr1 = new Card(0, 0);
        Card zauberer1 = new Card(9, 0);


        //Green
        Card green1 = new Card(1, 1);
        Card green2 = new Card(2, 1);
        Card green3 = new Card(3, 1);
        Card green4 = new Card(4, 1);
        Card green5 = new Card(5, 1);
        Card green6 = new Card(6, 1);
        Card green7 = new Card(7, 1);
        Card green8 = new Card(8, 1);
        Card narr2 = new Card(0, 1);
        Card zauberer2 = new Card(9, 1);


        //Yellow
        Card yellow1 = new Card(1, 2);
        Card yellow2 = new Card(2, 2);
        Card yellow3 = new Card(3, 2);
        Card yellow4 = new Card(4, 2);
        Card yellow5 = new Card(5, 2);
        Card yellow6 = new Card(6, 2);
        Card yellow7 = new Card(7, 2);
        Card yellow8 = new Card(8, 2);
        Card narr3 = new Card(0, 2);
        Card zauberer3 = new Card(9, 2);


        //Red
        Card red1 = new Card(1, 3);
        Card red2 = new Card(2, 3);
        Card red3 = new Card(3, 3);
        Card red4 = new Card(4, 3);
        Card red5 = new Card(5, 3);
        Card red6 = new Card(6, 3);
        Card red7 = new Card(7, 3);
        Card red8 = new Card(8, 3);
        Card narr4 = new Card(0, 3);
        Card zauberer4 = new Card(9, 3);


        //    <----- put Cards in HashMap (with ID) ---->

        //Blue
        card.put(blue1.getId(), blue1);
        card.put(blue2.getId(), blue2);
        card.put(blue3.getId(), blue3);
        card.put(blue4.getId(), blue4);
        card.put(blue5.getId(), blue5);
        card.put(blue6.getId(), blue6);
        card.put(blue7.getId(), blue7);
        card.put(blue8.getId(), blue8);
        card.put(narr1.getId(), narr1);
        card.put(zauberer1.getId(), zauberer1);

        //Green
        card.put(green1.getId(), green1);
        card.put(green2.getId(), green2);
        card.put(green3.getId(), green3);
        card.put(green4.getId(), green4);
        card.put(green5.getId(), green5);
        card.put(green6.getId(), green6);
        card.put(green7.getId(), green7);
        card.put(green8.getId(), green8);
        card.put(narr2.getId(), narr2);
        card.put(zauberer2.getId(), zauberer2);

        //Yellow
        card.put(yellow1.getId(), yellow1);
        card.put(yellow2.getId(), yellow2);
        card.put(yellow3.getId(), yellow3);
        card.put(yellow4.getId(), yellow4);
        card.put(yellow5.getId(), yellow5);
        card.put(yellow6.getId(), yellow6);
        card.put(yellow7.getId(), yellow7);
        card.put(yellow8.getId(), yellow8);
        card.put(narr3.getId(), narr3);
        card.put(zauberer3.getId(), zauberer3);

        //Red
        card.put(red1.getId(), red1);
        card.put(red2.getId(), red2);
        card.put(red3.getId(), red3);
        card.put(red4.getId(), red4);
        card.put(red5.getId(), red5);
        card.put(red6.getId(), red6);
        card.put(red7.getId(), red7);
        card.put(red8.getId(), red8);
        card.put(narr4.getId(), narr4);
        card.put(zauberer4.getId(), zauberer4);


    }


    public List<String> getCards(int currentRound) {                //getCards method takes the input currentRound, to generate a List<String> with the number of random cards equal to current Round




        List<String> HandCards;
        HashSet<String> set = new HashSet();                        //HashSet doesn't allow duplicates values


        while (set.size() < currentRound) {
            Object[] rand = card.keySet().toArray();                //get a random entry from the HashMap
            Object key = rand[new Random().nextInt(rand.length)];   //get a random key for a card
            String c = card.get(key).toString();                    //get ID from the Card with the generated key
            set.add(c);                                             //add the ID of the Card with the random generated key to the HashSet set
            card.remove(key);                                       //remove this Card from the HashMap card, so that the getCards Method gives different Cards every time it is called, until the Method resetDeck() is colled
        }

        HandCards = new ArrayList<String>(set);                     //save HashSet set into a new ArrayList


        return HandCards;
    }


}





    //Main Method and getCardsTest Method for testing functionality (with sout for better understanding)


    /*public static void main(String[] args) {
        Deck test = new Deck();
        test.resetDeck();


        System.out.println(test.getCardsTest(5));


        test.resetDeck();


        System.out.println(test.getCardsTest(5));
        System.out.println(test.getCardsTest(4));

    }

    public List<String> getCardsTest(int currentRound) {


        List<String> HandCards;
        HashSet<String> set = new HashSet();


        while (set.size() < currentRound) {
            Object[] rand = card.keySet().toArray();                //Get a random entry from the HashMap
            Object key = rand[new Random().nextInt(rand.length)];
            String c = card.get(key).toString();
            set.add(c);
            card.remove(key);
            System.out.println(card.size());

        }

        HandCards = new ArrayList<String>(set);


        return HandCards;
    }

}*/


