package com.example.mi_b_wizard.Data;

public class Card {

    private Rank rank;
    private Colour colour;
    private String id;

    //Constructor
    public Card(int rank, int colour){
        setRank(rank);
        setColour(colour);
        setId(rank, colour);
    }

    public Rank getRank(){
        return rank;
    }

    public void setRank(int rank){
        if(rank <0|| rank >9){
            throw new RuntimeException("Rank must be between 0 and 9");
        }else
        this.rank = Rank.values()[rank];
    }

    public Colour getColour(){
        return colour;
    }

    public void setColour(int colour){
        if(colour <0|| colour >3){
            throw new RuntimeException("Colour must be between 0 and 3");
        }else
        this.colour = Colour.values()[colour];
    }

    public boolean isMagician(){
        return this.rank == Rank.values()[9];
    }
    public boolean isNarr(){
        return this.rank ==Rank.values()[0];
    }
    //SetId example Rank= ZAUBERER, Colour = CRÜN -->ZAUBERER_GRÜN
    public void setId(int rank, int colour) {
        this.id = Rank.values()[rank].toString()+"_"+Colour.values()[colour].toString();
    }
    public String getId(){
        return id;
    }

    public boolean equalToOtherCard(Card otherCard){
        if (this.getColour() == otherCard.getColour() && this.getRank() == otherCard.getRank()){
            return true;
        }else
            return false;
    }

    public void showCard(){
        //needs to be implemented
        System.out.println(toString()+", ");
    }
    //This method returns e.g. "ZWEI in BLAU"
    @Override
    public String toString(){
        return String.format("%s in %s", getRank(),getColour());
    }
}
