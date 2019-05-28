package com.pazaak.prototype.pazaakprototype;

import android.graphics.drawable.Drawable;

public class Card {
    private int cardId;
    private int cardType;
    private int cardValue;
    public static final int PLUS = 0;
    public static final int MINUS = 1;
    public static final int PM = 2;
    public static final int MAIN = 3;
    public Card(int type, int value)
    {
        if(type > MAIN || type < PLUS)
            this.cardType = MAIN;
        else
            this.cardType = type;

        if((this.cardType == MAIN && (value < 1 || value > 10)) || ((this.cardType == PLUS || this.cardType == MINUS || this.cardType == PM) && (value < 1 || value > 6)))
            this.cardValue = 1;
        else
            this.cardValue = value;
    }
    public int getType()
    {
        return this.cardType;
    }

    public int getValue()
    {
        int cardVal = 0;
        if (this.cardType == MINUS)
        {
            cardVal = (-1) * this.cardValue;
        }
        else
        {
            cardVal = this.cardValue;
        }
        return cardVal;
    }
    public int getImage()
    {
        switch (this.cardType){
            case PLUS: switch(this.cardValue){
                case 1: return R.drawable.plus1;
                case 2: return R.drawable.plus2;
                case 3: return R.drawable.plus3;
                case 4: return R.drawable.plus4;
                case 5: return R.drawable.plus5;
                case 6: return R.drawable.plus6;
                default: return R.drawable.ic_launcher_background;
            }
            case MINUS: switch(this.cardValue){
                case 1: return R.drawable.minus1;
                case 2: return R.drawable.minus2;
                case 3: return R.drawable.minus3;
                case 4: return R.drawable.minus4;
                case 5: return R.drawable.minus5;
                case 6: return R.drawable.minus6;
                default: return R.drawable.ic_launcher_background;
            }
            case PM: switch(this.cardValue){
                case 1: return R.drawable.pm1;
                case 2: return R.drawable.pm2;
                case 3: return R.drawable.pm3;
                case 4: return R.drawable.pm4;
                case 5: return R.drawable.pm5;
                case 6: return R.drawable.pm6;
                default: return R.drawable.ic_launcher_background;
            }
            case MAIN: switch(this.cardValue){
                case 1: return R.drawable.main1;
                case 2: return R.drawable.main2;
                case 3: return R.drawable.main3;
                case 4: return R.drawable.main4;
                case 5: return R.drawable.main5;
                case 6: return R.drawable.main6;
                case 7: return R.drawable.main7;
                case 8: return R.drawable.main8;
                case 9: return R.drawable.main9;
                case 10: return R.drawable.main10;
                default: return R.drawable.ic_launcher_background;
            }

            default: return R.drawable.ic_launcher_background;
        }
    }
    public static Card[] getSideDeck(boolean cards[])
    {
        Card deck[] = new Card[10];
        int count = 0;
        for(int i = 0; i < 18 && count < 10; i++)
        {
            if(cards[i])
            {
                deck[count] = new Card(i / 6, (i % 6) + 1);
                count++;
            }
        }
        for(int i = 0; count < 10 && i < 18; i++)
        {
            if(!cards[i])
            {
                deck[count] = new Card(i/6, (i % 6 )+1);
                count++;
            }
        }
        return deck;
    }
}

