package com.example.mi_b_wizard.Data;

public enum Colour {
    BLUE(0),
    GREEN(1),
    YELLOW(2),
    RED(3);

    //variable to get the values
    private int value;

    //Enum constructor
    Colour(int value){
        this.value = value;
    }
}
