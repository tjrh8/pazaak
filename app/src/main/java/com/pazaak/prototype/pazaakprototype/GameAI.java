package com.pazaak.prototype.pazaakprototype;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class GameAI
{
    private int difficulty;
    public static final int EASY = 2;
    public static final int MEDIUM = 1;
    public static final int HARD = 0;
    public static boolean PLUS = false;
    public static boolean MINUS = true;
    private List<Card> aiDeckAndHand;
    private boolean plusOrMinus;
    public GameAI(int aiDifficulty)
    {
        this.aiDeckAndHand = new ArrayList<Card>();
        this.difficulty = aiDifficulty;
        setAiDeck();
        trimDeck();
        this.plusOrMinus = MINUS;
    }
    public boolean shouldStand(int aiScore, List<Card> mainDeck)
    {
        ///mainDeck.remove(0);
        List<Card> mainDeckCopy = new ArrayList<Card>(mainDeck);
        float average = getDeckAverage(mainDeckCopy);
        //if(average + aiScore <= 20)
            //return true;

        return (average + aiScore - difficulty) > 20;
    }
    private float getDeckAverage(List<Card> deck)
    {
        int totalCards = deck.size();
        float cardAverage = 0;
        while(!deck.isEmpty())
        {
            cardAverage += deck.remove(0).getValue();
        }
        cardAverage /= totalCards;
        return cardAverage;
    }
    private void setAiDeck()
    {
        switch(difficulty)
        {
            case EASY: setEasyDeck();break;
            //case MEDIUM: setMediumDeck();break;
            case HARD: setHardDeck();break;
            default: setMediumDeck();break;
        }
    }
    private void setEasyDeck()
    {
        this.aiDeckAndHand.add(new Card(Card.PLUS, 1));
        this.aiDeckAndHand.add(new Card(Card.PLUS, 2));
        this.aiDeckAndHand.add(new Card(Card.PLUS, 4));
        this.aiDeckAndHand.add(new Card(Card.PLUS, 5));
        this.aiDeckAndHand.add(new Card(Card.PLUS, 6));
        this.aiDeckAndHand.add(new Card(Card.PLUS, 3));
        this.aiDeckAndHand.add(new Card(Card.MINUS, 4));
        this.aiDeckAndHand.add(new Card(Card.MINUS, 3));
        this.aiDeckAndHand.add(new Card(Card.MINUS, 2));
        this.aiDeckAndHand.add(new Card(Card.MINUS, 1));
        //setMediumDeck();
    }
    private void setMediumDeck()
    {
        this.aiDeckAndHand.add(new Card(Card.MINUS, 1));
        this.aiDeckAndHand.add(new Card(Card.MINUS, 2));
        this.aiDeckAndHand.add(new Card(Card.MINUS, 4));
        this.aiDeckAndHand.add(new Card(Card.MINUS, 5));
        this.aiDeckAndHand.add(new Card(Card.MINUS, 6));
        this.aiDeckAndHand.add(new Card(Card.PM, 1));
        this.aiDeckAndHand.add(new Card(Card.PM, 4));
        this.aiDeckAndHand.add(new Card(Card.PM, 3));
        this.aiDeckAndHand.add(new Card(Card.PM, 6));
        this.aiDeckAndHand.add(new Card(Card.PLUS, 4));
    }
    private void setHardDeck()
    {
        this.aiDeckAndHand.add(new Card(Card.PM, 1));
        this.aiDeckAndHand.add(new Card(Card.PM, 6));
        this.aiDeckAndHand.add(new Card(Card.PM, 4));
        this.aiDeckAndHand.add(new Card(Card.PM, 5));
        //setMediumDeck();
    }
    private void trimDeck()
    {
        Random handSelector = new Random(difficulty);
        while(aiDeckAndHand.size() >= 5)
        {
            aiDeckAndHand.remove(handSelector.nextInt(aiDeckAndHand.size()) % aiDeckAndHand.size());
        }
    }
    public Card getCard(int aiScore, List<Card> mainDeck)
    {
        List<Card> copyOfMainDeck = new ArrayList<>(mainDeck);
        if(aiDeckAndHand.size() == 0) //no Cards in hand
            return null;
        //Card cardToPlay;
        int cardToPlayID = hitTwenty(aiScore);
        if(cardToPlayID >= 0)
        {
            //cardToPlay = aiDeckAndHand.remove(cardToPlayID);
            return aiDeckAndHand.remove(cardToPlayID);
        }
        cardToPlayID = getBestCard(aiScore, getDeckAverage(copyOfMainDeck));
        if(cardToPlayID >= 0)
        {
            //cardToPlay = aiDeckAndHandAndHand.remove(cardToPlayID);
            return aiDeckAndHand.remove(cardToPlayID);
        }
        return null; //No card to play was found.
    }
    private int hitTwenty(int score)
    {
        for(int i = 0; i < aiDeckAndHand.size(); i++)
        {
            if(aiDeckAndHand.get(i).getType() == Card.PM)
            {
                if(aiDeckAndHand.get(i).getValue() + score == 20) {
                plusOrMinus = PLUS;
                return i;
                }
                else if(aiDeckAndHand.get(i).getValue() - score == 20)
                {
                    plusOrMinus = MINUS;
                    return i;
                }
            }
            else if(aiDeckAndHand.get(i).getValue() + score == 20)
            {
                return i;
            }
        }
        return -1;
    }
    private int getBestCard(int score, float deckAverage)
    {
        //int cardID = -1;
        for(int cardID = 0; cardID < aiDeckAndHand.size(); cardID++)
        {
            if(aiDeckAndHand.get(cardID).getType() == Card.MINUS)
            {
                if((score > 20) && (aiDeckAndHand.get(cardID).getValue() + score <= 20))
                    return cardID;
                else if(score + (int)deckAverage + aiDeckAndHand.get(cardID).getValue() == 20)
                    return cardID;
            }
            else if(aiDeckAndHand.get(cardID).getType() == Card.PM)
            {
                if((score > 20) && ((-aiDeckAndHand.get(cardID).getValue() + score) <= 20))
                {
                    this.plusOrMinus = MINUS;
                    return cardID;
                }
                else if((score + (int)deckAverage + aiDeckAndHand.get(cardID).getValue()) == 20)
                {
                    this.plusOrMinus = PLUS;
                    return cardID;
                }
                else if((score + (int)deckAverage - aiDeckAndHand.get(cardID).getValue()) == 20)
                {
                    this.plusOrMinus = MINUS;
                    return cardID;
                }
            }
            else
            {
                int scorePlusAverage = score + (int)deckAverage;
                int scorePlusCard = score + aiDeckAndHand.get(cardID).getValue();
                if(scorePlusAverage < scorePlusCard && scorePlusCard <= 20)
                    return cardID;
                else if(scorePlusCard + (int)deckAverage == 20)
                    return cardID;
            }
        }
        return -1;
    }

    public boolean choosePlusOrMinus(int aiScore, Card plusMinusCard)
    {
        return this.plusOrMinus;
    }
    public Card testGetCard()
    {
        if(aiDeckAndHand.size() == 0)
            return null;

        return  aiDeckAndHand.remove(0);
    }

}
