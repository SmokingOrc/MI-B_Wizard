package com.example.mi_b_wizard.Data;

public enum Colour {
    BLAU(0),
    GRÜN(1),
    GELB(2),
    ROT(3);

    //variable to get the values
    private int value;

    //Enum constructor
    Colour(int value){
        this.value = value;
    }
}
