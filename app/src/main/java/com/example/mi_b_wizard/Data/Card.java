package com.example.mi_b_wizard.Data;

public class Card {

    private Rank rank;
    private Colour colour;
    private byte id;

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
        if(rank <0|| rank >14){
            throw new RuntimeException("Rank must be between 0 and 14");
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
        return this.rank == Rank.values()[14];
    }
    public boolean isJester(){
        return this.rank ==Rank.values()[0];
    }

    //Sets the ID of the cards in Byte to send the cards clients <->host
    //ID Example rank 0, colour 1 --> 1 ; rank 1, colour 0 -->10
    public void setId(int rank, int colour){
        String idString = Integer.toString(rank)+(colour);
        int idInt = Integer.valueOf(idString);
        int intID = (rank+1)+((colour+1)*15);
        byte bID = (byte)intID;
        this.id = bID;
    }



    public byte getId(){
        return id;
    }

    public boolean equalToOtherCard(Card otherCard){
        if (this.getColour() == otherCard.getColour() && this.getRank() == otherCard.getRank()){
            return true;
        }else
            return false;
    }

    public Card returnThisCard(){
        return this;
    }

    public void showCard(){
        //needs to be implemented
        System.out.println(toString()+", ");
    }
    //This method returns e.g. "TWO in BLUE"
    @Override
    public String toString(){
        //return String.format("%s in %s", getRank(),getColour());
        return getColour() + "_" + getRank();
    }
}
